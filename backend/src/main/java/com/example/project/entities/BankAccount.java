package com.example.project.entities;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.ToString;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {

	  @Column(name = "account_holder")
	    private String accountHolder;

	    @Column(name = "upi_id",nullable = false, unique = true)
	    private String upiId;

	    @Id
	    @Column(name = "account_number",nullable = false, unique = true)
	    private String accountNumber;

	    @Column(name = "balance")
	    private BigDecimal balance;
	    @ManyToOne
	    @JoinColumn(name = "user_id") 
	    @JsonBackReference
	    @ToString.Exclude 
	    private User user;
	    
	    @Column(name = "bank_name")
	    private String bankName;
	    
	    @Column(nullable = false)
	    private String pin;  // Store hashed PIN
	    private boolean isprimary;
	    private String ifsc;
	public BankAccount() {
	
	}
	
	public boolean isPrimary() {
        return isprimary;
    }

    public void setPrimary(boolean isprimary) {
        this.isprimary = isprimary;
    }




	public String getIfsc() {
		return ifsc;
	}





	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}





	public BankAccount(String accountHolder, String upiId, String accountNumber, BigDecimal balance, User user,
			String bankName, String pin, String ifsc,boolean isprimary) {
		super();
		this.accountHolder = accountHolder;
		this.upiId = upiId;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.user = user;
		this.bankName = bankName;
		this.pin = pin;
		this.ifsc = ifsc;
		this.isprimary=isprimary;
	}





	public User getUser() {
		return user;
	}

	public String getBankName() {
		return bankName;
	}



	public void setBankName(String bankName) {
		this.bankName = bankName;
	}



	public void setUser(User user) {
		this.user = user;
	}

	public String getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	public String getUpiId() {
		return upiId;
	}
	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}



	public String getPin() {
		return pin;
	}



	public void setPin(String pin) {
		this.pin = pin;
	}




	
    
    

	
}
