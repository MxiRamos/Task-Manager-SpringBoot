package com.taskManager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record TaskRequest (
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    String title,
    @NotBlank(message = "Description is required")
    @Size(max = 1000)
    String description
){}
