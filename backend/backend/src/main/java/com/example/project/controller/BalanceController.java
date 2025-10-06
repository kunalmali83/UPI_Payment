package com.example.project.controller;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.project.dto.BalanceResponse;
import com.example.project.dto.BankAccountResp;
import com.example.project.entities.BankAccount;
import com.example.project.entities.User;
import com.example.project.service.AccountService;
import com.example.project.service.BankAccountService;
import com.example.project.service.UserService;
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
    @Autowired
    private BankAccountService bankAccountService;
    

    @Autowired
    private UserService userService;

    // ✅ Check balance endpoint
    @PostMapping("/checkBalance")
    public ResponseEntity<?> checkBalance(@RequestBody Map<String, String> payload) {

        String accountNumber = payload.get("accountNumber");  // ✅ use from payload
        String enteredPin = payload.get("pin");

        BankAccount account = repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!passwordEncoder.matches(enteredPin, account.getPin())) {
            return ResponseEntity.status(403).body("❌ Incorrect PIN");
        }

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
