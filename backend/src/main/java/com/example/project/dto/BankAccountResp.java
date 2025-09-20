package com.example.project.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BankAccountResp {
    private String accountNumber;  // optionally mask partially
    private String accountHolder;
    private String bankName;
    private String upiId;
    private BigDecimal balance;   // optional
    private boolean primary;
}
