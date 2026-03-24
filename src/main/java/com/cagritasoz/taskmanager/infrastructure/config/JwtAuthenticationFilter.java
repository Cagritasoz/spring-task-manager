package com.cagritasoz.taskmanager.infrastructure.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/v1/auth/"); //Do not run for public rest endpoints. Ignore jwt even if sent.
    }

    /*
    -By default, in modern Spring Security (since Spring Boot 3.0),
    the security filter chain does run again for a forwarded request to /error.

    -A request is forwarded to /error when an exception occurs and is then unhandled so not caught.
     */

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("Jwt authentication attempt.");

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email; //We use the email as the username in the jwt token!

        if(authHeader == null || !authHeader.startsWith("Bearer ")) { //No JWT sent.

            log.warn("No JWT sent with request.");
            filterChain.doFilter(request, response); //Forward the request to the next filter.
            return; //Stop the execution of this filter. Rest of the code is not ran after forwarding request.

        }

        jwt = authHeader.substring(7);

        try {

            email = jwtUtils.extractUsername(jwt);

            log.info("Jwt authentication attempt. Email: {} ", email);

            log.info("Current authentication in context: {}", SecurityContextHolder.getContext().getAuthentication());

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) { //null means no authentication has been set yet.

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                log.info("LoadUserByUsername method did not throw any exception.");

                if(jwtUtils.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.info("JWT authentication successful. Email: {}, URI: {}",
                            email, request.getRequestURI());

                }
            }

        }

        catch (JwtException | UsernameNotFoundException e) {

            SecurityContextHolder.clearContext();

            log.warn("JWT authentication failed. Reason: {}, URI: {}",
                    e.getMessage(), request.getRequestURI());

            //If we use return keyword here, request is not forwarded. Method security fails.

        }

        filterChain.doFilter(request, response); //Since there is no more code to run in this filter
        //we can just forward it. No return needed here.

    }
}
