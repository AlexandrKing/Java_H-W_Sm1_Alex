package com.example.todolist.controller;

import com.example.todolist.service.TaskService;
import com.example.todolist.service.TaskStatisticsJdbcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Simple integration tests for TaskController.
 * Focuses on endpoint availability and basic response codes.
 */
@SpringBootTest
public class TaskControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskStatisticsJdbcService statisticsService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllTasks_ReturnsOk() throws Exception {
        when(taskService.getAllTasks()).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTaskById_Found() throws Exception {
        Task task = new Task(1L, "Test Task", "Description", false);
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTask_ReturnsOk() throws Exception {
        Task savedTask = new Task(1L, "New Task", "Description", false);
        when(taskService.createTask(any(Task.class))).thenReturn(savedTask);

        String json = "{\"title\": \"New Task\", \"description\": \"Description\", \"priority\": \"HIGH\"}";
        
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    public void testUpdateTask_Found() throws Exception {
        Task existingTask = new Task(1L, "Old Task", "Description", false);
        Task updatedTask = new Task(1L, "Updated Task", "Description", true);
        
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(existingTask));
        when(taskService.updateTask(any(Long.class), any(Task.class))).thenReturn(Optional.of(updatedTask));

        String json = "{\"title\": \"Updated Task\", \"completed\": true}";
        
        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateTask_NotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        String json = "{\"title\": \"Updated Task\"}";
        
        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTask_Found() throws Exception {
        Task task = new Task(1L, "Task", "Description", false);
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
        when(taskService.deleteTask(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    public void testDeleteTask_NotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }
}
