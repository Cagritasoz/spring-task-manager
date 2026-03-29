package com.cagritasoz.taskmanager.application.ports.outbound;

public interface LoggerPort {

    void logInfo(String message);

    void logInfo(String message, Object... args);

    void logWarn(String message);

    void logWarn(String message, Object... args);



}
