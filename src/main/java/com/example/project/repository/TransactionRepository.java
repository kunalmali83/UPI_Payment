package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entities.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Optional: custom methods
	List<Transaction> findBySenderUpi(String upiId);
	List<Transaction> findByReceiverUpi(String upiId);
	List<Transaction> findByReceiverMobile(String mobile);
	List<Transaction> findByReceiverAccountNoAndReceiverIfsc(String account, String ifsc);
	List<Transaction> findBySenderUpiOrReceiverUpi(String senderUpi, String receiverUpi);

}
