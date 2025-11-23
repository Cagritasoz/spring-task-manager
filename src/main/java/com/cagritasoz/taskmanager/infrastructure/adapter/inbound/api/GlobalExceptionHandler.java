package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.domain.exception.UserAlreadyExistsException;
import com.cagritasoz.taskmanager.domain.exception.UserDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/* ======================================
     @RestControllerAdvice EXPLANATION
   =====================================
  @RestControllerAdvice is a Spring annotation used to:
  -Intercept exceptions thrown from any @RestController
  -Apply global logic to all REST controllers
  -Provide centralized exception handling
  -Return JSON responses instead of HTML error pages
  -Avoid writing try/catch in every controller
  -It is basically: @ControllerAdvice + @ResponseBody. -> So every method in it returns JSON by default.
  !!!@ResponseBody tells Spring to serialize the object in the response body to JSON (or XML if configured differently) using a message converter like Jackson.
  -It can be used only on classes
  -For normal CRUD apps → one is enough and recommended.
*/
@RestControllerAdvice
public class GlobalExceptionHandler {


/* ===============================
    @ExceptionHandler EXPLANATION
   ===============================
  @ExceptionHandler is used to handle exceptions thrown in controller methods.
  Can be applied inside a controller or in @RestControllerAdvice or @ControllerAdvice classes to handle exceptions globally.
  It means "If a method in any controller throws UserAlreadyExistsException, call this method to handle it."

  -If used inside a @RestController class only concerns that very @RestController.(Controller level)
  -If used inside a @RestControllerAdvice concerns all the @RestController's.(Global level)

  -Parameter: Accepts one or more exception classes it handles.
  -For example: @ExceptionHandler({UserAlreadyExistsException.class, AnotherException.class})
  -If we throw a subclass of the handled exception, it is caught too.
 */
    @ExceptionHandler(UserAlreadyExistsException.class)
/* ======================================
       ResponseEntity<T> EXPLANATION
   ======================================
  ResponseEntity<T> is a Spring generic class that represents a full HTTP response.
  T is the object type of which the response's body will have.

  It contains:
  1.HTTP status code → 200 OK, 201 Created, 404 Not Found and more
  2.Headers → optional HTTP headers
  (Headers are basically metadata sent along with the HTTP response)
  (We can add as many headers as we like)
  (Some headers are special and influence client behavior (Content-Type, Location, Cache-Control))
  3.Body → the actual content (generic T) that will be sent in the response

  -One body only per ResponseEntity.
  -A body is not required for a ResponseEntity. It is optional. It is possible to have a response with no body at all.
  -Body object is serialized to JSON by default.
  -If we want multiple objects in the body, wrap them in a container object or collection, for example a list.

  ResponseEntity Examples:
  1.return ResponseEntity.status(HttpStatus.CREATED).body(body)
  -status() method can be used to set the HTTP response status codes.
  -Some examples are: status(HttpStatus.OK) -> Code 200. status(HttpStatus.CONFLICT) -> Code 409.

  -body() method is used to attach the body to the response. body() takes an object as a parameter. It finalizes the response.
  -Object is serialized to JSON by default because of @ResponseBody.

  2.return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
  -Here we have no body in the response. In this case we can finalize the response by calling the build() method.

  3.return ResponseEntity.ok(user)
  -This code is shorthand for "return ResponseEntity.status(HttpStatus.OK).body(body)"
  -It is possible to have shortcuts for some status codes like HttpStatus.OK (ok()) or HttpStatus.NO_CONTENT (noContent())

  4.return ResponseEntity.noContent().build()
  -This code is the same as "return ResponseEntity.status(HttpStatus.NO_CONTENT).build()"
  -Shortcut for HttpStatus.NO_CONTENT is used.

  5.return ResponseEntity.ok().build()
  -If the ok() method has no arguments, it obviously means no body for the response.
  -If there is no body for the response we still have to finalize it with build()

  6.return new ResponseEntity<>(user, HttpStatus.OK)
  -Here we use the constructor to create the response. This one is less common to do but still valid.
  -This code is equal to "return ResponseEntity.ok(user)"

  7.return ResponseEntity.ok()
        .header("X-Custom-Header", "SomeValue")
        .body("Hello World");

  -Here with the method header() we can add a single header to our response.
  -We can also see that we can use the body() method with shortcut methods such as ok()

  8.HttpHeaders headers = new HttpHeaders();
    headers.add("X-Header-One", "Value1");
    headers.add("X-Header-Two", "Value2");

    return ResponseEntity.ok()
        .headers(headers)
        .body("Multiple headers example");

  -HttpHeaders is a convenient Spring class for managing multiple headers.
  -We can set up a container for headers, add some headers and attach them to the response with the headers() method.

  ======================================
        SPECIAL/STANDARD HEADERS
   =====================================

  Special / Standard Headers:
  1.Content-Type
  -Content-Type specifies the format of the body in an HTTP request or response. (JSON or XML among others)
  -In requests: tells the server how to parse the incoming data (JSON, XML, form-data).
  -In responses: tells the client what format the returned body is in.
  -It tells the server or client how to interpret the data being sent.

  -Very important because if we send JSON data but don’t set Content-Type, the client may not parse it correctly.

  Common Values:
  1.application/json → JSON
  2.application/xml -> XML
  3.text/plain → plain text
  4.text/html → HTML

  Example of Content-Type header:
  HttpHeaders headers = new HttpHeaders();
  headers.setContentType(MediaType.APPLICATION_JSON);
  return ResponseEntity.ok().headers(headers).body(Map.of("message", "Hello JSON"));

  2.Authorization
  -Contains credentials to authenticate a user.
  -Example: Authorization: Bearer <JWT>
  -Use case: Used to secure your endpoints. Typically, for JWT or OAuth tokens.

  3.Accept
  -It is used only in HTTP requests, sent by the client.
  -It tells the server: “These are the formats I can understand in the response. Please return one of them.”
  -Example: Accept: application/json, application/xml
  -If no Accept is specified we return JSON by default.
*/
    public ResponseEntity<Map<String,String>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //In these methods the HTTP responses body will be of type Map<String, String>. Because it is easy to send an error message.
    //For more structured responses, we can define a custom error DTO instead.

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<Map<String, String>> handleUserDoesNotExistException(UserDoesNotExistException e) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
/* ======================================
             HTTP EXPLANATION
   ======================================
  -Every HTTP response has a 3-digit status code that clients (frontend, Postman, etc.) can use to react.
  Code Examples:
  1. 1xx Informational: Rarely used in REST APIs

  2. 2xx Success: Request succeeded
  -200 OK → Request succeeded, example: GET /users returns list of users (HttpStatus.OK)
  -201 Created → Resource created, example: POST /users returns new user (HttpStatus.CREATED)
  -204 No Content → Request succeeded, no body, example: DELETE /users/1 (HttpStatus.NO_CONTENT)

  3. 3xx Redirection: Client needs to follow redirect

  4. 4xx Client errors: Invalid request or unauthorized
  -400 Bad Request → Invalid syntax or params, example: missing required JSON field (HttpStatus.BAD_REQUEST)
  -401 Unauthorized → Not authenticated, example: no API token or wrong credentials (HttpStatus.UNAUTHORIZED)
  -403 Forbidden → Authenticated but not allowed, example: deleting someone else’s account (HttpStatus.FORBIDDEN)
  -404 Not Found → Resource doesn’t exist, example: GET /users/999 (HttpStatus.NOT_FOUND)
  -409 Conflict → Conflict with existing state, e.g., register with an existing email (HttpStatus.CONFLICT)

  5. 5xx Server errors: Something went wrong on the server
-------------------------------------------------------------------------------------------------------------------------
  -URI (Uniform Resource Identifier) for localhost: "http://localhost:8080"
  -URI format: scheme://hostname[:port]/path/to/doc[?param]
  -URL (Uniform Resource Locator) is a type of URI; URI is more general and preferred.

  -A URI (Uniform Resource Identifier) is a broader term for a string that identifies a resource by
  -name, location, or both, while a URL (Uniform Resource Locator) is a specific type of URI that identifies a
  -resource by its location and the means to access it.

  -HTTP = HyperText Transfer Protocol
  -HTTPS = HyperText Transfer Protocol Secure

  -HTTP runs on top of the TCP/IP stack:
  -IP (Internet Protocol) handles addressing and routing packets to the correct machine
  -TCP (Transmission Control Protocol) ensures reliable, ordered, error-checked delivery of data

  -HTTP is the application-level protocol used for web communication (GET, POST, JSON, etc.)
  -HTTP requests and responses travel inside TCP packets

  -HTTP is stateless:
  -Each request/response is processed independently
  -The client must send all necessary information with each request
  -The server does not retain memory of previous requests

  -HTTP servlet:
  - A servlet is a Java class that handles HTTP requests and generates responses.
  - It runs on a servlet container (like Tomcat) and implements the Servlet API.
  - It provides methods like doGet(), doPost(), doPut(), doDelete() for handling specific HTTP methods.
  - Spring abstracts servlets so you usually work with @RestController instead of implementing doGet/doPost manually.

  -Tomcat Embedded Server
  -Tomcat is a servlet container and web server that can run Java web applications.
  -It manages servlets, maps HTTP requests to the correct servlet, and handles the HTTP lifecycle.
  -Spring Boot often uses an embedded Tomcat server, meaning Tomcat runs inside the Spring Boot application JAR.

  -Embedded Server:
  -Spring Boot packages an embedded servlet container (Tomcat) into the application.
  -We do not need to install a separate web server; running the app starts the server automatically.
  -This server listens on a TCP port (default 8080) and handles incoming HTTP requests.

  -Spring HTTP Request Handling:
  -Spring MVC maps HTTP requests to @Controller/@RestController methods using annotations like @GetMapping, @PostMapping.
  -Spring uses DispatcherServlet (a front controller) to route requests to the appropriate controller method.
  -DispatcherServlet is itself a servlet, so Spring leverages the servlet API while providing a higher-level, annotation-based abstraction.
  -Each request is handled in a stateless manner unless explicitly managed with sessions or security context.

  -HTML = HyperText Markup Language
 */


}
