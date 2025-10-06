package com.example.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.dto.UserLoginRequest;
import com.example.project.dto.UserProfileDTO;
import com.example.project.dto.UserSignUpReq;
import com.example.project.entities.BankAccount;
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

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).build();
            }

            String mobileNumber = userService.getMobileFromToken(authHeader);
            User user = userService.getUserByMobile(mobileNumber);

            List<BankAccount> accounts = user.getAccounts();
            accounts.size(); // ensure loaded

            UserProfileDTO response = new UserProfileDTO();
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setMobileNumber(user.getMobileNumber());
            response.setAddress(user.getAddress());

            List<BankAccountResp> accountDTOs = accounts.stream().map(acc -> {
                BankAccountResp dto = new BankAccountResp();
                dto.setAccountNumber(acc.getAccountNumber());
                dto.setAccountHolder(acc.getAccountHolder());
                dto.setBankName(acc.getBankName());
                dto.setUpiId(acc.getUpiId());
                dto.setBalance(acc.getBalance());
                dto.setPrimary(acc.isPrimary());
                return dto;
            }).toList();

            response.setAccounts(accountDTOs);

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<List<BankAccountResp>> getMyAccounts(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).build();
            }

            String mobileNumber = userService.getMobileFromToken(authHeader);
            User user = userService.getUserByMobile(mobileNumber);

            List<BankAccount> accounts = user.getAccounts();
            accounts.size();

            List<BankAccountResp> accountDTOs = accounts.stream().map(acc -> {
                BankAccountResp dto = new BankAccountResp();
                dto.setAccountNumber(acc.getAccountNumber());
                dto.setAccountHolder(acc.getAccountHolder());
                dto.setBankName(acc.getBankName());
                dto.setUpiId(acc.getUpiId());
                dto.setBalance(acc.getBalance());
                dto.setPrimary(acc.isPrimary());
                return dto;
            }).toList();

            return ResponseEntity.ok(accountDTOs);

        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{mobile}/accounts")
    public ResponseEntity<List<BankAccountResp>> getAccountsByMobile(@PathVariable String mobile) {
        try {
            User user = userService.getUserByMobile(mobile);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            List<BankAccount> accounts = user.getAccounts();
            accounts.size();

            List<BankAccountResp> accountDTOs = accounts.stream().map(acc -> {
                BankAccountResp dto = new BankAccountResp();
                dto.setAccountNumber(acc.getAccountNumber());
                dto.setAccountHolder(acc.getAccountHolder());
                dto.setBankName(acc.getBankName());
                dto.setUpiId(acc.getUpiId());
                dto.setBalance(acc.getBalance());
                dto.setPrimary(acc.isPrimary());
                return dto;
            }).toList();

            return ResponseEntity.ok(accountDTOs);
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }
}
