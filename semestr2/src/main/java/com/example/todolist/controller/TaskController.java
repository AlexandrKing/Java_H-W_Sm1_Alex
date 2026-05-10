package com.example.todolist.controller;

import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.dto.TaskResponseDto;
import com.example.todolist.dto.TaskUpdateDto;
import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.mapper.TaskMapper;
import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import com.example.todolist.service.TaskStatisticsJdbcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for managing tasks.
 * Provides CRUD endpoints for tasks with DTOs and validation.
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "Operations for managing to-do tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskStatisticsJdbcService statisticsService;

    /**
     * Constructor injection of TaskService, TaskMapper, and TaskStatisticsJdbcService.
     * @param taskService the service to use
     * @param taskMapper the mapper to use for DTO conversion
     * @param statisticsService the statistics service to use
     */
    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper, TaskStatisticsJdbcService statisticsService) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.statisticsService = statisticsService;
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
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody TaskCreateDto createDto,
            HttpSession session) {
        Task task = taskMapper.toEntity(createDto);
        Task savedTask = taskService.createTask(task);
        // Store task ID in session for tracking
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

    /**
     * Mark multiple tasks as completed.
     * @param ids list of task IDs to mark as completed
     * @return no content response
     */
    @PostMapping("/bulk-complete")
    @Operation(summary = "Bulk complete tasks", description = "Mark multiple tasks as completed in a single transaction")
    public ResponseEntity<Void> bulkCompleteTasks(@RequestBody List<Long> ids) {
        taskService.bulkCompleteTasks(ids);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get tasks due within the next 7 days.
     * @return list of tasks due within 7 days
     */
    @GetMapping("/due-soon")
    @Operation(summary = "Get tasks due soon", description = "Retrieve tasks that are due within the next 7 days")
    public List<TaskResponseDto> getTasksDueSoon() {
        List<Task> tasks = taskService.getTasksDueWithin7Days();
        return taskMapper.toResponseDtoList(tasks);
    }

    /**
     * Get task statistics by priority.
     * @return statistics of tasks grouped by priority
     */
    @GetMapping("/statistics/priority")
    @Operation(summary = "Get task statistics by priority", description = "Retrieve statistics of task counts grouped by priority")
    public List<Map<String, Object>> getTaskStatisticsByPriority() {
        return statisticsService.getTasksCountByPriority();
    }
}