package com.example.demo.dto;

public record NotificationRequest(
        String userId,
        String message,
        int priority
) {}