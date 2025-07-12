package com.example.expt.service;

import com.example.expt.controller.CreateAccountRequest;
import com.example.expt.controller.PayCreditCardRequest;
import com.example.expt.entity.Account;

import java.util.List;
import java.math.BigDecimal;

public interface AccountService {
    Account addAccount(Long userId, Account account);
    Account createAccount(Long userId, CreateAccountRequest request);
    List<Account> getAccounts(Long userId);
    Account getAccount(Long accountId);
    Account updateAccount(Long accountId, Account account);
    void deleteAccount(Long accountId);
    void updateAccountBalance(Long accountId, BigDecimal amountChange);
    void payCreditCardStatement(PayCreditCardRequest request);
}
