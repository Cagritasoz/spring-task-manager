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
  Default fetch type for @OneToMany is LAZY.
  @ManyToOne annotation indicates a many-to-one relationship. When used on a field it means that side is the "many" side.
  Default fetch type for @ManyToOne is EAGER.

  -For bidirectional mapping to be achieved user has to know about their tasks and task has to know about its user.
  -Bidirectional mapping allows navigation both ways: User → Tasks, Task → User.

  -A user can have multiple tasks but a task can only belong to one user. UserEntity -> One side, TaskEntity -> Many side.
  -The “one side” or “many side” refers to the entity as a whole, not a specific field.
  -This means that user has to have the field List<TaskEntity> tasks, task has to have the field UserEntity user.
  -In a bidirectional sense the one side (UserEntity) typically has a List or Set of the many-side entity (TaskEntity).
   and the many side (TaskEntity) has a single object reference to the one-side entity (UserEntity). This is the standard.

  -For a proper bidirectional relationship, since both entities should know about each other, we use @OneToMany annotation
   on UserEntity.tasks and @ManyToOne annotation on TaskEntity.user

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

   ========================================
                  CASCADING
   ========================================
  -CascadeType defines which operations propagate from parent to child entities.
  -Common types: ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH.
  -Cascading is a Hibernate/JPA thing, NOT a database thing.

  1.CascadeType.ALL → operations on User propagate to Tasks (persist, merge, remove etc.)

  2.Cascading is almost always defined on the parent/inverse side. Defining cascading on the parent side
  operates on the child collection. We can define helper methods for integrity for adding a child
  entity to the collection and setting that child entities parent entity as the current parent instance.
  We should do the same for removing. This works well with orphanRemoval = true.

  3.Cascading can also be defined on the child side. Not commonly used though.

  4.Cascading applies only to operations initiated on that entity. For example if
  cascading is defined on the UserEntity class @OneToMany, if an operation is performed on a user entity object cascading
  for task entity objects are is in effect. The same is true if cascading is defined on the TaskEntity
  class @ManyToOne. If an operation is performed on the task entity cascading is in effect for user entity objects.

  5.Defining Cascading only makes sense on the entity that is able to navigate to the target entity.
  For example, if you have a unidirectional relationship: Task -> User, but not User -> Tasks,
  setting cascade on the User side would be pointless, because User cannot navigate to its tasks
  and thus cannot automatically persist, merge, or remove them through cascading.

  -orphanRemoval = true, is an attribute we can add to @OneToMany or @OneToOne relationships.
  -It tells JPA:
  -“If a child entity is removed from the parent’s collection (or unlinked from the parent)
   automatically delete it from the database.”
 */
