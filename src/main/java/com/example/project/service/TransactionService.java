package com.example.project.service;

import com.example.project.dto.TransferRequestDTO;

public interface TransactionService {


	
	void transferMoney(String senderAccountNo, TransferRequestDTO request);


}
