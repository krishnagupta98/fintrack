package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(nullable = false)
        private String username;

    @Column(nullable = false)
    private String password;

        private String user_role;


        public User(){

        }
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.user_role = role;
    }
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean equal(Object o){
            if (this == o) return true;
            if ( o == null || getClass() != o.getClass()  ) return false;
            User user = (User) o;
            return id == user.id && Objects.equals(username , user.username);
    }
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
