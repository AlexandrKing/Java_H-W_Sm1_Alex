package com.example.todolist.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a file attachment for a task.
 */
@Entity
@Table(name = "task_attachments")
public class TaskAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @CreatedDate
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    public TaskAttachment() {
    }

    public TaskAttachment(String fileName, String contentType, Long fileSize, String filePath) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskAttachment that = (TaskAttachment) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(fileSize, that.fileSize) &&
                Objects.equals(filePath, that.filePath) &&
                Objects.equals(uploadedAt, that.uploadedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, contentType, fileSize, filePath, uploadedAt);
    }

    @Override
    public String toString() {
        return "TaskAttachment{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}