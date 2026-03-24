package com.cagritasoz.taskmanager.infrastructure.config;

import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.warn("Access denied for request. Reason: {}, URI: {}",
                accessDeniedException.getMessage(), request.getRequestURI()); //Exceptions come here from FilterSecurityInterceptor. Method security.

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
                403,
                "Access Denied");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
        response.getWriter().flush(); //flush the buffer
        response.getWriter().close();
    }
}
