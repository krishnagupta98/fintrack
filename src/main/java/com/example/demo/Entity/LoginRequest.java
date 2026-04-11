package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
public class LoginRequest {
     @Id
     private String username;
     private String password;

     LoginRequest(){}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
