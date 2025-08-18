package com.example.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.dto.ChatOverviewDTO;
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
	
	@Query("SELECT t FROM Transaction t WHERE " +
		       "(t.senderMobile = :senderMobile AND t.receiverMobile = :receiverMobile) OR " +
		       "(t.senderMobile = :receiverMobile AND t.receiverMobile = :senderMobile) " +
		       "ORDER BY t.timestamp ASC")
		List<Transaction> findChatHistory(String senderMobile, String receiverMobile);
		
	@Query(value = "SELECT " +
	        "CASE WHEN sender_mobile = :mobile THEN receiver_mobile ELSE sender_mobile END AS otherMobile, " +
	        "CASE WHEN sender_mobile = :mobile THEN receiver_upi ELSE sender_upi END AS otherUpi, " +
	        "MAX(timestamp) AS lastTransactionTime, " +
	        "MAX(amount) AS lastAmount " +
	        "FROM transaction " +
	        "WHERE sender_mobile = :mobile OR receiver_mobile = :mobile " +
	        "GROUP BY otherMobile, otherUpi",
	        nativeQuery = true)
	List<Object[]> findAllChatsRaw(@Param("mobile") String mobile);




}
