package com.cagritasoz.taskmanager.infrastructure.bootstrap;

import com.cagritasoz.taskmanager.application.ports.outbound.PasswordEncoderPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteUserPort;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final WriteUserPort writeUserPort;

    private final PasswordEncoderPort passwordEncoderPort;

    @Bean
    public CommandLineRunner seedAdmin() {

        return args -> {

            User admin = new User(null,
                    "admin",
                    "admin@gmail.com",
                    passwordEncoderPort.encodePassword("admin123"),
                    Role.ADMIN);

            writeUserPort.saveUser(admin);

            System.out.println("Admin created!");

        };

    }

}
