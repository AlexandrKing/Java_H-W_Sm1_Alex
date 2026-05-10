package com.example.todolist.repository;

import com.example.todolist.model.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing TaskAttachment entities.
 * Provides CRUD operations for task attachments.
 */
@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {

    /**
     * Finds all attachments for a specific task.
     * @param taskId the task ID
     * @return list of attachments for the task
     */
    List<TaskAttachment> findByTaskId(Long taskId);

    /**
     * Deletes all attachments for a specific task.
     * @param taskId the task ID
     */
    void deleteByTaskId(Long taskId);
}