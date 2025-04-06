package com.uni.projectmanager.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.projectmanager.dao.UserDAO;
import com.uni.projectmanager.user.User;
import com.uni.projectmanager.user.UserController;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDAO userDAO; // Replace UserRepository with UserDAO

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Arrange: Create mock users and configure the DAO to return them
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");

        when(userDAO.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act & Assert: Perform GET request and verify the response
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("John Doe"))
               .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    void testGetUserById() throws Exception {
        // Arrange: Create a mock user and configure the DAO to return it
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userDAO.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert: Perform GET request for a specific user and verify the response
        mockMvc.perform(get("/api/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testCreateUser() throws Exception {
        // Arrange: Create a mock user and configure the DAO to save it
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");

        doReturn(user).when(userDAO).save(any(User.class));

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

        when(userDAO.findById(1L)).thenReturn(Optional.of(existingUser));
        doReturn(updatedUser).when(userDAO).save(any(User.class));

        // Act & Assert: Perform PUT request to update the user and verify the response
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Arrange: Create a mock user and configure the DAO to delete it
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userDAO.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userDAO).deleteById(1L);

        // Act & Assert: Perform DELETE request to delete the user and verify the response
        mockMvc.perform(delete("/api/users/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("User with id: 1 deleted successfully"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Arrange: Create a mock user and configure the DAO to return it
        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setName("John Doe");

        when(userDAO.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        // Act & Assert: Perform POST request and verify the response
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        // Arrange: Configure the DAO to return empty for invalid email
        when(userDAO.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

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
}