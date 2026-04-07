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

    @Override
    public List<Task> findByCompletedAndPriority(boolean completed, com.example.todolist.model.Priority priority) {
        return stubTasks.stream()
                .filter(task -> task.isCompleted() == completed)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Task> findTasksDueWithin7Days(java.time.LocalDate today, java.time.LocalDate nextWeek) {
        // Stub implementation - return empty list
        return new ArrayList<>();
    }

    @Override
    public <S extends Task> List<S> findAll(org.springframework.data.domain.Example<S> example, org.springframework.data.domain.Sort sort) {
        // Stub implementation - return empty list
        return new ArrayList<>();
    }

    @Override
    public <S extends Task> List<S> findAll(org.springframework.data.domain.Example<S> example) {
        // Stub implementation - return empty list
        return new ArrayList<>();
    }

    @Override
    public Task getReferenceById(Long id) {
        Task task = stubTasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Task not found with id " + id));
        return task;
    }
}