package com.example.Bank_System.controller;

import com.example.Bank_System.dto.DepositRequest;
import com.example.Bank_System.dto.TransferRequest;
import com.example.Bank_System.dto.WithdrawRequest;
import com.example.Bank_System.entity.Transaction;
import com.example.Bank_System.entity.User;
import com.example.Bank_System.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bank")
@CrossOrigin(origins = "http://127.0.0.1:5500")

public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            bankService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> body) {
        try {
            User user = bankService.login(body.get("username"), body.get("password"));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest depositRequest) {
        try {
            bankService.deposit(depositRequest.getUserId(), depositRequest.getAmount());
            return ResponseEntity.ok("Deposit successful.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deposit failed: " + e.getMessage());
        }
    }


    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest withdrawalRequest) {
        try {
            bankService.withdraw(withdrawalRequest.getUserId(), withdrawalRequest.getAmount());
            return ResponseEntity.ok("Withdrawal successful.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Withdrawal failed: " + e.getMessage());
        }
    }


    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        String result = bankService.transfer(request.getFromUserId(), request.getToUserId(), request.getAmount());

        if (result.equals("Transfer successful")) {
            return ResponseEntity.ok(result);
        } else if (result.equals("Sender not found") || result.equals("Receiver not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.equals("Insufficient funds")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }



    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@RequestParam Long userId) {
        Double balance = bankService.getBalance(userId);

        Map<String, Object> response = new HashMap<>();

        if (balance != null) {
            response.put("balance", balance);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam Long userId) {
        List<Transaction> transactions = bankService.getTransactions(userId);
        if (transactions != null && !transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
