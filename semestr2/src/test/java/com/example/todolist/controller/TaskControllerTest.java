package com.example.todolist.controller;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.model.Priority;
import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TaskController endpoints.
 * Tests controller behavior using MockMvc and mocked services.
 */
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private com.example.todolist.service.TaskService taskService;

    @MockBean
    private com.example.todolist.mapper.TaskMapper taskMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllTasks_Success() throws Exception {
        Task task1 = new Task(1L, "Task 1", "Description 1", false);
        Task task2 = new Task(2L, "Task 2", "Description 2", true);
        
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    public void testGetTaskById_Found() throws Exception {
        Task task = new Task(1L, "Task 1", "Description 1", false);
        
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());
        
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    public void testCreateTask_Success() throws Exception {
        String createJson = "{\"title\": \"New Task\", \"description\": \"Description\", \"priority\": \"MEDIUM\"}";
        Task savedTask = new Task(1L, "New Task", "Description", false);
        
        when(taskService.createTask(any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk());
        
        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    public void testUpdateTask_Found() throws Exception {
        String updateJson = "{\"title\": \"Updated Task\", \"completed\": true}";
        Task existingTask = new Task(1L, "Old Task", "Description", false);
        Task updatedTask = new Task(1L, "Updated Task", "Description", true);
        
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(existingTask));
        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(Optional.of(updatedTask));

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk());
        
        verify(taskService, times(1)).getTaskById(1L);
        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    public void testUpdateTask_NotFound() throws Exception {
        String updateJson = "{\"title\": \"Updated Task\"}";
        
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
        
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    public void testDeleteTask_Found() throws Exception {
        Task task = new Task(1L, "Task", "Desc", false);
        
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
        when(taskService.deleteTask(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
        
        verify(taskService, times(1)).getTaskById(1L);
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    public void testDeleteTask_NotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound());
        
        verify(taskService, times(1)).getTaskById(1L);
    }
}