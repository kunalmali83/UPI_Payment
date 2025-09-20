package com.example.project.dto;



import lombok.Data;

@Data
public class BankAccountReq {
    private String accountNumber;
    private String accountHolder;
    private String bankName;
    private String pin;
    private String ifsc;
	
}
