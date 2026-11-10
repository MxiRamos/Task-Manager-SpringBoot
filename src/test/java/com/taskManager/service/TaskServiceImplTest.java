package com.taskManager.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskManager.dto.TaskRequest;
import com.taskManager.dto.TaskResponse;
import com.taskManager.exception.ResourceNotFoundException;
import com.taskManager.model.Task;
import com.taskManager.repository.TaskRepository;
import com.taskManager.service.impl.TaskServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock 
    private TaskRepository taskRepository;

    @InjectMocks 
    private TaskServiceImpl taskService;

    //Get Task Test

    @Test 
    void getAllTasksTest(){
        List<Task> tasks = List.of(
                Task.builder()
                .id(0L)
                .title("Title task Test1")
                .description("Description task Test1")
                .build(),

                Task.builder()
                .id(1L)
                .title("Title task Test2")
                .description("Description task Test2")
                .build()
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskResponse> taskResponses = taskService.getAllTasks();

        assertThat(taskResponses).hasSize(2);

        assertThat(taskResponses.get(0).id()).isEqualTo(0L);
        assertThat(taskResponses.get(0).title()).isEqualTo("Title task Test1");

        assertThat(taskResponses.get(1).id()).isEqualTo(1L);
        assertThat(taskResponses.get(1).title()).isEqualTo("Title task Test2");

        verify(taskRepository).findAll();
    }

    @Test 
    void getAllTaskEmptyTest(){
        when(taskRepository.findAll()).thenReturn(List.of());

        List<TaskResponse> taskResponses = taskService.getAllTasks();

        assertThat(taskResponses).isEmpty();

        verify(taskRepository).findAll();
    }

    @Test 
    void getTaskTest(){
        Task task = Task.builder()
                .id(1L)
                .title("Title task Test")
                .description("Description task Test")
                .build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse taskResponse = taskService.getTaskById(1L);
        
        assertThat(taskResponse.id()).isEqualTo(1L);
        assertThat(taskResponse.title()).isEqualTo("Title task Test");
        assertThat(taskResponse.description()).isEqualTo("Description task Test");
        verify(taskRepository).findById(1L);
    }

    @Test 
    void getTaskNotFoundTest(){
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No task found with id: 99");
    }

    //Create Task Test
    @Test
    void createTaskTest(){
        TaskRequest taskRequest = new TaskRequest(
                "New Task Test",
                "New description Test"
            );

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
        Task savedTask = invocation.getArgument(0);
        savedTask.setId(1L);
        return savedTask;
        });

        TaskResponse taskResponse = taskService.createTask(taskRequest);

        assertThat(taskResponse.id()).isEqualTo(1L);
        assertThat(taskResponse.title()).isEqualTo("New Task Test");
        assertThat(taskResponse.description()).isEqualTo("New description Test");

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        assertThat(captor.getValue().getTitle()).isEqualTo("New Task Test");
        assertThat(captor.getValue().getDescription())
                .isEqualTo("New description Test");
    }


    //Update Task Test
    @Test 
    void updateTaskTest(){
        TaskRequest request = new TaskRequest(
                "Updated Task Test",
                "Updated description Test"
        );

        Task existingTask = Task.builder()
                .id(1L)
                .title("Old Task Test")
                .description("Old Description Test")
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        when(taskRepository.save(any(Task.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.updateTask(1L, request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Updated Task Test");
        assertThat(response.description()).isEqualTo("Updated description Test");

        assertThat(existingTask.getTitle()).isEqualTo("Updated Task Test");
        assertThat(existingTask.getDescription())
                .isEqualTo("Updated description Test");

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existingTask);
    }

    @Test 
    void updateTaskNotFoundTest(){
        TaskRequest taskRequest = new TaskRequest(
                "Updated Task Test",
                "Updated description Test"
        );

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(99L, taskRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found with id: 99");
    }

    @Test 
    void markTaskAsCompletedTest(){

        Task existingTask = Task.builder()
                .id(1L)
                .title("Task to complete")
                .description("Description task to complete")
                .completed(false)
                .build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse taskResponse = taskService.markTaskAsCompleted(1L);

        assertThat(taskResponse.id()).isEqualTo(1L);
        assertThat(taskResponse.title()).isEqualTo("Task to complete");
        assertThat(taskResponse.description()).isEqualTo("Description task to complete");
        assertThat(taskResponse.completed()).isTrue();
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test 
    void markTaskAsCompletedNotFoundTest(){
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.markTaskAsCompleted(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found with id: 99");
    }

    //Delete Task Test
    @Test 
    void deleteTaskTest(){
        Task existingTask = Task.builder()
                .id(1L)
                .title("Task to delete")
                .description("Description task to delete")
                .build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        taskService.deleteTask(1L);

        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(existingTask);
    }

    @Test 
    void deleteTaskNotFoundTest(){
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task not found with id: 99");

        verify(taskRepository).findById(99L);
        verify(taskRepository, never()).delete(any(Task.class));
    }
}
