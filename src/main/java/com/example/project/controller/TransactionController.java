package com.example.project.controller;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.ChatOverviewDTO;
import com.example.project.entities.Transaction;
import com.example.project.security.CustomUserDetails;
import com.example.project.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/chat-history/{mobile}")
    public ResponseEntity<?> getChatHistory(@PathVariable String mobile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String loggedInMobile = ((CustomUserDetails) auth.getPrincipal()).getMobileNumber();
        if (loggedInMobile == null || loggedInMobile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid logged-in mobile number");
        }

        List<Transaction> history = transactionService.getChatHistory(loggedInMobile, mobile);
        return ResponseEntity.ok(history);
    }
    @GetMapping("/all-chats")
    public ResponseEntity<?> getAllChats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String loggedInMobile = ((CustomUserDetails) principal).getMobileNumber();

        List<ChatOverviewDTO> chats = transactionService.getAllChatsForUser(loggedInMobile);
        return ResponseEntity.ok(chats);
    }
    @PostMapping("/send")
    public ResponseEntity<?> sendTransaction(@RequestBody Transaction transaction) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String senderMobile = ((CustomUserDetails) auth.getPrincipal()).getMobileNumber();
        transaction.setSenderMobile(senderMobile);
        transaction.setTimestamp(LocalDateTime.now());

        // Save using service
        Transaction saved = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(saved);
    }



}
