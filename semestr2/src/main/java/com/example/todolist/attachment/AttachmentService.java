package com.example.todolist.attachment;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.model.Task;
import com.example.todolist.model.TaskAttachment;
import com.example.todolist.repository.TaskAttachmentRepository;
import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing task attachments.
 * Handles file upload and download operations with database persistence.
 */
@Service
public class AttachmentService {

    private final TaskRepository taskRepository;
    private final TaskAttachmentRepository attachmentRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Autowired
    public AttachmentService(TaskRepository taskRepository, TaskAttachmentRepository attachmentRepository) {
        this.taskRepository = taskRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @PostConstruct
    private void createUploadDirectory() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + uploadDir, e);
        }
    }

    /**
     * Upload an attachment for a task.
     * @param taskId the task ID
     * @param file the multipart file to upload
     * @return the stored attachment metadata
     */
    @Transactional
    public TaskAttachment uploadAttachment(Long taskId, MultipartFile file) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isEmpty()) {
            throw new TaskNotFoundException("Task with id " + taskId + " not found");
        }

        Task task = taskOpt.get();
        String fileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path filePath = Paths.get(uploadDir, uniqueFileName);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        TaskAttachment attachment = new TaskAttachment();
        attachment.setTask(task);
        attachment.setFileName(fileName);
        attachment.setContentType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setFilePath(filePath.toString());
        attachment.setUploadedAt(LocalDateTime.now());

        return attachmentRepository.save(attachment);
    }

    /**
     * Download an attachment for a task.
     * @param taskId the task ID
     * @param attachmentId the attachment ID
     * @return the attachment entity if found
     */
    public Optional<TaskAttachment> getAttachment(Long taskId, Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .filter(attachment -> attachment.getTask().getId().equals(taskId));
    }

    /**
     * Get all attachments for a task.
     * @param taskId the task ID
     * @return list of attachments
     */
    public List<TaskAttachment> getTaskAttachments(Long taskId) {
        return attachmentRepository.findByTaskId(taskId);
    }

    /**
     * Delete an attachment for a task.
     * @param taskId the task ID
     * @param attachmentId the attachment ID
     * @return true if deleted, false if not found
     */
    @Transactional
    public boolean deleteAttachment(Long taskId, Long attachmentId) {
        Optional<TaskAttachment> attachmentOpt = attachmentRepository.findById(attachmentId);
        if (attachmentOpt.isPresent() && attachmentOpt.get().getTask().getId().equals(taskId)) {
            TaskAttachment attachment = attachmentOpt.get();

            // Delete file from filesystem
            try {
                Files.deleteIfExists(Paths.get(attachment.getFilePath()));
            } catch (IOException e) {
                // Log error but continue with database deletion
                System.err.println("Failed to delete file: " + attachment.getFilePath());
            }

            attachmentRepository.delete(attachment);
            return true;
        }
        return false;
    }
}
