package com.example.Bank_System.dto;

import lombok.Data;

@Data
public class DepositRequest {
    private Long userId;
    private Double amount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

