package com.example.project.security;

import com.example.project.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final String accountNumber; // This holds login account number

    public CustomUserDetails(User user, String accountNumber) {
        this.user = user;
        this.accountNumber = accountNumber;
    }

    @Override
    public String getUsername() {
        return accountNumber;  // ✅ NOT from User entity
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public User getUser() {
        return user;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
