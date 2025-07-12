package com.example.expt.controller;

import com.example.expt.entity.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequest {
    
    private String accountName;
    private AccountType accountType;
    private BigDecimal balance;
    private BigDecimal creditLimit;
    private Integer statementDate;
    
    public CreateAccountRequest() {}
    
    public CreateAccountRequest(String accountName, AccountType accountType, BigDecimal balance, 
                               BigDecimal creditLimit, Integer statementDate) {
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.statementDate = statementDate;
    }
}