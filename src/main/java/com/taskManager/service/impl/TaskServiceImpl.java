package com.taskManager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taskManager.dto.TaskRequest;
import com.taskManager.dto.TaskResponse;
import com.taskManager.exception.ResourceNotFoundException;
import com.taskManager.model.Task;
import com.taskManager.repository.TaskRepository;
import com.taskManager.service.TaskService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    // Implement the methods defined in the TaskService interface
    private final TaskRepository taskRepository;

    
    @Override
    public List<TaskResponse> getAllTasks() {
        // Implement logic to retrieve all tasks and convert them to TaskResponse
        return taskRepository.findAll().stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted(),
                        task.getCreatedAt().toString()))
                .toList();
        // throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        // Implement logic to retrieve a single task and convert it to TaskResponse
        return taskRepository.findById(id)
            .map(task -> new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt().toString()))
                .orElseThrow(() -> new ResourceNotFoundException("No task found"));
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {
        // Implement logic to create a new task and convert it to TaskResponse
        Task task = new Task(
            request.title(),
            request.description()
        );
        Task savedTask = taskRepository.save(task);
        return new TaskResponse(
            savedTask.getId(),
            savedTask.getTitle(),
            savedTask.getDescription(),
            savedTask.isCompleted(),
            savedTask.getCreatedAt().toString()
        );
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        // Implement logic to update an existing task and convert it to TaskResponse
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setTitle(request.title());
        task.setDescription(request.description());
        Task updatedTask = taskRepository.save(task);

        return new TaskResponse(
            updatedTask.getId(),
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.isCompleted(),
            updatedTask.getCreatedAt().toString()
        );
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public TaskResponse markTaskAsCompleted(Long id) {
        // Implement logic to mark a task as completed and convert it to 
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);

        return new TaskResponse(
            updatedTask.getId(),
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.isCompleted(),
            updatedTask.getCreatedAt().toString()
        );
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteTask(Long id) {
        // Implement logic to delete a task
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        taskRepository.delete(task);
        //throw new UnsupportedOperationException("Not implemented yet");
    }
}
