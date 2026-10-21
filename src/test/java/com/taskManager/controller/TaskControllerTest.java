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


}
