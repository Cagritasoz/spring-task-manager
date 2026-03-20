package com.cagritasoz.taskmanager.application.ports.outbound;

public interface AuthManagerPort {

    void authenticateUser(String email, String password);

}
