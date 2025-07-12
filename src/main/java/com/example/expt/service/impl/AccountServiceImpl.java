package com.example.expt.service.impl;

import com.example.expt.controller.CreateAccountRequest;
import com.example.expt.controller.PayCreditCardRequest;
import com.example.expt.entity.Account;
import com.example.expt.entity.AccountType;
import com.example.expt.entity.Expense;
import com.example.expt.entity.User;
import com.example.expt.repository.AccountRepository;
import com.example.expt.repository.ExpenseRepository;
import com.example.expt.repository.UserRepository;
import com.example.expt.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Account addAccount(Long userId, Account account) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        account.setUser(user);
        return accountRepository.save(account);
    }

    @Override
    public Account createAccount(Long userId, CreateAccountRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        Account account = new Account();
        account.setUser(user);
        account.setAccountName(request.getAccountName());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setCreditLimit(request.getCreditLimit());
        account.setStatementDate(request.getStatementDate());
        account.setCreatedAt(LocalDateTime.now());

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

    @Override
    @Transactional
    public void payCreditCardStatement(PayCreditCardRequest request) {
        Account creditCardAccount = accountRepository.findById(request.getCreditCardAccountId())
                .orElseThrow(() -> new NoSuchElementException("Credit card account not found with id: " + request.getCreditCardAccountId()));

        Account paidByAccount = accountRepository.findById(request.getPaidByAccountId())
                .orElseThrow(() -> new NoSuchElementException("Payment account not found with id: " + request.getPaidByAccountId()));

        if (!creditCardAccount.getAccountType().equals(AccountType.CREDIT_CARD)) {
            throw new IllegalArgumentException("Account " + request.getCreditCardAccountId() + " is not a credit card account");
        }

        BigDecimal currentCreditBalance = creditCardAccount.getBalance();
        if (currentCreditBalance == null) {
            currentCreditBalance = BigDecimal.ZERO;
        }

        BigDecimal newCreditBalance = currentCreditBalance.subtract(request.getAmount());
        creditCardAccount.setBalance(newCreditBalance);

        BigDecimal currentPaymentBalance = paidByAccount.getBalance();
        if (currentPaymentBalance == null) {
            currentPaymentBalance = BigDecimal.ZERO;
        }

        BigDecimal newPaymentBalance = currentPaymentBalance.subtract(request.getAmount());
        paidByAccount.setBalance(newPaymentBalance);

        Expense statementExpense = Expense.builder()
                .description("Credit Card Statement Payment - " + creditCardAccount.getAccountName())
                .amount(request.getAmount())
                .expenseDate(request.getPaymentDate())
                .paidByUser(paidByAccount.getUser())
                .account(paidByAccount)
                .category("statement")
                .createdAt(LocalDateTime.now())
                .build();

        accountRepository.save(creditCardAccount);
        accountRepository.save(paidByAccount);
        expenseRepository.save(statementExpense);
    }
}
