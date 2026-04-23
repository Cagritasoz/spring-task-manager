package com.cagritasoz.taskmanager.application.service.testutil;

import com.cagritasoz.taskmanager.domain.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class DataFactory {

    public static User adminUser() {
        return new User(1L, "admin", "admin@example.com", "encoded_admin", Role.ADMIN);
    }

    public static User regularUser() {
        return new User(2L, "user", "user@example.com", "encoded_user", Role.USER);
    }

    public static User regularUserWithId(Long id) {
        return new User(id, "user" + id, "user" + id + "@example.com", "encoded_pass", Role.USER);
    }

    public static User unsavedUser() {
        return new User(null, "newuser", "newuser@example.com", "plain_pass", Role.USER);
    }

    public static Task taskWithId(Long taskId, Long userId) {
        return new Task(taskId, "Task " + taskId, "Description for task " + taskId,
                LocalDateTime.now().plusDays(7), userId);
    }

    public static Task unsavedTask(Long userId) {
        return new Task(null, "New Task", "New Task Description",
                LocalDateTime.now().plusDays(7), userId);
    }

    public static Pagination<User> userPage(List<User> users) {
        return new Pagination<>(users, 0, 10, List.of(), users.size(), 1);
    }

    public static Pagination<Task> taskPage(List<Task> tasks) {
        return new Pagination<>(tasks, 0, 10, List.of(), tasks.size(), 1);
    }
}
