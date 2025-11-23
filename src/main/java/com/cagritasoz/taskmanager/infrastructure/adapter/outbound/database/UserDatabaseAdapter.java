package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.database;


import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.domain.ports.outbound.UserRepository;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.database.entity.UserEntity;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.database.mapper.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//@Repository tells Spring that this class is a Data Access Object (DAO) — basically, it’s responsible for interacting with the database.
//Beans are automatically created. DI can be used.
public class UserDatabaseAdapter implements UserRepository {

    @PersistenceContext
    //Automatically provides an instance of EntityManager for your adapter class.
    //No need to manually create an entity manager with the keyword "new".
    //injects a Spring-managed EntityManager.
    //Both adapters share the same persistence context.
    private EntityManager em;

    private final UserMapper userMapper;

    public UserDatabaseAdapter(UserMapper userMapper) {
        this.userMapper = userMapper; //example of DI
    }

    //persist = Make a new entity managed and save it to the database. Usage: For new objects that don’t exist in the database yet.
    //Primary key is always used.
    //The entity must be new (ID null/0 if @GeneratedValue is used).
    //If you call persist() on an entity with an existing ID, JPA may throw: EntityExistsException → the ID already exists or the database may reject it due to primary key violation. This way a potential newly registered user is lost due to bad logic.

    //merge = Update an existing entity in the database or save a detached entity. Usage: For existing objects or objects you got outside the persistence context.
    //Primary key is always used.
    //merge() copies the state of the given object into a managed entity.
    //If the ID is null/0 → merge() acts like persist() and inserts a new row if the db allows it.
    //If the ID exists → merge() updates the existing row.

    //remove = is used to delete an entity from the database.
    //The entity you pass must be managed by the EntityManager (i.e., it is in the persistence context).
    //If you pass a detached entity (one not loaded in the current context), JPA throws
    //First load it from the database with find then remove it.
    //Cascading is in action. If a user is deleted all the tasks of the user are deleted as well.

    @Override
    public Optional<User> findById(Long id) {
        UserEntity userEntity = em.find(UserEntity.class, id); //find will return null if id doesn't exist.
        return (userEntity != null) ? Optional.of(userMapper.toDomain(userEntity)) : Optional.empty(); //Ternary operator. Short way of writing if else. Very easy to read.
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<UserEntity> users = em.createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class) //JPQL query.
                .setParameter("email", email) //In the JPQL, we can see ":email". With the setParameter we set the email to the method parameter String email.
                .getResultList(); //We get a list back even though we know the query will either return one object or no objects. With getResultList() if the user is not found an exception is NOT thrown. We do not have to handle the exceptions. We could have used getSingleResult() but with this if the user was not found an exception would have occurred which we should have caught. Exception handling should be done in the service layer. Minimal exception or null checks in the database adapter methods.

        return users.stream()
                .findFirst() //Returns an Optional. Optional might be either empty or contain a value inside.
                .map(userMapper::toDomain); // "::" is the method reference. map is called on the Optional not on the stream. If a value exists inside the Optional, map method transforms that value with a Function<T,V> or a method reference. If the value is not there Optional.empty is returned. Simple.
    }

    @Override
    public User saveUser(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        em.persist(userEntity); //By calling persist the userEntity becomes managed by the EntityManager. This means it’s now tracked by JPA's persistence context (in-memory).
        return userMapper.toDomain(userEntity); //After persist is called the database generates the ID then JPA reads the generated ID from the database and sets it on the userEntity object in memory.
    }

    @Override
    public boolean deleteUser(Long id) {

        UserEntity userEntity = em.find(UserEntity.class, id);

        if(userEntity != null) { //user exists in the database
            em.remove(userEntity);
            return true;
        }

        return false;

    }
}
