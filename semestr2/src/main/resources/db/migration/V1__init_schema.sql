-- V1__init_schema.sql
-- Initial schema migration for To-Do List Manager

-- Create tasks table
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date DATE,
    priority VARCHAR(20) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH'))
);

-- Create task_tags table for tags (separate table for H2 compatibility)
CREATE TABLE task_tags (
    task_id BIGINT NOT NULL,
    tag VARCHAR(50) NOT NULL,
    PRIMARY KEY (task_id, tag),
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

-- Create task_attachments table
CREATE TABLE task_attachments (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100),
    file_size BIGINT,
    file_path VARCHAR(500),
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_attachment FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_tasks_completed ON tasks(completed);
CREATE INDEX idx_tasks_priority ON tasks(priority);
CREATE INDEX idx_tasks_due_date ON tasks(due_date);
CREATE INDEX idx_task_attachments_task_id ON task_attachments(task_id);