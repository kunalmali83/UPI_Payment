package com.example.project.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
	private String name;
    private String email;
    private String mobileNumber;
    private String address;
	private List<BankAccountResp> accounts;

}
