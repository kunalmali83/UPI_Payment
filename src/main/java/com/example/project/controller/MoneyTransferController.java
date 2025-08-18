package com.example.project.controller;

import java.util.List;
import java.util.Map;

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

    // Step 1: Identify receiver
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

        return ResponseEntity.ok(Map.of(
            "receiverName", receiver.getUser().getName(),
            "receiverAccount", receiver.getAccountNumber()
        ));
    }

    // Step 2: Confirm and Transfer
 // Step 2: Confirm and Transfer
    @PostMapping("/confirm-transfer")
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequestDTO request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            // ✅ Check if principal is of correct type
            Object principal = auth.getPrincipal();
            if (!(principal instanceof CustomUserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
            }

            String senderAccountNo = ((CustomUserDetails) principal).getAccountNumber();

            // ✅ Pass senderAccountNo and whole request DTO
            transactionService.transferMoney(senderAccountNo, request);



            return ResponseEntity.ok("Transfer successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
