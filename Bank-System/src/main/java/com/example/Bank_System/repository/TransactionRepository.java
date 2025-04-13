package com.example.Bank_System.repository;


import com.example.Bank_System.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
}