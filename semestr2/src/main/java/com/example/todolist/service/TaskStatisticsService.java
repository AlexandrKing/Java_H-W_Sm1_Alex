package com.example.todolist.service;

import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for task statistics using JPA repository.
 */
@Service
public class TaskStatisticsService {

    private final TaskRepository taskRepository;

    /**
     * Constructor injection of TaskRepository.
     * @param taskRepository the JPA repository for tasks
     */
    @Autowired
    public TaskStatisticsService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Gets the total number of tasks.
     * @return the count of all tasks
     */
    public long getTotalTaskCount() {
        return taskRepository.count();
    }
}