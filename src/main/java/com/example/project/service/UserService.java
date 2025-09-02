package com.example.project.service;


import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.project.dto.BankAccountReq;
import com.example.project.dto.UserLoginRequest;
import com.example.project.dto.UserResponse;
import com.example.project.dto.UserSignUpReq;
import com.example.project.entities.BankAccount;
import com.example.project.entities.User;
import com.example.project.repository.BalanceRepo;
import com.example.project.repository.UserRepo;
import com.example.project.security.CustomUserDetails;
import com.example.project.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BalanceRepo balanceRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;


    public ResponseEntity<Map<String, String>> registerUser(UserSignUpReq request) {
    	
    	Map<String, String> response = new HashMap<>();

        // Check if email already exists
        if (userRepo.existsByEmail(request.getEmail())) {
            response.put("message", "Email already registered");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

      

        // Check account numbers
        for (BankAccountReq accReq : request.getAccounts()) {
            if (balanceRepo.existsByAccountNumber(accReq.getAccountNumber())) {
                response.put("message", "Account number " + accReq.getAccountNumber() + " already exists.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        }


        // Create new user
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setAddress(request.getAddress());
        newUser.setMobileNumber(request.getMobileNumber());

        List<BankAccount> savedAccounts = new ArrayList<>();
        for (BankAccountReq accReq : request.getAccounts()) {
            BankAccount account = new BankAccount();
            account.setAccountNumber(accReq.getAccountNumber());
            account.setAccountHolder(accReq.getAccountHolder());
            account.setBalance(BigDecimal.ZERO);
            account.setUser(newUser); // Set owning user
            account.setBankName(accReq.getBankName());
            account.setPin(passwordEncoder.encode(accReq.getPin()));
            account.setIfsc(accReq.getIfsc());


            // Generate unique UPI ID
            String baseUpiId = accReq.getAccountHolder().toLowerCase().replaceAll("\\s+", "") + "@" + accReq.getBankName().toLowerCase();
            String uniqueUpiId = baseUpiId;
            int counter = 1;
            while (balanceRepo.existsByUpiId(uniqueUpiId)) {
                uniqueUpiId = baseUpiId + counter;
                counter++;
            }
            account.setUpiId(uniqueUpiId);

            savedAccounts.add(account);
        }

        newUser.setAccounts(savedAccounts);
        userRepo.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<?> login(UserLoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getAccountNumber(), loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            String token = jwtUtil.generateToken(userDetails.getAccountNumber());

            UserResponse response = new UserResponse(user.getName(), user.getEmail(), token);
            return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(response);

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


}



