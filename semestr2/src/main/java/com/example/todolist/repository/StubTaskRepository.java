package com.example.todolist.repository;

import com.example.todolist.model.Task;

import java.util.*;

/**
 * Stub implementation of TaskRepository with fixed data.
 * Used for testing or demonstration purposes.
 * Note: Bean is created via @Bean method in AppConfig class.
 */
public class StubTaskRepository implements TaskRepository {

    private final List<Task> stubTasks = Arrays.asList(
            new Task(1L, "Learn Spring", "Study Spring Framework basics", false),
            new Task(2L, "Write tests", "Create unit tests for the application", true),
            new Task(3L, "Deploy app", "Deploy the application to production", false)
    );

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(stubTasks);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return stubTasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    @Override
    public Task save(Task task) {
        // Stub implementation - does not actually save
        return task;
    }

    @Override
    public void deleteById(Long id) {
        // Stub implementation - does not actually delete
    }

    @Override
    public boolean existsById(Long id) {
        return stubTasks.stream()
                .anyMatch(task -> task.getId().equals(id));
    }
}