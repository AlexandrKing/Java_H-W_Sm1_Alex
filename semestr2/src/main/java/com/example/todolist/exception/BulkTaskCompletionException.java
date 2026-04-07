package com.example.todolist.exception;

/**
 * Exception thrown when bulk task completion fails due to non-existent task IDs.
 */
public class BulkTaskCompletionException extends RuntimeException {

    private final Long taskId;

    public BulkTaskCompletionException(Long taskId) {
        super("Task with id " + taskId + " not found during bulk completion");
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }
}