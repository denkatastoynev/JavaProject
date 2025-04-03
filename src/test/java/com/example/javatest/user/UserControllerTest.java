package com.example.javatest.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Arrange: Create mock users and configure the repository to return them
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act & Assert: Perform GET request and verify the response
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("John Doe"))
               .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    void testGetUserById() throws Exception {
        // Arrange: Create a mock user and configure the repository to return it
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert: Perform GET request for a specific user and verify the response
        mockMvc.perform(get("/api/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testCreateUser() throws Exception {
        // Arrange: Create a mock user and configure the repository to save it
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act & Assert: Perform POST request to create a user and verify the response
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testUpdateUser() throws Exception {
        // Arrange: Create an existing user and an updated user
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John Doe");
        existingUser.setEmail("john@example.com");

        User updatedUser = new User();
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act & Assert: Perform PUT request to update the user and verify the response
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Arrange: Create a mock user and configure the repository to delete it
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        // Act & Assert: Perform DELETE request to delete the user and verify the response
        mockMvc.perform(delete("/api/users/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("User with id: 1 deleted successfully"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange: Create a mock user and configure the repository to return it
        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setName("John Doe");

        when(userRepository.findByEmail("john@example.com")).thenReturn(user);

        // Act & Assert: Perform POST request and verify the response
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        // Arrange: Configure the repository to return null for invalid email
        when(userRepository.findByEmail("invalid@example.com")).thenReturn(null);

        // Act & Assert: Perform POST request and verify the response
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User("invalid@example.com", "wrongpassword"))))
               .andExpect(status().isUnauthorized())
               .andExpect(content().string("Invalid email or password"));
    }

    @Test
    void testLoginMissingFields() throws Exception {
        // Act & Assert: Perform POST request with missing fields and verify the response
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("Email and password must not be null"));
    }

    @Test
    void testFindUserByEmail() {
        // Arrange: Create a mock user
        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("password123");

        when(userRepository.findByEmail("john@example.com")).thenReturn(user);

        // Act: Call the repository method
        User foundUser = userRepository.findByEmail("john@example.com");

        // Assert: Verify the result
        assertNotNull(foundUser);
        assertEquals("john@example.com", foundUser.getEmail());
    }

    @Test
    void testSaveUser() {
        // Arrange: Create a new user
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setPassword("securepassword");

        when(userRepository.save(user)).thenReturn(user);

        // Act: Save the user
        User savedUser = userRepository.save(user);

        // Assert: Verify the saved user
        assertNotNull(savedUser);
        assertEquals("Jane Doe", savedUser.getName());
        assertEquals("jane@example.com", savedUser.getEmail());
    }

    @Test
    void testDeleteUserById() {
        // Arrange: Mock the repository behavior
        doNothing().when(userRepository).deleteById(1L);

        // Act: Delete the user
        userRepository.deleteById(1L);

        // Assert: Verify the interaction
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateUserWithMissingFields() throws Exception {
        // Arrange: Create a user with missing fields
        User user = new User();
        user.setName("John Doe");

        // Act & Assert: Perform POST request and verify bad request response
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("User name, email, and password must not be null"));
    }

    @Test
    void testCreateUserWithInvalidEmail() throws Exception {
        // Arrange: Create a user with an invalid email
        User user = new User();
        user.setName("John Doe");
        user.setEmail("invalid-email");
        user.setPassword("password123");

        // Act & Assert: Perform POST request and verify bad request response
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateNonExistentUser() throws Exception {
        // Arrange: Configure repository to return empty for non-existent user
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");

        // Act & Assert: Perform PUT request and verify error response
        mockMvc.perform(put("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
               .andExpect(status().isInternalServerError())
               .andExpect(content().string("User not found for update with id: 999"));
    }

    @Test
    void testDeleteNonExistentUser() throws Exception {
        // Arrange: Configure repository to return empty for non-existent user
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert: Perform DELETE request and verify error response
        mockMvc.perform(delete("/api/users/999"))
               .andExpect(status().isOk())
               .andExpect(content().string("Error deleting user with id: 999 not found"));
    }
}
