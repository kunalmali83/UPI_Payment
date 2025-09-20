package com.example.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class ChatOverviewDTO {
    private String otherMobile;
    private String otherUpi;
    private LocalDateTime lastTransactionTime;
    private BigDecimal lastAmount;

    public ChatOverviewDTO(String otherMobile, String otherUpi, LocalDateTime lastTransactionTime, BigDecimal lastAmount) {
        this.otherMobile = otherMobile;
        this.otherUpi = otherUpi;
        this.lastTransactionTime = lastTransactionTime;
        this.lastAmount = lastAmount;
    }

  
}

