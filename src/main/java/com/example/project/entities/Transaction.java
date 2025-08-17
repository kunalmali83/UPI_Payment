package com.example.project.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.example.project.enums.TransferMethod;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Common for all methods
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String message;

    // Who sent money
    private String senderUpi;

    // --- Receiver by 3 methods ---
    
    // UPI-based
    private String receiverUpi;

    // Mobile-based
    private String receiverMobile;

    // Account+IFSC based
    private String receiverAccountNo;
    private String receiverIfsc;

    @Enumerated(EnumType.STRING)
    private TransferMethod method;  // UPI, MOBILE, ACCOUNT_IFSC
}
