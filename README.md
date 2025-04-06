# JavaProjects

## Overview
This is a CRUD REST API built with Java, designed to manage user and product data. The application leverages modern technologies and tools to ensure scalability, maintainability, and ease of use.

## Technologies Used
- **Java 17**: The programming language used for the backend.
- **Spring Boot**: For building the REST API.
- **Spring Data JPA**: For database interaction and ORM.
- **Hibernate**: For ORM and database interaction.
- **PostgreSQL**: As the relational database.
- **SpringDoc OpenAPI**: For API documentation.
- **JUnit 5**: For unit and integration testing.
- **Mockito**: For mocking dependencies in tests.
- **Jackson**: For JSON serialization and deserialization.
- **Maven**: For dependency management and building the application.
- **Docker**: For containerizing the application.
- **Docker Compose**: For orchestrating the application and database containers.

## Features
The application provides the following endpoints:

### User Management
1. **Create a User**: Add a new user to the database. Requires `name`, `email`, and `password`.
2. **Read All Users**: Retrieve a list of all users.
3. **Read a User by ID**: Retrieve a specific user by their ID.
4. **Update a User**: Modify an existing user's details.
5. **Delete a User**: Remove a user by their ID.
6. **Login**: Authenticate a user by their email and password.

### Product Management
1. **Create a Product**: Add a new product to the database. Requires `name` and `price`.
2. **Read All Products**: Retrieve a list of all products.
3. **Read a Product by ID**: Retrieve a specific product by its ID.
4. **Update a Product**: Modify an existing product's details.
5. **Delete a Product**: Remove a product by its ID.

## Setup Instructions
### Prerequisites
- Docker and Docker Compose installed.
- Java 17 or higher installed (for local development).
- Maven installed (for building the application).

### Steps
1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd javatest
   ```

2. **Build the Application**:
   ```bash
   mvn clean package -DskipTests
   ```
   *Tests are skipped because they require database connectivity, which is configured externally.*

3. **Build and Run with Docker Compose**:
   ```bash
   docker compose up --build
   or
   docker compose build
   docker compose up java_app
   ```

4. **Access the Application**:
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - Front-end: `http://localhost:3000/`

## API Endpoints
### User Endpoints
#### Create a User
- **POST** `/api/users`
- **Request Body**:
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123"
  }
  ```
- **Response**: Returns the created user.

#### Login
- **POST** `/api/users/login`
- **Request Body**:
  ```json
  {
    "email": "john.doe@example.com",
    "password": "password123"
  }
  ```
- **Response**: Returns the authenticated user or an error message.

### Product Endpoints
#### Create a Product
- **POST** `/api/products`
- **Request Body**:
  ```json
  {
    "name": "Product A",
    "price": 50.0
  }
  ```
- **Response**: Returns the created product.

#### Update a Product
- **PUT** `/api/products/{id}`
- **Request Body**:
  ```json
  {
    "name": "Updated Product",
    "price": 100.0
  }
  ```
- **Response**: Returns the updated product.

### Other Endpoints
Refer to the Swagger UI for detailed documentation of all endpoints.

## Testing
- Run tests using Maven:
  ```bash
  mvn test
  ```

## Docker Details
- The application and database are containerized using Docker.
- Use `docker compose up` to start the application and database.

## Notes
- Ensure the database is running before testing or using the application.
- Passwords are stored in plain text for simplicity. Consider using encryption for production.
- The application includes both user and product management features.