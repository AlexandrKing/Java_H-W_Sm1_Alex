package com.example.todolist.service;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing tasks.
 * Provides business logic for task operations.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Constructor injection of TaskRepository.
     * @param taskRepository the repository to use
     */
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @EntityGraph(attributePaths = {"attachments"})
    public List<Task> getAllTasksWithAttachments() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, Task updatedTask) {
        if (taskRepository.existsById(id)) {
            updatedTask.setId(id);
            return Optional.of(taskRepository.save(updatedTask));
        }
        return Optional.empty();
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Marks multiple tasks as completed in a single transaction.
     * If any task ID doesn't exist, the entire operation is rolled back.
     * @param ids list of task IDs to mark as completed
     * @throws TaskNotFoundException if any task ID doesn't exist
     */
    @Transactional
    public void bulkCompleteTasks(List<Long> ids) {
        for (Long id : ids) {
            Optional<Task> taskOpt = taskRepository.findById(id);
            if (!taskOpt.isPresent()) {
                throw new TaskNotFoundException("Task with id " + id.toString() + " not found");
            }
            Task task = taskOpt.get();
            task.setCompleted(true);
            taskRepository.save(task);
        }
    }

    /**
     * Finds tasks due within the next 7 days.
     * @return list of tasks due within 7 days
     */
    public List<Task> getTasksDueWithin7Days() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        return taskRepository.findTasksDueWithin7Days(today, nextWeek);
    }
}