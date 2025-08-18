package com.example.project.service;

import java.util.List;

import com.example.project.dto.ChatOverviewDTO;
import com.example.project.dto.TransferRequestDTO;
import com.example.project.entities.Transaction;

public interface TransactionService {


	
	void transferMoney(String senderAccountNo, TransferRequestDTO request);

	List<Transaction> getChatHistory(String loggedInMobile, String otherMobile);


	List<ChatOverviewDTO> getAllChatsForUser(String mobile);

}
