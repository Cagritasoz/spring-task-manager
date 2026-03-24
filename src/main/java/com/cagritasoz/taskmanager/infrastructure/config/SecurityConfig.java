package com.cagritasoz.taskmanager.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //.authenticationProvider(authenticationProvider) //Set the authentication provider
    //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // Disable CSRF because we use JWTs in headers, not cookies
                // CSRF protection is unnecessary for stateless REST APIs
                .csrf(AbstractHttpConfigurer::disable)

                // Stateless session management since JWTs handle authentication
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Define authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints: registration and login do not require authentication
                        // Note: All requests still go through the filter chain, but authentication/authorization is skipped
                        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login")
                        .permitAll()

                        // All other endpoints require authentication
                        .anyRequest()
                        .authenticated()
                )

                // Configure exception handling (can set custom AuthenticationEntryPoint / AccessDeniedHandler here)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationExceptionHandler)
                        .accessDeniedHandler(accessDeniedExceptionHandler))

                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }

    /*
       → SecurityContextPersistenceFilter (Load SecurityContext)

       !!! ExceptionTranslationFilter
       -Wraps the whole filter chain. If any filter throws AuthenticationException(or exceptions that extend this),
       it calls AuthenticationEntryPoint's commence method. Unauthorized -> 401
       -If AccessDeniedException(or exceptions that extend this), calls AccessDeniedHandler's handle method.
       Forbidden -> 403

       → JwtAuthenticationFilter (custom filter for jwt)

       !!! UsernamePasswordAuthenticationFilter
       -This filter is designed to intercept login requests. Like my login rest endpoint /api/v1/auth/login
       -By default, it listens for POST requests to /login, but we can configure this.
       -If a POST request is made to /login, a UsernamePasswordAuthenticationToken
       is created through the login request body which of course should contain
       the email and the password, and calls the authenticate
       method of AuthenticationManager. If the request is not a POST request to the specified
       rest endpoint this filter is skipped and does nothing.
       -Set SecurityContextHolder if authentication successful.
       -authenticate() method throws an exception AuthenticationException if authentication fails.

       !!! FilterSecurityInterceptor
       -Checks if the current Authentication in SecurityContextHolder
       has sufficient roles/permissions for the requested URL or method (@PreAuthorize).
       -Throws AuthenticationException if unauthenticated, AccessDeniedException if authenticated but unauthorized.
       -Authorization check for whitelisted rest endpoints are skipped in this filter!

       → Controller (@PreAuthorize)



     */

}
