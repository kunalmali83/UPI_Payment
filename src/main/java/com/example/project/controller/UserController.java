package com.example.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.UserLoginRequest;
import com.example.project.dto.UserProfileDTO;
import com.example.project.dto.UserSignUpReq;
import com.example.project.entities.User;
import com.example.project.service.OtpService;
import com.example.project.service.UserService;
import com.example.project.dto.BankAccountResp;

import jakarta.validation.Valid;
@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;

    public UserController() {
        System.out.println("✅ UserController loaded");
    }


    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        otpService.generateOtp(email);
        System.out.println("🔹 sendOtp called for: " + email); 
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-otp-register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserSignUpReq request) {
        if (!otpService.validateOtp(request.getEmail(), request.getOtp())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid or expired OTP");
            return ResponseEntity.badRequest().body(response);
        }
        return userService.registerUser(request);
    }
   

        // Map to DTO
    @GetMapping("/profile")
    public UserProfileDTO getProfile(@RequestHeader("Authorization") String authHeader) {
        System.out.println("🔹 Profile endpoint hit");

        String accountNumber = userService.getAccountNumberFromToken(authHeader);
        System.out.println("🔹 Account number from token: " + accountNumber);

        User user = userService.getUserByAccountNumber(accountNumber);

        UserProfileDTO response = new UserProfileDTO();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setMobileNumber(user.getMobileNumber());
        response.setAddress(user.getAddress());

        // Map bank accounts
        List<BankAccountResp> accountDTOs = user.getAccounts().stream().map(acc -> {
            BankAccountResp dto = new BankAccountResp();
            dto.setAccountNumber(acc.getAccountNumber()); // show full number for testing
            dto.setAccountHolder(acc.getAccountHolder());
            dto.setBankName(acc.getBankName());
            dto.setUpiId(acc.getUpiId());
            dto.setBalance(acc.getBalance());
            return dto;
        }).toList();

        System.out.println("🔹 Accounts: " + accountDTOs);

        response.setAccounts(accountDTOs);
        return response;
    }

}
