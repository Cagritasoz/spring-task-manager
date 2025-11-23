package com.cagritasoz.taskmanager.domain.service;

import com.cagritasoz.taskmanager.domain.exception.UserAlreadyExistsException;
import com.cagritasoz.taskmanager.domain.exception.UserDoesNotExistException;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.domain.ports.inbound.UserUseCase;
import com.cagritasoz.taskmanager.domain.ports.outbound.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

/* ==============================
      @Service EXPLANATION
   ==============================
  @Service is a Spring stereotype annotation used to mark a class as a service layer component.
  It tells Spring: “This class contains business logic” and “Create a Spring Bean from this class and manage its lifecycle”.
  It is functionally the same as @Component, but it has a semantic meaning: “This class belongs to the service layer.”
 */
@Service
public class UserService implements UserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
/* ==============================
     @Transactional EXPLANATION
   ==============================
  @Transactional is required because the underlying JPA operations
  (such as persist, merge, or remove) MUST run inside a database transaction.

  @Transactional controls transaction boundaries, not component scanning.

  @Transactional can be used on classes or methods:
  -If used on a class applies to all methods unless overridden
  -If used on a method only applies to that method.

  Spring automatically:
  1.opens a transaction at the start of this method,
  2.flushes entity changes to the database when the method completes,
  3.commits the transaction if the method finishes without throwing an exception,
  4.rolls back the transaction if a runtime exception is thrown.

  We use @Transactional here because the EntityManager is accessed indirectly
  through the UserRepository's JPA operations. Without it, JPA operations like
  persist(), merge(), or remove() would fail because no active transaction exists.
*/
    @Transactional
    public User registerUser(User user) {

        //Normalization of the email is important. "Most applications convert the email to lowercase before storing and comparing.",

        String email = user.getEmail();
        String normalizedEmail = email.toLowerCase();
        user.setEmail(normalizedEmail);

        Optional<User> loadedUser = userRepository.findByEmail(user.getEmail());

        if(loadedUser.isPresent()) { //If a user with the email exists we throw an exception. If not found we save it. This assures that every email is unique like we have declared in the JPA @Column's.
            throw new UserAlreadyExistsException(email);
        }

        return userRepository.saveUser(user); //We return the newly saved user object that has an ID now.
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        
        boolean isDeleted = userRepository.deleteUser(id);

        if(!isDeleted) {
            throw new UserDoesNotExistException();
        }
    }


}
