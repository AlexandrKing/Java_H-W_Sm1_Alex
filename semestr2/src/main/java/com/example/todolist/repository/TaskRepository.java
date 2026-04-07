package com.example.todolist.repository;

import com.example.todolist.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing Task entities.
 * Provides CRUD operations and custom queries for tasks.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Finds tasks by completion status and priority.
     * @param completed the completion status
     * @param priority the priority level
     * @return list of tasks matching the criteria
     */
    List<Task> findByCompletedAndPriority(boolean completed, com.example.todolist.model.Priority priority);

    /**
     * Finds tasks with due dates within the next 7 days.
     * @return list of tasks due within 7 days
     */
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :today AND :nextWeek AND t.completed = false")
    List<Task> findTasksDueWithin7Days(@Param("today") LocalDate today, @Param("nextWeek") LocalDate nextWeek);

    /**
     * Finds all tasks with their attachments loaded to avoid N+1 problem.
     * @return list of tasks with eagerly loaded attachments
     */
    @EntityGraph(attributePaths = {"attachments"})
    List<Task> findAllWithAttachments();
}