package com.example.project.dto;



import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceResponse {
 private String accountNumber;
 private BigDecimal balance;
}
