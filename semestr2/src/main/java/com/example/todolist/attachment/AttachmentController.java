package com.example.todolist.attachment;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.model.TaskAttachment;
import com.example.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing task attachments.
 * Provides endpoints for uploading and downloading files.
 */
@RestController
@RequestMapping("/api/tasks/{taskId}/attachments")
@Tag(name = "Task Attachments", description = "Operations for managing task file attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final TaskService taskService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService, TaskService taskService) {
        this.attachmentService = attachmentService;
        this.taskService = taskService;
    }

    /**
     * Upload a file attachment for a task.
     * @param taskId the task ID
     * @param file the file to upload
     * @return uploaded attachment metadata
     */
    @PostMapping
    @Operation(summary = "Upload attachment", description = "Upload a file attachment to a task")
    public ResponseEntity<TaskAttachment> uploadAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file) {

        // Verify task exists
        if (!taskService.getTaskById(taskId).isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        TaskAttachment uploaded = attachmentService.uploadAttachment(taskId, file);
        return ResponseEntity.ok(uploaded);
    }

    /**
     * Get all attachments for a task.
     * @param taskId the task ID
     * @return list of attachments
     */
    @GetMapping
    @Operation(summary = "Get task attachments", description = "Get all file attachments for a task")
    public ResponseEntity<List<TaskAttachment>> getTaskAttachments(@PathVariable Long taskId) {
        // Verify task exists
        if (!taskService.getTaskById(taskId).isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        List<TaskAttachment> attachments = attachmentService.getTaskAttachments(taskId);
        return ResponseEntity.ok(attachments);
    }

    /**
     * Download a file attachment from a task.
     * @param taskId the task ID
     * @param attachmentId the attachment ID
     * @return file content with appropriate headers
     */
    @GetMapping("/{attachmentId}")
    @Operation(summary = "Download attachment", description = "Download a file attachment from a task")
    public ResponseEntity<FileSystemResource> downloadAttachment(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId) {

        // Verify task exists
        if (!taskService.getTaskById(taskId).isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        Optional<TaskAttachment> attachmentOpt = attachmentService.getAttachment(taskId, attachmentId);
        if (attachmentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TaskAttachment attachment = attachmentOpt.get();
        File file = new File(attachment.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(attachment.getContentType() != null ? attachment.getContentType() : "application/octet-stream"))
                .body(resource);
    }

    /**
     * Delete a file attachment from a task.
     * @param taskId the task ID
     * @param attachmentId the attachment ID
     * @return no content response
     */
    @DeleteMapping("/{attachmentId}")
    @Operation(summary = "Delete attachment", description = "Delete a file attachment from a task")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId) {

        // Verify task exists
        if (!taskService.getTaskById(taskId).isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        boolean deleted = attachmentService.deleteAttachment(taskId, attachmentId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
