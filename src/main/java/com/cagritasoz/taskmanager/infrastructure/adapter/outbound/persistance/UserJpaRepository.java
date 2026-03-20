package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance;

import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    //Automatically builds the query. We just write the proper method name here, and it is provided as a method.

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

}
