package com.example.todolist.repository;

import com.example.todolist.model.Priority;
import com.example.todolist.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for TaskRepository using @DataJpaTest.
 */
@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testSaveAndFindById() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setPriority(Priority.HIGH);
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setTags(new HashSet<>(Arrays.asList("tag1", "tag2")));
        // When
        Task savedTask = taskRepository.save(task);

        // Then
        assertThat(savedTask.getId()).isNotNull();
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Test Task");
        assertThat(foundTask.get().getTags()).contains("tag1", "tag2");
    }

    @Test
    public void testFindByCompletedAndPriority() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setCompleted(false);
        task1.setPriority(Priority.HIGH);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setCompleted(true);
        task2.setPriority(Priority.HIGH);

        Task task3 = new Task();
        task3.setTitle("Task 3");
        task3.setCompleted(false);
        task3.setPriority(Priority.LOW);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // When
        List<Task> incompleteHighPriorityTasks = taskRepository.findByCompletedAndPriority(false, Priority.HIGH);

        // Then
        assertThat(incompleteHighPriorityTasks).hasSize(1);
        assertThat(incompleteHighPriorityTasks.get(0).getTitle()).isEqualTo("Task 1");
    }

    @Test
    public void testFindTasksDueWithin7Days() {
        // Given
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusDays(7);
        LocalDate nextMonth = today.plusMonths(1);

        Task task1 = new Task();
        task1.setTitle("Due Tomorrow");
        task1.setCompleted(false);
        task1.setDueDate(tomorrow);

        Task task2 = new Task();
        task2.setTitle("Due Next Week");
        task2.setCompleted(false);
        task2.setDueDate(nextWeek);

        Task task3 = new Task();
        task3.setTitle("Due Next Month");
        task3.setCompleted(false);
        task3.setDueDate(nextMonth);

        Task task4 = new Task();
        task4.setTitle("Completed Task");
        task4.setCompleted(true);
        task4.setDueDate(tomorrow);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);
        taskRepository.save(task4);

        // When
        List<Task> dueTasks = taskRepository.findTasksDueWithin7Days(today, nextWeek.plusDays(1));

        // Then
        assertThat(dueTasks).hasSize(2);
        assertThat(dueTasks.stream().map(Task::getTitle))
                .containsExactlyInAnyOrder("Due Tomorrow", "Due Next Week");
    }
}