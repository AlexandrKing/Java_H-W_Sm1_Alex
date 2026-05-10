# To-Do List Manager - Homework 2 (Advanced)

This is an advanced Spring Boot implementation of a To-Do List Manager with DTOs, validation, file attachments, session management, and OpenAPI documentation.

## Features

### Core Features (Homework 1)
- ✅ Spring Boot REST API with layered architecture
- ✅ Dependency injection and autowiring
- ✅ Spring AOP for cross-cutting concerns
- ✅ Bean lifecycle management (@PostConstruct, @PreDestroy)
- ✅ In-memory task repository

### Advanced Features (Homework 2)
- ✅ **DTOs (Data Transfer Objects)** - Separate request/response models
  - `TaskCreateDto` - For creating new tasks
  - `TaskUpdateDto` - For updating tasks
  - `TaskResponseDto` - For API responses
  - Validation groups (OnCreate, OnUpdate)
  
- ✅ **MapStruct** - Automatic entity-DTO mapping with type safety
  - `TaskMapper` interface with Spring integration
  
- ✅ **Bean Validation (JSB 380)** - Input validation with groups
  - `@NotBlank`, `@Size`, `@FutureOrPresent` annotations
  - Custom validator: `@DueDateNotBeforeCreation`
  
- ✅ **File Attachments** - Upload/download files for tasks
  - `AttachmentService` - In-memory file storage
  - `AttachmentController` - File endpoints
  - Multipart file support
  
- ✅ **Session & Cookie Support** - User preference management
  - `UserPreferencesService` - Session-based storage
  - `PreferencesController` - Preference endpoints
  - Favorites management
  - Sort and theme preferences
  
- ✅ **Global Exception Handling** - Centralized error responses
  - `GlobalExceptionHandler` - Unified error formatting
  - Custom status codes and error messages
  - Field-level validation error details
  
- ✅ **CORS Configuration** - Cross-origin request support
  - Configurable origins and methods
  
- ✅ **OpenAPI/Swagger Documentation** - Interactive API docs
  - Swagger UI at `/swagger-ui.html`
  - API docs at `/v3/api-docs`
  - Full endpoint documentation with descriptions

## Project Structure

```
semestr2/
├── src/
│   ├── main/java/com/example/todolist/
│   │   ├── attachment/              # File management
│   │   │   ├── AttachmentDto.java
│   │   │   ├── AttachmentService.java
│   │   │   └── AttachmentController.java
│   │   ├── config/                  # Spring configuration
│   │   │   ├── CorsConfig.java
│   │   │   └── OpenApiConfig.java
│   │   ├── controller/              # REST controllers
│   │   │   ├── TaskController.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── AttachmentController.java
│   │   ├── dto/                     # Data transfer objects
│   │   │   ├── OnCreate.java (validation group)
│   │   │   ├── OnUpdate.java (validation group)
│   │   │   ├── TaskCreateDto.java
│   │   │   ├── TaskUpdateDto.java
│   │   │   └── TaskResponseDto.java
│   │   ├── exception/               # Custom exceptions
│   │   │   └── TaskNotFoundException.java
│   │   ├── favorites/               # User preferences
│   │   │   ├── UserPreferencesService.java
│   │   │   └── PreferencesController.java
│   │   ├── mapper/                  # MapStruct mappers
│   │   │   └── TaskMapper.java
│   │   ├── model/                   # Domain models
│   │   │   ├── Task.java
│   │   │   └── Priority.java (enum)
│   │   ├── repository/              # Data access
│   │   │   ├── TaskRepository.java
│   │   │   ├── InMemoryTaskRepository.java
│   │   │   └── StubTaskRepository.java
│   │   ├── service/                 # Business logic
│   │   │   └── TaskService.java
│   │   ├── validation/              # Custom validators
│   │   │   ├── DueDateNotBeforeCreation.java (annotation)
│   │   │   └── DueDateNotBeforeCreationValidator.java
│   │   ├── aspect/                  # AOP aspects
│   │   └── ToDoListApplication.java # Main application
│   └── test/java/com/example/todolist/
│       └── controller/
│           ├── TaskControllerTest.java
│           └── TaskIntegrationTest.java
└── pom.xml                          # Maven dependencies
```

## API Endpoints

### Task Management
- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task

### File Attachments
- `POST /api/tasks/{taskId}/attachments` - Upload file
- `GET /api/tasks/{taskId}/attachments/{fileName}` - Download file
- `DELETE /api/tasks/{taskId}/attachments/{fileName}` - Delete attachment

### User Preferences
- `POST /api/preferences/favorites/{taskId}` - Add to favorites
- `DELETE /api/preferences/favorites/{taskId}` - Remove from favorites
- `GET /api/preferences/favorites` - Get all favorites
- `GET /api/preferences/favorites/{taskId}` - Check if favorite
- `POST /api/preferences/sort-preference` - Set sort preference
- `GET /api/preferences/sort-preference` - Get sort preference
- `POST /api/preferences/theme` - Set theme preference
- `GET /api/preferences/theme` - Get theme preference

## Validation Rules

### Creating Tasks
- Title: Required, 3-100 characters
- Description: Optional, max 500 characters
- Priority: Required (LOW, MEDIUM, HIGH)
- Due Date: Optional, must be in future or present
- Tags: Optional, max 5 tags

### Updating Tasks
- Title: Optional, 3-100 characters if provided
- Description: Optional, max 500 characters
- Completed: Optional, boolean
- Due Date: Optional, must be in future or present
- Priority: Optional
- Tags: Optional, max 5 tags
- Custom Validation: Due date cannot be before original creation date

## Building and Running

```bash
# Build the project
mvn clean package

# Run tests
mvn test

# Run the application
mvn spring-boot:run
```

## Accessing Swagger UI

Once the application is running, visit:
```
http://localhost:8080/swagger-ui.html
```

## Technologies Used

- **Spring Boot 2.7.x** - Web framework
- **Spring Web** - REST support
- **Spring AOP** - Aspect-oriented programming
- **Spring Validation** - Bean validation with groups
- **MapStruct** - DTO mapping with code generation
- **SpringDoc OpenAPI** - Swagger/OpenAPI documentation
- **JUnit 5** - Testing framework
- **Mockito** - Mocking library
- **Maven** - Build tool

## Notes

- Tasks are stored in-memory and will be lost on application restart
- File attachments are stored in-memory and not persisted
- User preferences are session-based and tied to HTTP session lifecycle
- CORS is enabled for all origins and standard HTTP methods
- Custom validation ensures data consistency (e.g., due date not before creation date)
