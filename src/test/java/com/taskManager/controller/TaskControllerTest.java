package com.taskManager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.taskManager.repository.TaskRepository;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest 
@AutoConfigureMockMvc 
public class TaskControllerTest {

    @Autowired 
    private MockMvc mockMvc;

    @Autowired 
    private TaskRepository taskRepository;

    @Autowired 
    private ObjectMapper objectMapper;

    @BeforeEach 
    void cleanDatabase(){
        taskRepository.deleteAll();
    }

    @Test 
    void getTasksTest_200() throws Exception{
        mockMvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test 
    void getTaskByIdTest_200() throws Exception {
        String body = """
            {
                "title": "test title",
                "description": "test description"
            }
            """;

        String response = mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content(body))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long taskId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/tasks/" + taskId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(taskId))
            .andExpect(jsonPath("$.title").value("test title"))
            .andExpect(jsonPath("$.description").value("test description"));
    }

    @Test 
    void getTaskByIdTest_404() throws Exception {
        mockMvc.perform(get("/api/tasks/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("No task found with id: 99"));
    }

    @Test 
    void postTaskTest_201() throws Exception {
        String body = """
            {
                "title":"test title",
                "description":"test description"
            }
            """;

        mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("test title"))
            .andExpect(jsonPath("$.description").value("test description"));

    }

    @Test
    void putTaskTest_200() throws Exception {
        String body = """
            {
                "title":"updated title",
                "description":"updated description"
            }
            """;

        String response = mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content("""
                    {
                        "title": "test title",
                        "description": "test description"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("test title"))
            .andExpect(jsonPath("$.description").value("test description"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long taskId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(put("/api/tasks/" + taskId)
                    .contentType("application/json")
                    .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("updated title"))
                .andExpect(jsonPath("$.description").value("updated description"));
    }

    
}
