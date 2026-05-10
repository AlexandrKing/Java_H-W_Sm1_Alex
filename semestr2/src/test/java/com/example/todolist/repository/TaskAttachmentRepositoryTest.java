package com.example.todolist.repository;

import com.example.todolist.model.Task;
import com.example.todolist.model.TaskAttachment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for TaskAttachmentRepository using @DataJpaTest.
 */
@DataJpaTest
@ActiveProfiles("test")
public class TaskAttachmentRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAttachmentRepository attachmentRepository;

    @Test
    public void testSaveAndFindByTaskId() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setCompleted(false);
        Task savedTask = taskRepository.save(task);

        TaskAttachment attachment = new TaskAttachment();
        attachment.setTask(savedTask);
        attachment.setFileName("test.txt");
        attachment.setContentType("text/plain");
        attachment.setFileSize(100L);
        attachment.setFilePath("/uploads/test.txt");
        attachment.setUploadedAt(LocalDateTime.now());

        // When
        TaskAttachment savedAttachment = attachmentRepository.save(attachment);

        // Then
        assertThat(savedAttachment.getId()).isNotNull();
        List<TaskAttachment> foundAttachments = attachmentRepository.findByTaskId(savedTask.getId());
        assertThat(foundAttachments).hasSize(1);
        assertThat(foundAttachments.get(0).getFileName()).isEqualTo("test.txt");
    }

    @Test
    public void testDeleteByTaskId() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setCompleted(false);
        Task savedTask = taskRepository.save(task);

        TaskAttachment attachment1 = new TaskAttachment();
        attachment1.setTask(savedTask);
        attachment1.setFileName("file1.txt");
        attachment1.setContentType("text/plain");
        attachment1.setFileSize(100L);
        attachment1.setFilePath("/uploads/file1.txt");
        attachment1.setUploadedAt(LocalDateTime.now());

        TaskAttachment attachment2 = new TaskAttachment();
        attachment2.setTask(savedTask);
        attachment2.setFileName("file2.txt");
        attachment2.setContentType("text/plain");
        attachment2.setFileSize(200L);
        attachment2.setFilePath("/uploads/file2.txt");
        attachment2.setUploadedAt(LocalDateTime.now());

        attachmentRepository.save(attachment1);
        attachmentRepository.save(attachment2);

        // When
        attachmentRepository.deleteByTaskId(savedTask.getId());

        // Then
        List<TaskAttachment> remainingAttachments = attachmentRepository.findByTaskId(savedTask.getId());
        assertThat(remainingAttachments).isEmpty();
    }
}