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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String email = null; //We use the email as the username in the jwt token!

        if(authHeader == null || !authHeader.startsWith("Bearer ")) { //No JWT sent.

            filterChain.doFilter(request, response); //Forward the request to the next filter.
            return; //Stop the execution of this filter. Rest of the code is not ran after forwarding request.

        }

        jwt = authHeader.substring(7);

        try {

            email = jwtUtils.extractUsername(jwt);

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) { //null means user is not authenticated yet.

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

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

                    log.info("JWT authentication successful. Email: {}, IP: {}, URI: {}",
                            email, request.getRemoteAddr(), request.getRequestURI());

                }
            }

        }

        catch (AuthenticationException e) {

            SecurityContextHolder.clearContext(); //clear context

            log.warn("JWT authentication failed - user not found. Email: {}, IP: {}, URI: {}",
                        email, request.getRemoteAddr(), request.getRequestURI());

            throw new JwtAuthenticationException("Authorization failed");

        }

        catch (JwtException e) {

            SecurityContextHolder.clearContext();

            log.warn("JWT authentication failed. Reason: {}, IP: {}, URI: {}",
                    e.getMessage(), request.getRemoteAddr(), request.getRequestURI());

            throw new JwtAuthenticationException("Authorization failed");

        }

        filterChain.doFilter(request, response); //Since there is no more code to run in this filter
        //we can just forward it. No return needed here.

    }
}
