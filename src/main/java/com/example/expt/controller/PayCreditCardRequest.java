package com.example.expt.controller;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PayCreditCardRequest {
    
    private Long creditCardAccountId;
    private Long paidByAccountId;
    private BigDecimal amount;
    private LocalDate paymentDate;
    
    public PayCreditCardRequest() {}
    
    public PayCreditCardRequest(Long creditCardAccountId, Long paidByAccountId, BigDecimal amount, LocalDate paymentDate) {
        this.creditCardAccountId = creditCardAccountId;
        this.paidByAccountId = paidByAccountId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }
}