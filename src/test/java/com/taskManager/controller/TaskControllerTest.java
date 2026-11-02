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
        String body1 = """
            {
                "title": "test title1",
                "description": "test description1"
            }
            """;

        String body2 = """
            {
                "title": "test title2",
                "description": "test description2"
            }
        """;

        mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content(body1));

        mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content(body2));        

        mockMvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("test title1"))
            .andExpect(jsonPath("$[1].title").value("test title2"));
    }

    @Test 
    void getTasksTestEmpty_200() throws Exception{
        mockMvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
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
    void postEmptyTaskTest_400() throws Exception {
        String body = """
            {
                "title":"",
                "description": ""
            }
            """;
        
        mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.title").value("Title is required"))
            .andExpect(jsonPath("$.error.description").value("Description is required"));
    }

    @Test 
    void postEmptyTitleTest_400() throws Exception {
        String body = """
            {
                "title":"",
                "description": "new Description"
            }
            """;
        
        mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.title").value("Title is required"));
    }

    @Test 
    void postEmptyDescriptionTest_400() throws Exception {
        String body = """
            {
                "title":"new Title",
                "description": ""
            }
            """;
        
        mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.description").value("Description is required"));
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

    @Test 
    void putTaskTest_404() throws Exception{
        String body = """
            {
                "title":"updated title",
                "description":"updated description"
            }
            """;

        mockMvc.perform(put("/api/tasks/99")
                    .contentType("application/json")
                    .content(body))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Task not found with id: 99"));
    }

    @Test 
    void markTaskAsCompleted_200() throws Exception{
        String newBody = """
            {
                "title":"new title",
                "description": "new description"
            }
            """;

        String response = mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content(newBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("new title"))
            .andExpect(jsonPath("$.description").value("new description"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long taskId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(put("/api/tasks/" + taskId + "/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));

    }

    @Test 
    void markTaskAsCompleted_404() throws Exception{
        String body = """
            {
                "title":"new title",
                "description":"new description"
            }
            """;

        mockMvc.perform(put("/api/tasks/99/complete")
                    .contentType("application/json")
                    .content(body))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Task not found with id: 99"));
    }

    @Test 
    void deleteTaskTest_200() throws Exception {
        String newBody = """
            {
                "title":"new title",
                "description": "new description"
            }
            """;
        
        String response = mockMvc.perform(post("/api/tasks")
                .contentType("application/json")
                .content(newBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("new title"))
            .andExpect(jsonPath("$.description").value("new description"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long taskId = objectMapper.readTree(response).get("id").asLong();
        
        mockMvc.perform(delete("/api/tasks/" + taskId))
            .andExpect(status().isOk());

        mockMvc.perform(delete("/api/tasks/" + taskId))
            .andExpect(status().isNotFound());
    }

    @Test 
    void deleteTaskTest_404() throws Exception{
        String body = """
            {
                "title":"new title",
                "description":"new description"
            }
            """;

        mockMvc.perform(delete("/api/tasks/99")
                    .contentType("application/json")
                    .content(body))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Task not found with id: 99"));
    }
}
