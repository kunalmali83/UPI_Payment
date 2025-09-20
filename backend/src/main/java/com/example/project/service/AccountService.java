package com.example.project.service;
import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.entities.BankAccount;
import com.example.project.repository.BalanceRepo;
import com.example.project.service.CacheService;

@Service
public class AccountService {

    @Autowired
    private BalanceRepo repo;

    @Autowired
    private CacheService cacheService;

    private static final int CACHE_TTL_MINUTES = 10;

    // Get balance with cache
    public BigDecimal getBalance(String accountNumber) {
        String cacheKey = "balance:" + accountNumber;

        // 1️⃣ Check Redis cache
        BigDecimal balance = (BigDecimal) cacheService.getValue(cacheKey);
        if (balance != null) {
            System.out.println("Cache HIT for " + accountNumber + " -> " + balance);
            return balance;
        }

        // 2️⃣ Fetch from DB
        Optional<BankAccount> optional = repo.findByAccountNumber(accountNumber);
        if (optional.isEmpty()) {
            throw new RuntimeException("Account not found: " + accountNumber);
        }

        balance = optional.get().getBalance();

        // 3️⃣ Store in Redis
        cacheService.cacheValue(cacheKey, balance, java.time.Duration.ofMinutes(CACHE_TTL_MINUTES));

        return balance;
    }

    // Update balance (DB + Redis)
    public void updateBalance(String accountNumber, BigDecimal newBalance) {
        Optional<BankAccount> accountOpt = repo.findByAccountNumber(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Account not found: " + accountNumber);
        }

        BankAccount account = accountOpt.get();
        account.setBalance(newBalance);
        repo.save(account);

        // ✅ Update Redis cache immediately
        String cacheKey = "balance:" + accountNumber;
        cacheService.cacheValue(cacheKey, newBalance, java.time.Duration.ofMinutes(CACHE_TTL_MINUTES));
    }
}
