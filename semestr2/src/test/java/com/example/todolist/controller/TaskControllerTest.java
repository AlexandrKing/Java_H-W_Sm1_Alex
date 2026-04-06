package com.example.todolist.controller;

import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.dto.TaskResponseDto;
import com.example.todolist.dto.TaskUpdateDto;
import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.mapper.TaskMapper;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TaskController with DTO support.
 */
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMapper taskMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskResponseDto createResponseDto(Long id, String title, String description, boolean completed) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setCompleted(completed);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Task task1 = new Task(1L, "Task 1", "Description 1", false);
        Task task2 = new Task(2L, "Task 2", "Description 2", true);
        
        TaskResponseDto dto1 = createResponseDto(1L, "Task 1", "Description 1", false);
        TaskResponseDto dto2 = createResponseDto(2L, "Task 2", "Description 2", true);
        
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));
        when(taskMapper.toResponseDtoList(Arrays.asList(task1, task2)))
                .thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    public void testGetTaskById_Found() throws Exception {
        Task task = new Task(1L, "Task 1", "Description 1", false);
        TaskResponseDto dto = createResponseDto(1L, "Task 1", "Description 1", false);
        
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponseDto(task)).thenReturn(dto);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void testGetTaskById_NotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTask_Success() throws Exception {
        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setTitle("New Task");
        createDto.setDescription("New Description");
        createDto.setPriority(Priority.MEDIUM);

        Task task = new Task(null, "New Task", "New Description", false);
        Task savedTask = new Task(1L, "New Task", "New Description", false);
        TaskResponseDto responseDto = createResponseDto(1L, "New Task", "New Description", false);
        
        when(taskMapper.toEntity(createDto)).thenReturn(task);
        when(taskService.createTask(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toResponseDto(savedTask)).thenReturn(responseDto);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    public void testUpdateTask_Found() throws Exception {
        TaskUpdateDto updateDto = new TaskUpdateDto();
        updateDto.setTitle("Updated Task");
        updateDto.setCompleted(true);

        Task existingTask = new Task(1L, "Old Task", "Description", false);
        Task updatedTask = new Task(1L, "Updated Task", "Description", true);
        TaskResponseDto responseDto = createResponseDto(1L, "Updated Task", "Description", true);
        
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(existingTask));
        when(taskMapper.updateEntity(updateDto, existingTask)).thenReturn(updatedTask);
        when(taskService.updateTask(eq(1L), eq(updatedTask))).thenReturn(Optional.of(updatedTask));
        when(taskMapper.toResponseDto(updatedTask)).thenReturn(responseDto);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    public void testUpdateTask_NotFound() throws Exception {
        TaskUpdateDto updateDto = new TaskUpdateDto();
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTask_Found() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(new Task(1L, "Task", "Desc", false)));
        when(taskService.deleteTask(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteTask_NotFound() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound());
    }
}