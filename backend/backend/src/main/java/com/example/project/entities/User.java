package com.example.project.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true) 
    private String email;

    @Column(nullable = false)
    private String password;

    private String address;

    @Column(name = "mobile_number", nullable = false, unique = true) // ✅ make it unique
    private String mobileNumber;

    // ❌ remove ifsc here (belongs in BankAccount)
    // private String ifsc;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude 
    private List<BankAccount> accounts = new ArrayList<>();

    // Constructors
    public User() {}

    public User(Long id, String name, String email, String password, String address, String mobileNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.mobileNumber = mobileNumber;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public List<BankAccount> getAccounts() { return accounts; }
    public void setAccounts(List<BankAccount> accounts) { this.accounts = accounts; }
}
