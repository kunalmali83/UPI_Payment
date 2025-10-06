package com.example.project.security;

import com.example.project.entities.BankAccount;
import com.example.project.entities.User;
import com.example.project.repository.BalanceRepo;
import com.example.project.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // ✅ looks in bank_accounts
    
    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        User user = userRepo.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with mobile: " + mobileNumber));

        return new CustomUserDetails(user);
    }
}
