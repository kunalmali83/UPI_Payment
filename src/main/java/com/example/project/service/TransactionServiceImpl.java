package com.example.project.service;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.dto.TransferRequestDTO;
import com.example.project.entities.BankAccount;
import com.example.project.entities.Transaction;
import com.example.project.enums.TransferMethod;
import com.example.project.repository.BankAccFindRepo;
import com.example.project.repository.TransactionRepository;

import jakarta.transaction.Transactional;


@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private BankAccFindRepo repo;

    @Autowired
    private TransactionRepository txnRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public void transferMoney(String senderAccountNumber, TransferRequestDTO request) {

        // Find sender
        BankAccount sender = repo.findById(senderAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // Validate PIN
        if (!passwordEncoder.matches(request.getPin(), sender.getPin())) {
            throw new RuntimeException("Invalid PIN");
        }

        // Determine receiver
        BankAccount receiver;
        TransferMethod method;

        if (request.getReceiverAccountNo() != null && !request.getReceiverAccountNo().isEmpty()) {
            if (request.getReceiverIfsc() != null && !request.getReceiverIfsc().isEmpty()) {
                // Find by account + IFSC
                receiver = repo.findByAccountNumberAndIfsc(
                        request.getReceiverAccountNo(),
                        request.getReceiverIfsc()
                ).orElseThrow(() -> new RuntimeException("Receiver not found by account + IFSC"));
            } else {
                // Find by account only
                receiver = repo.findById(request.getReceiverAccountNo())
                        .orElseThrow(() -> new RuntimeException("Receiver not found by account number"));
            }
            method = TransferMethod.ACCOUNT_IFSC; // You can rename to ACCOUNT if preferred

        } else if (request.getReceiverMobileNumber() != null && !request.getReceiverMobileNumber().isEmpty()) {
            // Find by mobile number
            List<BankAccount> accounts = repo.findFirstByUser_MobileNumber(request.getReceiverMobileNumber());
            if (accounts.isEmpty()) {
                throw new RuntimeException("Receiver not found by mobile number");
            }
            receiver = accounts.get(0); // pick first account
            method = TransferMethod.MOBILE;

        } else if (request.getReceiverUpiId() != null && !request.getReceiverUpiId().isEmpty()) {
            // Find by UPI ID
            receiver = repo.findByUpiId(request.getReceiverUpiId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found by UPI ID"));
            method = TransferMethod.UPI;

        } else {
            throw new RuntimeException("No valid receiver information provided");
        }

        // Validate funds
        BigDecimal amountToTransfer = BigDecimal.valueOf(request.getAmount());
        if (sender.getBalance().compareTo(amountToTransfer) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Perform transfer
        sender.setBalance(sender.getBalance().subtract(amountToTransfer));
        receiver.setBalance(receiver.getBalance().add(amountToTransfer));

        repo.save(sender);
        repo.save(receiver);

        // Save transaction record
        Transaction txn = new Transaction();
        txn.setAmount(amountToTransfer);
        txn.setTimestamp(LocalDateTime.now());
        txn.setMessage(request.getMessage() != null ? request.getMessage() :
                "Transfer from " + sender.getUser().getName() + " to " + receiver.getUser().getName());
        txn.setSenderUpi(sender.getUpiId());
        txn.setReceiverUpi(receiver.getUpiId());
        txn.setReceiverMobile(receiver.getUser().getMobileNumber());
        txn.setReceiverAccountNo(receiver.getAccountNumber());
        txn.setReceiverIfsc(receiver.getIfsc());
        txn.setMethod(method);

        txnRepo.save(txn);
    }

}


