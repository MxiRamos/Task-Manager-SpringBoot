package com.taskManager.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.taskManager.dto.TaskRequest;
import com.taskManager.dto.TaskResponse;

public interface TaskService {

    @Transactional(readOnly = true)
    List<TaskResponse> getAllTasks();
    @Transactional(readOnly = true)
    TaskResponse getTaskById(Long id);
    @Transactional
    TaskResponse createTask(TaskRequest request);
    @Transactional
    TaskResponse updateTask(Long id, TaskRequest request);
    @Transactional
    TaskResponse markTaskAsCompleted(Long id);
    @Transactional
    void deleteTask(Long id);

}
