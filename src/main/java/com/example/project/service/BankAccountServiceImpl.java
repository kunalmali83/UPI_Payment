package com.example.project.service;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.project.entities.BankAccount;
import com.example.project.repository.BankAccFindRepo;




@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccFindRepo repo;

    @Override
    public BankAccount findByAccountAndIfsc(String account, String ifsc) {
        return repo.findByAccountNumberAndIfsc(account, ifsc)
                   .orElseThrow(() -> new RuntimeException("Account not found with account + IFSC"));
    }

    @Override
    public BankAccount findByUserMobile(String mobileNumber) {
        System.out.println("Looking for mobile number: " + mobileNumber);
        List<BankAccount> accounts = repo.findFirstByUser_MobileNumber(mobileNumber);
        if (accounts.isEmpty()) {
            throw new RuntimeException("No account found for mobile number: " + mobileNumber);
        }
        // Choose first or apply your own logic
        BankAccount account = accounts.get(0);
        System.out.println("Found account: " + account.getAccountNumber());
        return account;
    }


    @Override
    public BankAccount findByUpiId(String upiId) {
        return repo.findByUpiId(upiId)
                   .orElseThrow(() -> new RuntimeException("Account not found for UPI ID"));
    }
   
}
