package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.logger;

import com.cagritasoz.taskmanager.application.ports.outbound.LoggerPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggerAdapter implements LoggerPort {


    @Override
    public void logInfo(String message) {
        log.info(message);
    }

    @Override
    public void logInfo(String message, Object... args) {
        log.info(message, args);
    }

    @Override
    public void logWarn(String message) {
        log.warn(message);
    }

    @Override
    public void logWarn(String message, Object... args) {
        log.warn(message, args);
    }
}
