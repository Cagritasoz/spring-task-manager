package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 70)
    private String title;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    /*
    Only the user_id is required when inserting a Task to the database.
    The UserEntity can be set using a JPA reference (getReferenceById, proxy object)
    so the full User does not need to be fetched from the database. This is for optimization.
     */

    /*
    @ManyToOne -> Many tasks belong to one user.
    LAZY: User is not loaded from the database unless getUserEntity() is called.
    optional = false: Every task MUST be associated with a user (cannot be null).
    optional ensures that if we try to persist a task entity with user entity field as null. JPA/Hibernate rejects it.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)

    /*
    @JoinColumn Maps the foreign key column "user_id" in the task table.
    nullable = false: The DB will not allow a task without a user_id.
     */
    @JoinColumn(name = "user_id", nullable = false)

    /*
    Database-level cascade delete.
    When a User is deleted, all associated Tasks are automatically deleted
    by the database (ON DELETE CASCADE). This prevents orphan records
    and is more efficient than deleting tasks manually.
    Because my system uses a unidirectional (Task -> User), we have to handle delete cascading in the DB.
    @OnDelete annotation adds a foreign key constraint to the database schema.
     */
    @OnDelete(action = OnDeleteAction.CASCADE) //On delete works!
    private UserEntity user;

}

//@Entity determines the tables. The database schema consists of tables, columns(name, column attributes like nullable
//or unique), primary keys, foreign keys, relationships, cascades, constraints, indexes and more. For all of these
//concepts jpa/hibernate provides annotations so we can directly influence database schema.
