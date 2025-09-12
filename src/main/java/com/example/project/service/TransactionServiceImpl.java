package com.example.project.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.dto.ChatOverviewDTO;
import com.example.project.dto.TransferRequestDTO;
import com.example.project.entities.BankAccount;
import com.example.project.entities.Transaction;
import com.example.project.entities.User;
import com.example.project.enums.TransferMethod;
import com.example.project.repository.BankAccFindRepo;
import com.example.project.repository.TransactionRepository;
import com.example.project.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private BankAccFindRepo repo;

    @Autowired
    private TransactionRepository txnRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    /**
     * Transfer money from a sender's selected account to receiver's primary account.
     * @param senderMobile - mobile number of the logged-in user
     * @param request - DTO containing fromAccountNo, amount, PIN, and message
     */
    @Override
    @Transactional
    public Transaction transferMoney(String senderMobile, TransferRequestDTO request) {

        // 1️⃣ Find sender account and validate it belongs to this user
        BankAccount sender = repo.findByAccountNumber(request.getFromAccountNo())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        if (!sender.getUser().getMobileNumber().equals(senderMobile)) {
            throw new RuntimeException("Sender account does not belong to logged-in user");
        }

        // 2️⃣ Validate PIN
        if (!passwordEncoder.matches(request.getPin(), sender.getPin())) {
            throw new RuntimeException("Invalid PIN");
        }

        // 3️⃣ Find receiver’s **primary account**
        User receiverUser = userRepo.findByMobileNumber(request.getReceiverMobile())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        BankAccount receiver = receiverUser.getAccounts().stream()
                .filter(BankAccount::isPrimary)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Receiver does not have a primary account"));

        // 4️⃣ Validate funds
        BigDecimal amount = BigDecimal.valueOf(request.getAmount());
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // 5️⃣ Update balances
        BigDecimal senderNewBalance = sender.getBalance().subtract(amount);
        BigDecimal receiverNewBalance = receiver.getBalance().add(amount);

        accountService.updateBalance(sender.getAccountNumber(), senderNewBalance);
        accountService.updateBalance(receiver.getAccountNumber(), receiverNewBalance);

        // 6️⃣ Create and save transaction
        Transaction txn = new Transaction();
        txn.setAmount(amount);
        txn.setTimestamp(LocalDateTime.now());
        txn.setMessage(request.getMessage() != null ? request.getMessage() :
                "Transfer from " + sender.getUser().getName() + " to " + receiver.getUser().getName());

        txn.setSenderMobile(sender.getUser().getMobileNumber());
        txn.setSenderUpi(sender.getUpiId());
        txn.setReceiverMobile(receiver.getUser().getMobileNumber());
        txn.setReceiverUpi(receiver.getUpiId());

        txn.setReceiverAccountNo(receiver.getAccountNumber());
        txn.setReceiverIfsc(receiver.getIfsc());
        txn.setMethod(TransferMethod.ACCOUNT_IFSC); // or MOBILE/UPI based on flow

        return txnRepo.save(txn);
    }

    @Override
    public List<Transaction> getChatHistory(String loggedInMobile, String otherMobile) {
        String sender = loggedInMobile.replaceAll("\\D", "");
        String receiver = otherMobile.replaceAll("\\D", "");
        return txnRepo.findChatHistory(sender, receiver);
    }

    @Override
    public List<ChatOverviewDTO> getAllChatsForUser(String mobile) {
        List<Object[]> rawResults = txnRepo.findAllChatsRaw(mobile);
        return rawResults.stream().map(r ->
                new ChatOverviewDTO(
                        (String) r[0],
                        (String) r[1],
                        r[2] != null ? ((java.sql.Timestamp) r[2]).toLocalDateTime() : null,
                        (BigDecimal) r[3]
                )
        ).toList();
    }

    public Transaction saveTransaction(Transaction transaction) {
        return txnRepo.save(transaction);
    }
}
