package com.cagritasoz.taskmanager.application.ports.inbound;


import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.User;

public interface GetUsersUseCase {

    Pagination<User> getUsers(Pagination<User> pagination);

}
