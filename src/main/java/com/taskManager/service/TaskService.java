package com.taskManager.service;

import java.util.List;

import com.taskManager.dto.TaskRequest;
import com.taskManager.dto.TaskResponse;

public interface TaskService {

    List<TaskResponse> getAllTasks();
    TaskResponse getTaskById(Long id);
    TaskResponse createTask(TaskRequest request);
    TaskResponse updateTask(Long id, TaskRequest request);
    TaskResponse markTaskAsCompleted(Long id);
    void deleteTask(Long id);

}
