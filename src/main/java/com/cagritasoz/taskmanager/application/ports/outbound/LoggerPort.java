package com.cagritasoz.taskmanager.application.ports.outbound;

public interface LoggerPort {

    void logInfo(String message);

    void logInfo(String message, Object object);

    void logInfo(String message, Object o1, Object o2);

    void logInfo(String message, Object o1, Object o2, Object o3);

    void logInfo(String message, Object o1, Object o2, Object o3, Object o4);

    void logWarn(String message);

    void logWarn(String message, Object object);

    void logWarn(String message, Object o1, Object o2);

    void logWarn(String message, Object o1, Object o2, Object o3);

    void logWarn(String message, Object o1, Object o2, Object o3, Object o4);

}
