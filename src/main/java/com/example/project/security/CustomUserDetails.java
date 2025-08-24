package com.example.project.security;

import com.example.project.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final User user;
    private final String accountNumber;
    private final String mobileNumber;

    public CustomUserDetails(User user, String accountNumber) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.mobileNumber = user.getMobileNumber(); // ✅ fetch from linked User
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    // ✅ Add this so you can fetch the whole User entity
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // add roles later if needed
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
    	return accountNumber; // or accountNumber if login by accountNo
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
