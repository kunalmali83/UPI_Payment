package com.example.project.security;

import com.example.project.entities.BankAccount;
import com.example.project.entities.User;
import com.example.project.repository.BalanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BalanceRepo balanceRepo; // ✅ looks in bank_accounts

    @Override
    public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
        BankAccount bankAccount = balanceRepo.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new UsernameNotFoundException("Account not found with account number: " + accountNumber));

        User user = bankAccount.getUser(); // 🔁 This links bank account to user
        System.out.println("🔐 Found user: " + user.getEmail() + " for account number: " + accountNumber);

        return new CustomUserDetails(user, accountNumber);

    }
}
