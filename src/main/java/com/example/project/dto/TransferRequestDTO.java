package com.example.project.dto;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private String receiverAccountNo;
    private String receiverIfsc;
    private String receiverMobileNumber;
    private String receiverUpiId;
    private double amount;
    private String pin;
    private String message; // Optional
}
