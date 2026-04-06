package com.example.todolist.attachment;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
     * @throws IOException if file reading fails
     */
    @PostMapping
    @Operation(summary = "Upload attachment", description = "Upload a file attachment to a task")
    public ResponseEntity<AttachmentDto> uploadAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file) throws IOException {
        
        // Verify task exists
        if (!taskService.getTaskById(taskId).isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        AttachmentDto attachment = new AttachmentDto(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );

        AttachmentDto uploaded = attachmentService.uploadAttachment(taskId, attachment);
        return ResponseEntity.ok(uploaded);
    }

    /**
     * Download a file attachment from a task.
     * @param taskId the task ID
     * @param fileName the file name
     * @return file content with appropriate headers
     */
    @GetMapping("/{fileName:.+}")
    @Operation(summary = "Download attachment", description = "Download a file attachment from a task")
    public ResponseEntity<ByteArrayResource> downloadAttachment(
            @PathVariable Long taskId,
            @PathVariable String fileName) {
        
        // Verify task exists
        if (!taskService.getTaskById(taskId).isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        AttachmentDto attachment = attachmentService.downloadAttachment(taskId, fileName);
        if (attachment == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(attachment.getContent());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(attachment.getContentType() != null ? attachment.getContentType() : "application/octet-stream"))
                .body(resource);
    }

    /**
     * Delete a file attachment from a task.
     * @param taskId the task ID
     * @param fileName the file name
     * @return no content response
     */
    @DeleteMapping("/{fileName:.+}")
    @Operation(summary = "Delete attachment", description = "Delete a file attachment from a task")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long taskId,
            @PathVariable String fileName) {
        
        // Verify task exists
        if (!taskService.getTaskById(taskId).isPresent()) {
            throw new TaskNotFoundException(taskId);
        }

        attachmentService.deleteAttachment(taskId, fileName);
        return ResponseEntity.noContent().build();
    }
}
