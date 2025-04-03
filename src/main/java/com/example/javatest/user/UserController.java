package com.example.javatest.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
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
        if (user == null || user.getName() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("User name, email, and password must not be null");
        }
        try {
            // Ensure the user is in a managed state
            if (user.getId() != null && userRepository.existsById(user.getId())) {
                user = userRepository.findById(user.getId()).get();
            }
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving user: " + e.getMessage());
        }
    }

    @Operation(summary = "Update a user", description = "Update an existing user's details")
    @PutMapping("/{id}")
    public User updateUser(
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
        User existingUser = userRepository.findById(id).get();
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(user.getPassword());
            }
            return userRepository.save(existingUser);
        }
        throw new RuntimeException("User not found for update with id: " + id);
    }

    @Operation(summary = "Delete a user", description = "Delete a user by their ID")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            //find the user by id
            userRepository.findById(id).get();
            //delete the user by id
            userRepository.deleteById(id);
            return "User with id: " + id + " deleted successfully";
        } catch (Exception e) {
            return "Error deleting user with id: " + id + " not found";
        }
    }

    @Operation(summary = "Login", description = "Authenticate a user by email and password")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email and password must not be null");
        }

        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

}
