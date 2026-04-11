package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.security.access.method.P;

public class ExpenseRequest {

    @NotBlank(message = "title cannot be blank ")
    private String title;

    @Positive(message = "number should be always positive")
    private int amount;

    @NotBlank(message = "category cannot be empty")
    private String category;

    @NotBlank (message = "type cannot be blank")
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
