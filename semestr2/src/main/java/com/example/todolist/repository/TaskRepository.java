package com.example.todolist.repository;

import com.example.todolist.model.Task;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Task entities.
 * Provides CRUD operations for tasks.
 */
public interface TaskRepository {

    /**
     * Finds all tasks.
     * @return a list of all tasks
     */
    List<Task> findAll();

    /**
     * Finds a task by its ID.
     * @param id the ID of the task
     * @return an Optional containing the task if found, or empty if not
     */
    Optional<Task> findById(Long id);

    /**
     * Saves a task.
     * @param task the task to save
     * @return the saved task
     */
    Task save(Task task);

    /**
     * Deletes a task by its ID.
     * @param id the ID of the task to delete
     */
    void deleteById(Long id);

    /**
     * Checks if a task exists by its ID.
     * @param id the ID of the task
     * @return true if the task exists, false otherwise
     */
    boolean existsById(Long id);
}