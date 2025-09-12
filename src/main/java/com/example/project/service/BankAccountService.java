package com.example.project.service;

import java.util.List;

import com.example.project.entities.BankAccount;
import com.example.project.entities.User;

public interface BankAccountService {
    BankAccount findByAccountAndIfsc(String account, String ifsc);
    BankAccount findByUserMobile(String mobileNumber);
    BankAccount findByUpiId(String upiId);
    boolean isAccountOwnedByUser(String fromAccountNo,  String mobileNumber);
	BankAccount getPrimaryAccount(String receiverMobile);
	List<BankAccount> getAccountsByUser(User user);
	

}

