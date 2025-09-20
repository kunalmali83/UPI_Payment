package com.example.project.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.project.dto.ReceiverIdentifierDTO;
import com.example.project.dto.TransferRequestDTO;
import com.example.project.entities.BankAccount;
import com.example.project.entities.Transaction;
import com.example.project.security.CustomUserDetails;
import com.example.project.service.BankAccountService;
import com.example.project.service.TransactionService;
@RestController
@RequestMapping("/api/transfer")
public class MoneyTransferController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionService transactionService;

    // ✅ Step 1: Identify receiver
    @PostMapping("/identify")
    public ResponseEntity<?> identifyReceiver(@RequestBody ReceiverIdentifierDTO dto) {
        BankAccount receiver = null;

        switch (dto.getMethod()) {
            case ACCOUNT_IFSC:
                if (dto.getBankAccount() == null || dto.getIfsc() == null)
                    return ResponseEntity.badRequest().body("Account number and IFSC required");
                receiver = bankAccountService.findByAccountAndIfsc(dto.getBankAccount(), dto.getIfsc());
                break;

            case MOBILE:
                if (dto.getMobile() == null)
                    return ResponseEntity.badRequest().body("Mobile number required");
                receiver = bankAccountService.findByUserMobile(dto.getMobile());
                break;

            case UPI:
                if (dto.getUpiId() == null)
                    return ResponseEntity.badRequest().body("UPI ID required");
                receiver = bankAccountService.findByUpiId(dto.getUpiId());
                break;
        }

        if (receiver == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receiver not found");
        }

        // ✅ Return minimal info to frontend for confirmation
     // ✅ Return all required info to frontend for confirmation
        return ResponseEntity.ok(Map.of(
            "receiverName", receiver.getUser().getName(),
            "receiverAccountNo", receiver.getAccountNumber(),
            "bankName", receiver.getBankName(),
            "receiverMobile", receiver.getUser().getMobileNumber(),
            "receiverIfsc", receiver.getIfsc(),  // needed for Account+IFSC
            "upiId", receiver.getUpiId()         // needed for UPI
        ));

    }

    // ✅ Step 2: Confirm and Transfer
    @PostMapping("/confirm-transfer")
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequestDTO request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object principal = auth.getPrincipal();

            if (!(principal instanceof CustomUserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("Unauthorized or invalid token");
            }

            String loggedInMobile = ((CustomUserDetails) principal).getMobileNumber();

            // 1️⃣ Ensure sender owns the selected account
            if (!bankAccountService.isAccountOwnedByUser(request.getFromAccountNo(), loggedInMobile)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid sender account");
            }

            // 2️⃣ Perform transfer
            Transaction tx = transactionService.transferMoney(loggedInMobile, request);

            return ResponseEntity.ok(tx);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
