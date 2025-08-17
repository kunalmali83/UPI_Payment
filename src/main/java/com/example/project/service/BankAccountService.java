package com.example.project.service;

import com.example.project.entities.BankAccount;

public interface BankAccountService {
    BankAccount findByAccountAndIfsc(String account, String ifsc);
    BankAccount findByUserMobile(String mobileNumber);
    BankAccount findByUpiId(String upiId);
}
