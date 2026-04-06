package com.example.todolist.service;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class for managing tasks.
 * Provides business logic for task operations.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final Map<Long, Task> taskCache = new ConcurrentHashMap<>();

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    /**
     * Constructor injection of TaskRepository.
     * @param taskRepository the repository to use
     */
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Initializes the cache with predefined tasks.
     */
    @PostConstruct
    public void initCache() {
        // Load some predefined tasks into cache
        taskCache.put(1L, new Task(1L, "Default Task 1", "Description 1", false));
        taskCache.put(2L, new Task(2L, "Default Task 2", "Description 2", true));
        System.out.println("Task cache initialized with " + taskCache.size() + " tasks in " + appName + " v" + appVersion + ".");
    }

    /**
     * Cleans up resources before bean destruction.
     */
    @PreDestroy
    public void cleanup() {
        System.out.println("Cleaning up TaskService. Cache contains " + taskCache.size() + " tasks.");
        // Could save statistics to file here
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, Task updatedTask) {
        if (taskRepository.existsById(id)) {
            updatedTask.setId(id);
            return Optional.of(taskRepository.save(updatedTask));
        }
        return Optional.empty();
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}