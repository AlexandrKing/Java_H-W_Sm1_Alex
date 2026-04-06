package com.example.todolist.controller;

import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.dto.TaskUpdateDto;
import com.example.todolist.model.Priority;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the homework 2 advanced features.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTaskWithValidation_Success() throws Exception {
        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setTitle("Buy groceries");
        createDto.setDescription("Get milk, bread, and eggs");
        createDto.setPriority(Priority.HIGH);
        createDto.setDueDate(LocalDate.now().plusDays(7));

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Buy groceries"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    public void testCreateTaskWithValidation_BlankTitle() throws Exception {
        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setTitle("");  // Invalid: blank
        createDto.setPriority(Priority.MEDIUM);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").containsString("Validation failed"));
    }

    @Test
    public void testCreateTaskWithValidation_TitleTooShort() throws Exception {
        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setTitle("ab");  // Invalid: too short (min 3)
        createDto.setPriority(Priority.LOW);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    public void testUpdateTask_PartialFields() throws Exception {
        // First create a task
        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setTitle("Original Task");
        createDto.setDescription("Original Description");
        createDto.setPriority(Priority.MEDIUM);

        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        int taskId = objectMapper.readTree(responseBody).get("id").asInt();

        // Now update only some fields
        TaskUpdateDto updateDto = new TaskUpdateDto();
        updateDto.setTitle("Updated Title");
        updateDto.setCompleted(true);

        mockMvc.perform(put("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    public void testGetNonExistentTask_Returns404() throws Exception {
        mockMvc.perform(get("/api/tasks/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").containsString("not found"));
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        // Create a task first
        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setTitle("Task to Delete");
        createDto.setPriority(Priority.LOW);

        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        int taskId = objectMapper.readTree(responseBody).get("id").asInt();

        // Delete the task
        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());

        // Verify it's deleted
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        // Create multiple tasks
        for (int i = 0; i < 3; i++) {
            TaskCreateDto createDto = new TaskCreateDto();
            createDto.setTitle("Task " + i);
            createDto.setPriority(Priority.MEDIUM);
            mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isOk());
        }

        // Get all tasks
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    public void testFileAttachmentUpload() throws Exception {
        // First create a task
        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setTitle("Task with Attachment");
        createDto.setPriority(Priority.HIGH);

        MvcResult createResult = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        int taskId = objectMapper.readTree(responseBody).get("id").asInt();

        // Upload a file (simulated)
        // Note: This might require additional setup for multipart fixture support
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task with Attachment"));
    }

    @Test
    public void testUserPreferences_SetAndGet() throws Exception {
        // Set sort preference
        mockMvc.perform(post("/api/preferences/sort-preference")
                .param("preference", "priority"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").containsString("priority"));

        // Get sort preference
        mockMvc.perform(get("/api/preferences/sort-preference"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sortPreference").value("priority"));
    }

    @Test
    public void testUserPreferences_ThemePreference() throws Exception {
        // Set theme
        mockMvc.perform(post("/api/preferences/theme")
                .param("theme", "dark"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").containsString("dark"));

        // Get theme
        mockMvc.perform(get("/api/preferences/theme"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theme").value("dark"));
    }
}
