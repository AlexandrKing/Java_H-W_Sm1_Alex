package com.example.todolist.service;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.model.Priority;
import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for TaskService with real database.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testCreateAndRetrieveTask() {
        // Given
        Task task = new Task();
        task.setTitle("Integration Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setPriority(Priority.MEDIUM);
        task.setDueDate(LocalDate.now().plusDays(5));

        // When
        Task savedTask = taskService.createTask(task);

        // Then
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Integration Test Task");
        assertThat(savedTask.getCreatedAt()).isNotNull();
        assertThat(savedTask.getLastModifiedDate()).isNotNull();
    }

    @Test
    public void testBulkCompleteTasks_Success() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setCompleted(false);
        Task savedTask1 = taskService.createTask(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setCompleted(false);
        Task savedTask2 = taskService.createTask(task2);

        // When
        taskService.bulkCompleteTasks(Arrays.asList(savedTask1.getId(), savedTask2.getId()));

        // Then
        Task updatedTask1 = taskService.getTaskById(savedTask1.getId()).get();
        Task updatedTask2 = taskService.getTaskById(savedTask2.getId()).get();
        assertThat(updatedTask1.isCompleted()).isTrue();
        assertThat(updatedTask2.isCompleted()).isTrue();
    }

    @Test
    public void testBulkCompleteTasks_RollbackOnNotFound() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setCompleted(false);
        Task savedTask1 = taskService.createTask(task1);

        // When & Then
        assertThatThrownBy(() -> taskService.bulkCompleteTasks(Arrays.asList(savedTask1.getId(), 999L)))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task with id 999 not found");

        // Verify rollback - task1 should still be incomplete
        Task unchangedTask = taskService.getTaskById(savedTask1.getId()).get();
        assertThat(unchangedTask.isCompleted()).isFalse();
    }

    @Test
    public void testGetTasksDueWithin7Days() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Due Soon");
        task1.setCompleted(false);
        task1.setDueDate(LocalDate.now().plusDays(3));
        taskService.createTask(task1);

        Task task2 = new Task();
        task2.setTitle("Due Later");
        task2.setCompleted(false);
        task2.setDueDate(LocalDate.now().plusDays(10));
        taskService.createTask(task2);

        Task task3 = new Task();
        task3.setTitle("Completed Task");
        task3.setCompleted(true);
        task3.setDueDate(LocalDate.now().plusDays(2));
        taskService.createTask(task3);

        // When
        List<Task> dueTasks = taskService.getTasksDueWithin7Days();

        // Then
        assertThat(dueTasks).hasSize(1);
        assertThat(dueTasks.get(0).getTitle()).isEqualTo("Due Soon");
    }
}