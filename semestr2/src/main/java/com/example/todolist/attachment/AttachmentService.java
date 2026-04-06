package com.example.todolist.attachment;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for managing task attachments.
 * Handles file upload and download operations.
 */
@Service
public class AttachmentService {

    // In-memory storage of attachments (taskId -> attachmentMap)
    private final Map<Long, Map<String, AttachmentDto>> attachments = new HashMap<>();

    /**
     * Upload an attachment for a task.
     * @param taskId the task ID
     * @param attachment the attachment to upload
     * @return the stored attachment
     */
    public AttachmentDto uploadAttachment(Long taskId, AttachmentDto attachment) {
        attachments.computeIfAbsent(taskId, k -> new HashMap<>())
                   .put(attachment.getFileName(), attachment);
        return attachment;
    }

    /**
     * Download an attachment for a task.
     * @param taskId the task ID
     * @param fileName the file name
     * @return the attachment if found, null otherwise
     */
    public AttachmentDto downloadAttachment(Long taskId, String fileName) {
        return attachments.getOrDefault(taskId, new HashMap<>()).get(fileName);
    }

    /**
     * Delete an attachment for a task.
     * @param taskId the task ID
     * @param fileName the file name
     */
    public void deleteAttachment(Long taskId, String fileName) {
        Map<String, AttachmentDto> taskAttachments = attachments.get(taskId);
        if (taskAttachments != null) {
            taskAttachments.remove(fileName);
        }
    }

    /**
     * Get all attachments for a task.
     * @param taskId the task ID
     * @return map of file names to attachments
     */
    public Map<String, AttachmentDto> getAttachments(Long taskId) {
        return attachments.getOrDefault(taskId, new HashMap<>());
    }
}
