package com.example.project.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.BalanceResponse;
import com.example.project.entities.BankAccount;
import com.example.project.repository.BalanceRepo;

@RestController
@RequestMapping("/api/account")
public class BalanceController {

    @Autowired
    private BalanceRepo repo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/checkBalance")
    public ResponseEntity<?> checkBalance(
            Authentication auth,
            @RequestBody Map<String, String> payload) {

        String accountNumber = auth.getName(); // From JWT
        String enteredPin = payload.get("pin");

        Optional<BankAccount> optional = repo.findByAccountNumber(accountNumber);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not found.");
        }

        BankAccount account = optional.get();
        if (!passwordEncoder.matches(enteredPin, account.getPin())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ Incorrect PIN");
        }

        BalanceResponse response = new BalanceResponse(account.getAccountNumber(), account.getBalance());
        return ResponseEntity.ok(response);
    }


}
