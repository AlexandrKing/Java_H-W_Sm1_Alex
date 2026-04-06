package com.example.todolist.controller;

import com.example.todolist.dto.OnCreate;
import com.example.todolist.dto.OnUpdate;
import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.dto.TaskResponseDto;
import com.example.todolist.dto.TaskUpdateDto;
import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.mapper.TaskMapper;
import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing tasks.
 * Provides CRUD endpoints for tasks with DTOs and validation.
 */
@RestController
@RequestMapping("/api/tasks")
@Validated
@Tag(name = "Task Management", description = "Operations for managing to-do tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Constructor injection of TaskService and TaskMapper.
     * @param taskService the service to use
     * @param taskMapper the mapper to use for DTO conversion
     */
    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    /**
     * Get all tasks.
     * @return list of task response DTOs
     */
    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    public List<TaskResponseDto> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return taskMapper.toResponseDtoList(tasks);
    }

    /**
     * Get a single task by ID.
     * @param id the task ID
     * @return task response DTO if found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieve a specific task by its ID")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(t -> ResponseEntity.ok(taskMapper.toResponseDto(t)))
                   .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * Create a new task.
     * @param createDto the task creation DTO with validation
     * @param session HTTP session for storing user context (optional feature)
     * @return created task response DTO
     */
    @PostMapping
    @Operation(summary = "Create a new task", description = "Create a new task with the provided details")
    @Validated(OnCreate.class)
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody TaskCreateDto createDto,
            HttpSession session) {
        Task task = taskMapper.toEntity(createDto);
        Task savedTask = taskService.createTask(task);
        // Store task ID in session for tracking
        session.setAttribute("lastCreatedTaskId", savedTask.getId());
        return ResponseEntity.ok(taskMapper.toResponseDto(savedTask));
    }

    /**
     * Update an existing task.
     * @param id the task ID
     * @param updateDto the task update DTO with validation
     * @return updated task response DTO
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Update an existing task with the provided details")
    @Validated(OnUpdate.class)
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDto updateDto) {
        Task existingTask = taskService.getTaskById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        Task updatedTask = taskMapper.updateEntity(updateDto, existingTask);
        Task saved = taskService.updateTask(id, updatedTask).orElseThrow(
                () -> new TaskNotFoundException(id));
        return ResponseEntity.ok(taskMapper.toResponseDto(saved));
    }

    /**
     * Delete a task by ID.
     * @param id the task ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Delete a task by its ID")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskService.getTaskById(id).isPresent()) {
            throw new TaskNotFoundException(id);
        }
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}