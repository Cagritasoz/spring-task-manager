package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.security;

import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserAdapter implements CurrentUserPort {

    @Override
    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        return securityUser.getUser();

    }
}
