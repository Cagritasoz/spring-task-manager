package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.domain.ports.inbound.UserUseCase;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.RegisterUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.RegisterUserResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/* ======================================
        @RestController EXPLANATION
   ======================================
  @RestController is a Spring stereotype annotation used to define a REST web controller.
  Combines two annotations: @Controller + @ResponseBody.

  -@Controller marks the class as a Spring MVC controller.
  -@ResponseBody tells Spring to serialize the return value of each method directly into the HTTP response body as JSON(default)
  or XML if specified.

  -Use @RestController when building APIs where methods return data objects (DTOs) instead of HTML pages.

  -Methods in a @RestController are mapped to HTTP requests using annotations like:
  -@GetMapping, @PostMapping, @PutMapping, @DeleteMapping, etc.

  -All public methods return data by default, and Spring automatically handles JSON serialization using Jackson.
 */
@RestController

/* ======================================
        @RequestMapping EXPLANATION
   ======================================
  @RequestMapping maps HTTP requests to controller methods or classes.
  Can be applied at class level or method level.

  This is how a method-level mapping could look like:
  -@RequestMapping(value = "/all", method = RequestMethod.GET)
  -HTTP method is specified as GET and the mapping with value = "/all"
 */
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserUseCase userUseCase;

    private final UserDtoMapper userDtoMapper;

    public UserController(UserUseCase userUseCase, UserDtoMapper userDtoMapper) {
        this.userUseCase = userUseCase; //Example of DI.
        this.userDtoMapper = userDtoMapper; //Example of DI.
    }
/* ======================================
         @PostMapping EXPLANATION
   ======================================
  @PostMapping is a specialized shortcut for @RequestMapping with method = POST.
  It is commonly used for creating new resources (like users, tasks, orders)
  The string "/register" specifies the URL path this method listens to.

  Key points:
  1.The method typically consumes a request body (like JSON, XML). We use @RequestBody to bind it to a Java object.
  2.We can specify content types explicitly using 'consumes' and 'produces' attributes.
  -Example: @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
  -With consumes/produces we force strict rules.
  -Without them Spring will attempt to support every format for which it has converters.
  -If an HTTP request with the header Content-Type: text/plain is sent Spring might try the StringHttpMessageConverter and give unexpected results.
  3.POST requests are generally used for operations that change server state (like creating, updating, or triggering processes).

 */
    @PostMapping("/register")

/* ======================================
         @RequestBody EXPLANATION
   ======================================
  @RequestBody Binds the HTTP request body to a Java object.
  Automatically deserializes JSON into the specified object type using HttpMessageConverters (Jackson by default).

  -Works with @Valid to trigger validation on the incoming request object.
  -Useful for POST, PUT, PATCH requests where the client sends data in the body.

  -@RequestBody requires a JSON body by default. If the body is missing → 400 Bad Request.
  -To be able to read XML we have to add dependencies to Jackson in our pom.xml file.

  -Example Scenario:
  1.Client sends an HTTP request to our backend with the headers: Content-Type: application/xml, Accept: application/json
  2.This means that the HTTP request's body is in the XML format. The HTTP response to this HTTP request should have the
  body format of JSON. This is indicated by Accept: application/json
  3.Without consumes, Spring accepts any media type that it has a converter for. We have a JSON reader by default so if an HTTP
  request comes in with a JSON body it will be parsed no problem. If we want to parse xml we have to add a dependency to get a xml reader.
  4.Without produces, Spring produces whatever the client requests. Spring uses the Accept header to choose the response converter.
  5.So without consumes and produces Spring will attempt to support every format for which it has converters.

  @PostMapping(
    value = "/register",
    consumes = { "application/json", "application/xml" },
    produces = { "application/json", "application/xml" }
    )
  -In this code we indicate that this POST method can either receive a body in JSON or XML and produce either JSON or XML.
  -It would choose which one to consume with the header Content-Type and which one to produce with the header Accept.
  This is done automatically by Spring. We never manually read headers like Content-Type or Accept. Spring automates it.
  -No other format is supported and the method won't work if any other format is sent with a request.
  -If we do not have consumes or produces, and we have a xml reader the method would work fine because without
  consumes or produces Spring will attempt to support every format for which it has converters.
  -Consumes and produces can be used with ANY HTTP method annotation in Spring. Not just @PostMapping.
 */
    public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        User user = userDtoMapper.toDomain(registerUserRequest);

        RegisterUserResponse registerUserResponse = userDtoMapper.toDto(userUseCase.registerUser(user));

        return ResponseEntity.status(HttpStatus.CREATED).body(registerUserResponse);

    }
 /* ======================================
         @DeleteMapping EXPLANATION
    ======================================
  @DeleteMapping handles HTTP DELETE requests.
  It is used when the client wants to delete a resource on the server.

  -Typically combined with @PathVariable, since deletions usually identify
  a specific resource through its ID (Primary key) in the URL.

  Common responses:
  1. 204 No Content → Successful deletion, and no response body is returned.
     (ResponseEntity<Void> indicates that the HTTP response has no body.)
  2. 404 Not Found → The resource does not exist or cannot be found.
  */
    @DeleteMapping("/{id}")

/* ======================================
        @PathVariable EXPLANATION
   ======================================
  @PathVariable extracts values directly from the URL path.
  It binds a part of the URL (like {id}) to a method parameter.

  Example:
  DELETE http://localhost:8080/api/v1/users/5
  @DeleteMapping("/{id}") → {id} is automatically injected into the method.

  Important notes:
  -The curly braces `{}` in the URL path are required to mark a segment as a variable.
  Without them, Spring treats the segment as a literal string.
  -By default, the name inside `{}` must match the method parameter name: {id} → @PathVariable Long id
  -If the names differ, you can explicitly map them:
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long id) { ... }

  Use @PathVariable when:
  1. Identifying a specific resource (user, task, product, etc.).
  2. The value is part of the resource’s unique location/identity.
  3. Common pattern examples: GET /tasks/10, PUT /users/7, DELETE /orders/3
 */
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userUseCase.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
