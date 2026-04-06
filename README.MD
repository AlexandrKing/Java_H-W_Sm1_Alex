# To-Do List Manager

A Spring Boot application for managing to-do tasks with REST API.

## Features

- CRUD operations for tasks
- In-memory and stub repositories
- Dependency injection with @Primary and @Qualifier
- Bean lifecycle management with @PostConstruct and @PreDestroy
- AOP logging
- RESTful API endpoints
- Unit tests with Spring Boot Test
- CI/CD with GitHub Actions

## API Endpoints

- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create a new task
- `PUT /api/tasks/{id}` - Update a task
- `DELETE /api/tasks/{id}` - Delete a task

## Running the Application

1. Ensure you have Java 11 and Maven installed.
2. Clone the repository.
3. Navigate to the project directory.
4. Run `mvn spring-boot:run`.

The application will start on port 8080 (or 8081 for prod profile).

## Profiles

- `dev`: Detailed logging
- `prod`: Production settings with port 8081

## Testing

Run tests with `mvn test`.

## Project Structure

```
src/main/java/com/example/todolist/
├── aspect/
│   └── LoggingAspect.java
├── config/
│   ├── AppConfig.java
│   ├── RequestScopedBean.java
│   ├── PrototypeScopedBean.java
│   └── TaskLifecycleProcessor.java
├── controller/
│   └── TaskController.java
├── model/
│   └── Task.java
├── repository/
│   ├── InMemoryTaskRepository.java
│   ├── StubTaskRepository.java
│   └── TaskRepository.java
├── service/
│   ├── TaskService.java
│   └── TaskStatisticsService.java
└── ToDoListApplication.java
```