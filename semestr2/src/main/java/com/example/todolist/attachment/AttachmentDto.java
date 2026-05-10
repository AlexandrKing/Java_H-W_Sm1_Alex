package com.example.todolist.attachment;

/**
 * DTO for file attachment metadata.
 */
public class AttachmentDto {
    private String fileName;
    private String contentType;
    private byte[] content;

    public AttachmentDto() {
    }

    public AttachmentDto(String fileName, String contentType, byte[] content) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
