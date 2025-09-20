package com.example.project.dto;

import java.util.List;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserSignUpReq {

	@NotBlank
	private String name;
	@Email
	@NotBlank
    private String email;
	
	  @Size(min = 3)
    private String password;
    private String address;
    @Pattern(regexp = "\\d{10}")
     private String mobileNumber;
   
    
    private List<BankAccountReq> accounts;
    private String otp;

}
