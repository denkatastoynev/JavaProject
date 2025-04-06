package com.uni.projectmanager.user;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.uni.projectmanager.security.JwtUtil;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user", description = "Add a new user to the database")
    @PostMapping
    public ResponseEntity<?> createUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User object to be created",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "User Example",
                    value = "{ \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password123\" }"
                )
            )
        )
        @RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @Operation(summary = "Update a user", description = "Update an existing user's details")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User object with updated details",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Updated User Example",
                    value = "{ \"name\": \"Jane Doe\", \"email\": \"jane.doe@example.com\", \"password\": \"newpassword123\" }"
                )
            )
        )
        @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = "Delete a user", description = "Delete a user by their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User with id: " + id + " deleted successfully");
    }

    @Operation(summary = "Login", description = "Authenticate a user by email and password")
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User loginRequest) {
    return userService.login(loginRequest.getEmail(), loginRequest.getPassword())
            .map(user -> {
                String token = JwtUtil.generateToken(user.getEmail());
                return ResponseEntity.ok(token);
            })
            .orElse(ResponseEntity.status(401).body("Invalid email or password"));
}

}
