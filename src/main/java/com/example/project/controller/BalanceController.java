package com.example.project.controller;
import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.project.dto.BalanceResponse;
import com.example.project.entities.BankAccount;
import com.example.project.service.AccountService;
import com.example.project.repository.BalanceRepo;

@RestController
@RequestMapping("/api/account")
public class BalanceController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BalanceRepo repo;

    // ✅ Check balance endpoint
    @PostMapping("/checkBalance")
    public ResponseEntity<?> checkBalance(
            Authentication auth,
            @RequestBody Map<String, String> payload) {

        String accountNumber = auth.getName();
        String enteredPin = payload.get("pin");

        // Verify PIN
        BankAccount account = repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!passwordEncoder.matches(enteredPin, account.getPin())) {
            return ResponseEntity.status(403).body("❌ Incorrect PIN");
        }

        // Get balance via service (with Redis caching)
        BigDecimal balance = accountService.getBalance(accountNumber);

        return ResponseEntity.ok(new BalanceResponse(accountNumber, balance));
    }

    // ✅ Optional: Endpoint to update balance
    @PostMapping("/updateBalance")
    public ResponseEntity<?> updateBalance(
            @RequestBody Map<String, String> payload) {

        String accountNumber = payload.get("accountNumber");
        BigDecimal newBalance = new BigDecimal(payload.get("newBalance"));

        accountService.updateBalance(accountNumber, newBalance);
        return ResponseEntity.ok("Balance updated successfully");
    }
}
