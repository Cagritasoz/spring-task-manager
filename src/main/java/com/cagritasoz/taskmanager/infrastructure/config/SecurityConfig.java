package com.cagritasoz.taskmanager.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //.authenticationProvider(authenticationProvider) //Set the authentication provider
    //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //Configure the SecurityFilterChain.

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

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                //Had the jwt auth filter after ExceptionTranslationFilter. Decided to change that again.




        return httpSecurity.build();
    }
}

/*

-Default filters in order in spring. These filters are default filters,
and they can be disabled or skipped for custom SecurityFilterChains,

1. WebAsyncManagerIntegrationFilter: Integrates Spring Security with Spring
MVC's asynchronous request handling.

2. SecurityContextPersistenceFilter: Manages the SecurityContext lifecycle,
ensuring it is available at the beginning of a request and cleared or saved to the HttpSession at the end.

3. HeaderWriterFilter: Adds security-related HTTP headers to the response,
such as X-Frame-Options and X-XSS-Protection.

4. CsrfFilter: Enforces protection against Cross-Site Request Forgery
(CSRF) attacks (enabled by default for session-based applications).

5. LogoutFilter: Handles user logout requests by clearing
the security context and invalidating the session.

6. Authentication Filters (e.g., UsernamePasswordAuthenticationFilter, BasicAuthenticationFilter):
Process specific authentication mechanisms. UsernamePasswordAuthenticationFilter handles form-based login,
while BasicAuthenticationFilter handles HTTP Basic authentication if configured. My JwtAuthenticationFilter runs
before the UserNamePasswordAuthenticationFilter.

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

7. RequestCacheAwareFilter: Restores the original request details
after a successful authentication, typically a redirect to a login page.

8. SecurityContextHolderAwareRequestFilter: Wraps the HttpServletRequest to provide access
to the SecurityContext and authentication details.

9. AnonymousAuthenticationFilter: Provides an anonymous authentication object to the SecurityContextHolder if no other authentication has occurred,
allowing for secure access to resources configured for anonymous users and that downstream filters do not have to
operate on a null value.

10. SessionManagementFilter: Manages session-related tasks such as session
fixation protection and concurrent session control.

11. ExceptionTranslationFilter: Catches Spring Security exceptions (AuthenticationException and AccessDeniedException) and
translates them into appropriate HTTP responses or redirects (e.g., to a login page or an error page).

!!! ExceptionTranslationFilter
-ExceptionTranslationFilter can only catch exceptions that are thrown by filters
that come AFTER it in the Spring Security filter chain.
-Wraps the filter chain that come after it. If any filter throws AuthenticationException(or exceptions that extend this),
it calls AuthenticationEntryPoint's commence method. Unauthorized -> 401
-If AccessDeniedException(or exceptions that extend this), calls AccessDeniedHandler's handle method.
Forbidden -> 403


12. FilterSecurityInterceptor: Performs the final access control check
based on the security configuration and access rules for the requested resource.
Method security is enforced here.
!!! FilterSecurityInterceptor
-Checks if the current Authentication in SecurityContextHolder
has sufficient roles/permissions for the requested URL or method (@PreAuthorize).
-Throws AuthenticationException if unauthenticated, AccessDeniedException if authenticated but unauthorized.
-Authorization check for whitelisted rest endpoints are skipped in this filter!

13. Controller are reached after the security filter chain.
*/

/* THIS WAS THE INITIAL THOUGH PROCESS. REALISED LATER THAT THIS IS NOT GOOD.
 * BUG / LESSON LEARNED: JWT exception handling in custom filter
 *
 * JWT exceptions (like signature invalid, malformed token, or username not found)
 * do not extend AuthenticationException by default. To have Spring Security handle
 * them properly, we catch them in the JwtAuthenticationFilter and throw a custom
 * exception that **does** extend AuthenticationException. This allows
 * ExceptionTranslationFilter to intercept it and call the configured
 * AuthenticationEntryPoint (commence method) to send a proper 401 response.
 *
 * Key points:
 * 1. Logging: Catching these exceptions in the filter allows us to log specific
 *    messages (e.g., SignatureException vs. MalformedJwtException) and keep track
 *    of authentication failures in detail.
 *
 * 2. Filter order matters: Originally, this filter ran **before** ExceptionTranslationFilter.
 *    As a result, the thrown exceptions were not caught, and Tomcat forwarded the request
 *    to /error, producing stack traces in the console, even though Postman saw the correct
 *    JSON response assembled in handlers.
 *
 * 3. Correct flow: Placing the JWT filter **after** ExceptionTranslationFilter ensures:
 *      - Exceptions are caught by ExceptionTranslationFilter
 *      - AuthenticationEntryPoint sends the proper response
 *      - No /error forwarding occurs
 *      - Filters do not re-run and the request lifecycle stops after the response is sent
 *
 * 4. Design choice: This approach keeps JWT-related exception handling in its domain,
 *    rather than forwarding to a global @RestControllerAdvice. It centralizes auth logic
 *    while allowing precise logging.
 */

