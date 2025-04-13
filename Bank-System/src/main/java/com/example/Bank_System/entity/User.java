package com.example.Bank_System.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private Double balance = 0.0;

   // private String role;

    public void setBalance(Double balance) {
        this.balance = balance;
    }
//
//    public String getRole() {
//        return role;
//    }

//    public void setRole(String role) {
//        this.role = role;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


//    public User orElseThrow(Object userNotFound) {
//        return orElseThrow(userNotFound);
//    }
}
