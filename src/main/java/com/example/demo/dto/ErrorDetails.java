package com.example.demo.dto;

import java.time.LocalDateTime;

public class ErrorDetails {
     private LocalDateTime time;
     private String message;
     private String details;


    public ErrorDetails( String message, String details) {
        this.time = LocalDateTime.now();
        this.details = details;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
