package com.cagritasoz.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// This is a meta-annotation that combines:
// 1. @Configuration → marks the class as a Spring config source
// 2. @EnableAutoConfiguration → auto configures Spring based on dependencies
// 3. @ComponentScan → scans this package and sub-packages for components
// It is the entry point of the entire Spring Boot application.

public class TaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}

}
