package com.example.demo.Entity;
import jakarta.annotation.Generated;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")

public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int amount;
    private String category;
    private String type;


    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;


    @Column(nullable = true,updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime dateTime;

    public Expense() {
    }

    public Expense(Long id, LocalDateTime dateTime, String type, String category, int amount, String title, User user) {
        this.id = id;
        this.dateTime = dateTime;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.title = title;
        this.user=user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}