/*
 JWT AUTHENTICATION FLOW DESIGN DECISION

 I initially tried placing the JwtAuthenticationFilter AFTER ExceptionTranslationFilter
 and throwing a custom exception that extends AuthenticationException(abstract class) inside the JWT filter so that
 ExceptionTranslationFilter would catch it and call custom handler class that implements AuthenticationEntryPoint.

 However, this approach made the system more complex and harder to reason about.
 The main reason I wanted that setup was to log specific exceptions (JwtException,
 UsernameNotFoundException), but this can be done directly inside the JWT filter anyway.

 CURRENT DESIGN (SIMPLER AND CLEANER):

 1. JwtAuthenticationFilter responsibilities:
    - Extract JWT from Authorization header
    - Validate JWT
    - Load user from database using UserDetailsService
    - If everything is valid -> set Authentication in SecurityContextHolder
    - If JWT is invalid or user not found -> log the reason and clear context
    - DO NOT throw AuthenticationException here
    - Always continue filter chain
    - Let ExceptionTranslationFilter catch AuthenticationExceptions and AccessDeniedExceptions
     that occur from method security

 2. Why we catch exceptions inside JwtAuthenticationFilter:
    - If JwtException or UsernameNotFoundException is not caught,
      the exception will escape the filter and a stack trace will appear in the terminal. Spring automatically forwards
      the request path to /error. Meaning we did something wrong.
    - We want to handle JWT-related problems here and just treat the user as anonymous.
    - So we catch:
        JwtException
        UsernameNotFoundException
      Then we log and clear SecurityContextHolder.

 3. What happens after JwtAuthenticationFilter:
    - If authentication was NOT set -> request continues as anonymous. SecurityContextHolder holds a null value.
    This null value is then later set as an AnonymousAuthenticationToken later down the filter chain to ensure
    that down stream filters like FilterSecurityInterceptor can work with a non-null value.
    - If authentication WAS set -> request continues as authenticated user.

 4. Method-level security / authorization phase:
    This is handled later by Spring Security automatically.

    If endpoint is protected:
        - No authentication -> AuthenticationException -> AuthenticationEntryPoint(custom in my case) -> 401
        - Authenticated but wrong role -> AccessDeniedException -> AccessDeniedHandler(custom in my case) -> 403
        - Authenticated and authorized -> Controller runs -> 200

 5. Important architecture principle:
    - JwtAuthenticationFilter -> ONLY handles authentication (who are you?)
    - Spring Security (method security) -> handles authentication and authorization (who are you?, are you allowed?)

 6. Exception flow summary:

        Request
           ↓
        JwtAuthenticationFilter
           ↓
        (Authentication set OR not set)
           ↓
        FilterSecurityInterceptor / Method Security
           ↓
        If not authenticated -> AuthenticationException -> AuthenticationEntryPoint(custom) -> 401
        If not authorized   -> AccessDeniedException -> AccessDeniedHandler(custom) -> 403
        If OK               -> Controller -> 200

 7. This approach is better because:
    - Clear separation of responsibilities
    - No exceptions escaping filter chain
    - Cleaner logging
    - Simpler filter order
    - Matches Spring Security design

*/

/*
-/error path is protected by default. If an exception occurs and is not caught request gets forwarded to /error.
If an exception is thrown at this point in my code it is likely thrown in the security layer or validation layer.
My authentication fails for /error path even if jwt succeeds. This causes an AuthenticationException therefore commence
method is called.

 */

