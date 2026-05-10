package com.example.todolist.service;

import com.example.todolist.exception.BulkTaskCompletionException;
import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getAllTasksWithAttachments() {
        return taskRepository.findAllWithAttachments();
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

    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED,
        rollbackFor = BulkTaskCompletionException.class
    )
    public void bulkCompleteTasks(List<Long> ids) {
        List<Task> tasksToUpdate = new ArrayList<>();

        for (Long id : ids) {
            Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BulkTaskCompletionException(id));
            tasksToUpdate.add(task);
        }

        for (Task task : tasksToUpdate) {
            task.setCompleted(true);
        }

        taskRepository.saveAll(tasksToUpdate);
    }

    public List<Task> getTasksDueWithin7Days() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        return taskRepository.findTasksDueWithin7Days(today, nextWeek);
    }
}