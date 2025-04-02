# JavaProjects

## Overview
This is a CRUD REST API built with Java, designed to manage user data. The application leverages modern technologies and tools to ensure scalability and maintainability.

## Technologies Used
- **Spring Boot**: For building the REST API.
- **Hibernate**: For ORM and database interaction.
- **PostgreSQL**: As the relational database.
- **Docker**: For containerizing the application.
- **Docker Compose**: For orchestrating the application and database containers.
- **SpringDoc OpenAPI**: For API documentation.

## Features
The application provides the following endpoints:
1. **Create a User**: Add a new user to the database.
2. **Read All Users**: Retrieve a list of all users.
3. **Read a User by ID**: Retrieve a specific user by their ID.
4. **Update a User**: Modify an existing user's details.
5. **Delete a User**: Remove a user by their ID.

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
   docker compose build
   docker compose up java_app
   ```

4. **Access the Application**:
   - API Base URL: `http://localhost:8080/api/users`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints
### User Management
- **GET /api/users**: Retrieve all users.
- **POST /api/users**: Create a new user.
  - Example Request Body:
    ```json
    {
      "name": "John Doe",
      "email": "john.doe@example.com"
    }
    ```
- **GET /api/users/{id}**: Retrieve a user by ID.
- **PUT /api/users/{id}**: Update a user by ID.
  - Example Request Body:
    ```json
    {
      "name": "Jane Doe",
      "email": "jane.doe@example.com"
    }
    ```
- **DELETE /api/users/{id}**: Delete a user by ID.

## Testing
### Unit and Integration Tests
- Tests are written using JUnit and Mockito.
- Test cases cover CRUD operations and exception handling.
- Example: Verifying user not found exceptions.

### Running Tests
```bash
mvn test
```

## Docker Details
- **Application Container**:
  - Image: `denkatastoynev/java-app:latest`
  - Port: `8080`
- **Database Container**:
  - Image: `postgres:12`
  - Port: `5432`
  - Environment Variables:
    - `POSTGRES_USER=postgres`
    - `POSTGRES_PASSWORD=postgres`
    - `POSTGRES_DB=postgres`

## Notes
- The application uses environment variables for database configuration.
- Swagger documentation is automatically generated for all endpoints.
- The database schema is automatically updated using Hibernate's `ddl-auto=update`.

## Future Improvements
- Add authentication and authorization.
- Enhance test coverage for edge cases.
- Implement CI/CD pipelines for automated builds and deployments.
