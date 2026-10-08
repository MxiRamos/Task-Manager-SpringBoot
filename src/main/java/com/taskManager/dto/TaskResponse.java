package com.taskManager.dto;

public record TaskResponse (
    Long id,
    String title,
    String description,
    boolean completed,
    String createdAt
){}
