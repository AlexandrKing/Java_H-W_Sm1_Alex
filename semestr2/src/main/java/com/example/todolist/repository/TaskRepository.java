package com.example.todolist.repository;

import com.example.todolist.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompletedAndPriority(boolean completed, com.example.todolist.model.Priority priority);

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :today AND :nextWeek AND t.completed = false")
    List<Task> findTasksDueWithin7Days(@Param("today") LocalDate today, @Param("nextWeek") LocalDate nextWeek);

    @EntityGraph(attributePaths = "attachments")
    @Query("SELECT t FROM Task t")
    List<Task> findAllWithAttachments();
}