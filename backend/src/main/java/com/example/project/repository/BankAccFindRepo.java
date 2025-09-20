package com.example.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entities.BankAccount;
import com.example.project.entities.User;

@Repository
public interface BankAccFindRepo extends JpaRepository<BankAccount, String> {

    Optional<BankAccount> findByAccountNumberAndIfsc(String accountNo, String ifsc);


    Optional<BankAccount> findByUpiId(String upiId);

    List<BankAccount> findFirstByUser_MobileNumber(String mobileNumber);

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    List<BankAccount> findByUser(User user);
}
