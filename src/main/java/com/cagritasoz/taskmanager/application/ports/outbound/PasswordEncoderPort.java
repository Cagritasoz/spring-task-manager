package com.cagritasoz.taskmanager.application.ports.outbound;

public interface PasswordEncoderPort {

    String encodePassword(String password);

}
