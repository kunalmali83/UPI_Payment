package com.example.project.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;   // ✅ Use JPA annotations
import com.example.project.enums.TransferMethod;

import lombok.Data;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String message;

    // Who sent money
    private String senderUpi;
    
    private String senderMobile;  // ✅ NEW FIELD

    // --- Receiver by 3 methods ---
    private String receiverUpi;
    private String receiverMobile;
    private String receiverAccountNo;
    private String receiverIfsc;

    @Enumerated(EnumType.STRING)
    private TransferMethod method;
}
