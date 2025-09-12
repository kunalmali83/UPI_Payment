package com.example.project.dto;

import com.example.project.enums.TransferMethod;
import lombok.Data;

@Data
public class TransferRequestDTO {
    private String fromAccountNo;     // Sender’s chosen account
    private double amount;
    private String pin;
    private String message;           // optional

    // Receiver identification fields
    private String receiverMobile;    // for MOBILE method
    private String receiverAccountNo; // for ACCOUNT_IFSC method
    private String receiverIfsc;      // for ACCOUNT_IFSC method
    private String upiId;             // for UPI method

    private TransferMethod method;    // ACCOUNT_IFSC, MOBILE, UPI
}
