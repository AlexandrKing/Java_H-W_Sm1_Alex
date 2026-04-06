package com.example.todolist.model;

/**
 * Represents a task in the To-Do List Manager.
 * This class encapsulates the properties of a task including its ID, title, description, and completion status.
 */
public class Task {
    private Long id;
    private String title;
    private String description;
    private boolean completed;

    /**
     * Default constructor.
     */
    public Task() {}

    /**
     * Constructor with all fields.
     * @param id the unique identifier of the task
     * @param title the title of the task
     * @param description the description of the task
     * @param completed the completion status of the task
     */
    public Task(Long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return completed == task.completed &&
                java.util.Objects.equals(id, task.id) &&
                java.util.Objects.equals(title, task.title) &&
                java.util.Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, title, description, completed);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }
}