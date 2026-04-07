package com.example.todolist.repository;

import com.example.todolist.model.Task;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of TaskRepository.
 * This is the primary repository for managing tasks in memory.
 */
@Repository
@Primary
public class InMemoryTaskRepository implements TaskRepository {

    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator.getAndIncrement());
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void deleteById(Long id) {
        tasks.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return tasks.containsKey(id);
    }

    @Override
    public List<Task> findByCompletedAndPriority(boolean completed, com.example.todolist.model.Priority priority) {
        return tasks.values().stream()
                .filter(task -> task.isCompleted() == completed)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Task> findTasksDueWithin7Days(java.time.LocalDate today, java.time.LocalDate nextWeek) {
        return tasks.values().stream()
                .filter(task -> !task.isCompleted() &&
                        task.getDueDate() != null &&
                        !task.getDueDate().isBefore(today) &&
                        !task.getDueDate().isAfter(nextWeek))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public <S extends Task> List<S> findAll(org.springframework.data.domain.Example<S> example, org.springframework.data.domain.Sort sort) {
        // Simple implementation - return all tasks (ignoring example and sort for now)
        return (List<S>) findAll();
    }
}