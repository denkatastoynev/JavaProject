﻿# JavaProjects
CRUD Rest API in Java using the following:
-Spring Boot
-Hibernate
-Postgres
-Docker
-Docker Compose
There are 5 endpoints for the basic CRUD operations:
-Create
-Read all
-Read one
-Update
-Delete
Ran Postgres database in a container using Docker Compose, and tested it with TablePlus.
Dockerized the Java application by writing a Dockerfile and a docker-compose.yml file to run the application and the database.
Built the Java App, built the Docker image, and ran the container using Docker Compose, then tested it with Postman.
I have created the jar file of the Java application, so we can copy it into the Docker image. Used the following command to create it: mvn clean package -DskipTests
I have skipped the tests because the tests are trying to connect to the database, but the environment variables we defined are outside the application logic.
The next step was to build the Docker image with the following command: docker compose build
Then ran the Java Maven application with the following command: docker compose up java_app
To test the application, I have used Postman, and since we do not have a test endpoint, I have just made GET request to localhost:8080/api/users
The response from the GET request should be an empty array.
Afterward, I made POST requests to localhost:8080/api/users with a body, for example, the following one: {"name":"Denislav". "email":"denislav@gmail.com"}
Then tried to get one of the users by using again the GET request, but at the end of the URL I have added the user's id number. For example: localhost:8080/api/user/5
To update the information on any user, a new body should be provided, and then the URL ending with the id of the user you want to update. For example: localhost:8080/api/users/2
To delete a single user using the DELETE request with the URL associated with the user. For example: localhost:8080/api/users/1
There are tests to determine if the user is not found, and it throws an exception message + the id of the user.
