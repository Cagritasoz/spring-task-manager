package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper;

import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.RegisterUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.RegisterUserResponse;
import org.springframework.stereotype.Component;

/* ==============================
      @Component EXPLANATION
   ==============================
  @Component is a Spring stereotype annotation that marks a class as a Spring-managed bean.
  Spring automatically detects @Component during component scanning and creates the bean.
  @Component is meant to annotate classes.

  When Spring sees a class annotated with @Component, it:
  1.Instantiates the class (creates an object)
  2.Registers it inside the Application Context
  3.Allows it to be injected with @Autowired / constructor dependency injection

  It is the most generic stereotype—used when a class does not fit into more specific ones like:
  -@Service → service/business logic
  -@Repository → persistence-layer (JPA/Hibernate)
  -@Controller or @RestController → web layer
  -All of these actually inherit behavior from @Component.

  Perfect for mapper classes to be injected via constructor injection.
 */
@Component
public class UserDtoMapper {

    public User toDomain(RegisterUserRequest registerUserRequest) {
        return new User(registerUserRequest.username(), registerUserRequest.email(), registerUserRequest.password());
    }

    public RegisterUserResponse toDto(User user) {
        return new RegisterUserResponse(user.getId(), user.getUsername(), user.getEmail());
    }

}
