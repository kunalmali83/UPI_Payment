package com.example.project.security;

import com.example.project.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Expose the whole User entity if needed
    public User getUser() {
        return user;
    }

    public String getMobileNumber() {
        return user.getMobileNumber();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // add roles later if needed
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // password or hashed PIN
    }

    @Override
    public String getUsername() {
        return user.getMobileNumber(); // ✅ login identifier is mobile number
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
