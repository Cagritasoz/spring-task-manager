package com.cagritasoz.taskmanager.infrastructure.config;

import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.warn("Unauthorized request. Reason: {}, URI: {}",
                authException.getMessage(), request.getRequestURI()); //Exceptions come here from FilterSecurityInterceptor. Method security.

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                401,
                "Unauthorized");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();

    }
}
