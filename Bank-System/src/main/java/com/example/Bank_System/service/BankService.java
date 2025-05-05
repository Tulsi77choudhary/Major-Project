package com.example.Bank_System.service;

//import com.example.Bank_System.dto.CreateAccountRequest;
//import com.example.Bank_System.entity.Account;
import com.example.Bank_System.entity.Transaction;
import com.example.Bank_System.entity.User;
//import com.example.Bank_System.repository.AccountRepository;
import com.example.Bank_System.repository.TransactionRepository;
import com.example.Bank_System.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    public String register(User user) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            return "Username already exists";
        }
        userRepo.save(user);
        return "Registration successful";
    }

    public User login(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public String deposit(Long userId, Double amount) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null && amount > 0) {
            user.setBalance(user.getBalance() + amount);
            userRepo.save(user);
            saveTransaction(userId, "deposit", amount);
            return "Deposit successful";
        }
        return "Failed to deposit";
    }

    public String withdraw(Long userId, Double amount) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null && user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            userRepo.save(user);
            saveTransaction(userId, "withdraw", amount);
            return "Withdrawal successful";
        }
        return "Insufficient balance";
    }

    @Transactional
    public String transfer(Long fromUserId, Long toUserId, Double amount) {
        User fromUser = userRepo.findById(fromUserId).orElse(null);
        User toUser = userRepo.findById(toUserId).orElse(null);

        if (fromUser == null) {
            return "Sender not found";
        }

        if (toUser == null) {
            return "Receiver not found";
        }

        if (fromUser.getBalance() < amount) {
            return "Insufficient funds";
        }

        try {
            fromUser.setBalance(fromUser.getBalance() - amount);
            toUser.setBalance(toUser.getBalance() + amount);

            userRepo.save(fromUser);
            userRepo.save(toUser);

            // Saving the transaction details (optional enhancement)
            saveTransaction(fromUserId, "transfer", amount);

            return "Transfer successful";

        } catch (Exception e) {
            // Log the error message if necessary
            return "Transfer failed due to an error: " + e.getMessage();
        }
    }

            public Double getBalance(Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        return userOpt.map(User::getBalance).orElse(null);
    }


    public List<Transaction> getTransactions(Long userId) {
        return transactionRepo.findByUserId(userId);
    }

    private void saveTransaction(Long userId, String type, Double amount) {
        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        transactionRepo.save(tx);
    }


//    public Account createAccount(CreateAccountRequest request) {
//        User user = userRepo.findById(request.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Account account = new Account();
//        account.setAccountNumber(generateAccountNumber());
//        account.setBalance(request.getInitialBalance() != null ? request.getInitialBalance() : 0.0);
//
//        return accountRepository.save(account);
//    }
//
//    private String generateAccountNumber() {
//        return "ACCT-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 10000);
//    }
}
