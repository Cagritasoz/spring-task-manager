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
    public void logInfo(String message, Object object) {

        log.info(message, object);

    }

    @Override
    public void logInfo(String message, Object o1, Object o2) {

        log.info(message, o1, o2);

    }

    @Override
    public void logInfo(String message, Object o1, Object o2, Object o3) {

        log.info(message, o1, o2, o3);

    }

    @Override
    public void logInfo(String message, Object o1, Object o2, Object o3, Object o4) {

        log.info(message, o1, o2, o3, o4);

    }

    @Override
    public void logWarn(String message) {

        log.warn(message);

    }

    @Override
    public void logWarn(String message, Object object) {

        log.warn(message, object);

    }

    @Override
    public void logWarn(String message, Object o1, Object o2) {

        log.warn(message, o1, o2);

    }

    @Override
    public void logWarn(String message, Object o1, Object o2, Object o3) {

        log.warn(message, o1, o2, o3);

    }

    @Override
    public void logWarn(String message, Object o1, Object o2, Object o3, Object o4) {

        log.warn(message, o1, o2, o3, o4);

    }
}
