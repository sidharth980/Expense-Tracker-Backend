package com.example.expt.service.impl;

import com.example.expt.entity.Account;
import com.example.expt.entity.AccountType;
import com.example.expt.entity.User;
import com.example.expt.repository.AccountRepository;
import com.example.expt.repository.UserRepository;
import com.example.expt.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Account addAccount(Long userId, Account account) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        account.setUser(user);
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts(Long userId) {
        return accountRepository.findAll().stream().filter(account -> account.getUser().getUserId().equals(userId)).toList();
    }

    @Override
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found with id: " + accountId));
    }

    @Override
    public Account updateAccount(Long accountId, Account account) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found with id: " + accountId));

        existingAccount.setAccountName(account.getAccountName());
        existingAccount.setAccountType(account.getAccountType());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setCreditLimit(account.getCreditLimit());
        existingAccount.setStatementDate(account.getStatementDate());

        return accountRepository.save(existingAccount);
    }

    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public void updateAccountBalance(Long accountId, BigDecimal amountChange) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found with id: " + accountId));

        BigDecimal currentBalance = account.getBalance();
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }

        BigDecimal newBalance = currentBalance.add(amountChange);
        if(account.getAccountType().equals(AccountType.CREDIT_CARD)){
            newBalance = currentBalance.subtract(amountChange);
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
