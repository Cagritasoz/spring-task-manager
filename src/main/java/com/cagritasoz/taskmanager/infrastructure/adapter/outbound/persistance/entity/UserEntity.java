package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity;

import com.cagritasoz.taskmanager.domain.model.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 22)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}

/* =========================================
        RELATIONSHIPS / ASSOCIATIONS
   =========================================
  @OneToMany annotation indicates a one-to-many relationship. When used on a field it means that side is the "one" side.
  @ManyToOne annotation indicates a many-to-one relationship. When used on a field it means that side is the "many" side.

  -For bidirectional mapping to be achieved user has to know about their tasks and task has to know about its user.
  -Bidirectional mapping allows navigation both ways: User → Tasks, Task → User.

  -A user can have multiple tasks but a task can only belong to one user. UserEntity -> One side, TaskEntity -> Many side.
  -The “one side” or “many side” refers to the entity as a whole, not a specific field.
  -This means that user has to have the field List<TaskEntity> tasks, task has to have the field UserEntity user.
  -In a bidirectional sense the one side (UserEntity) typically has a List or Set of the many-side entity (TaskEntity).
   and the many side (TaskEntity) has a single object reference to the one-side entity (UserEntity). This is the standard.

  -For a proper bidirectional relationship, since both entities should know about each other, we use @OneToMany annotation
   on UserEntity.task and @ManyToOne annotation on TaskEntity.user

  -Terms parent/child side and inverse/owner side are introduced. Owner side(TaskEntity) has the foreign key.
  -The foreign key is determined with @JoinColumn(name = "user_id"). Foreign key now has the name "user_id".
  -@JoinColumn is used on the owner side which is TaskEntity.
  -A foreign key is a column (or a set of columns) in one table that references the primary key of another table.

  -Inverse side is determined by mappedBy = "user". This tells JPA that the inverse side is UserEntity.
  -The value is the name of the owner-side field.
  -mappedBy attribute is used on the inverse side(UserEntity).
  -Tells JPA “look at this field on the other entity -> that side owns the FK”.

  -To properly set up a bidirectional mapping, we both have use @JoinColumn and mappedBy to avoid confusions and clarity.
  -It is best practise to use them for clarity and no unpredictability, do not avoid them.

  =========================================
            FETCHING STRATEGY
  =========================================
  -Fetch means to load data from the database into memory (i.e., converting rows into entity objects)
  -Fetch types can be specified as attributes in the @OneToMany or @ManyToOne annotations.

  - fetch = FetchType.LAZY (default for OneToMany) → tasks are loaded only when accessed (user.getTasks()).
  (Avoids potential unnecessary memory load)

  - fetch = FetchType.EAGER (default for ManyToOne) → tasks loaded immediately (usually discouraged for large collections).
  (Ensures parent entity is loaded with child if used on the "one" side)

  -Accessing a lazy collection triggers a separate SQL query.
  -Use JOIN FETCH in queries if you want to load collections efficiently in a single query.
  -Beware of N+1 query problem when fetching collections in loops.

   ========================================
                  CASCADING
   ========================================
  -CascadeType defines which operations propagate from parent to child entities.
  -Common types: ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH.

  -Examples:
  1. CascadeType.ALL → operations on User propagate to Tasks (persist, merge, remove etc.)
  2.Cascading is almost always defined on the parent/inverse side.

  -orphanRemoval = true, is an attribute we can add to @OneToMany or @OneToOne relationships.
  -It tells JPA:
  -“If a child entity is removed from the parent’s collection (or unlinked from the parent)
   automatically delete it from the database.”
 */
