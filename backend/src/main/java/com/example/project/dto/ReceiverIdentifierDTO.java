package com.example.project.dto;

import com.example.project.enums.TransferMethod;

import lombok.Data;

@Data
public class ReceiverIdentifierDTO {

    private TransferMethod method;

    private String bankAccount;
    private String ifsc;

    private String mobile;

    private String upiId;
}

