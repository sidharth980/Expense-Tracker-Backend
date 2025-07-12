package com.example.expt.controller;

import com.example.expt.entity.Account;
import com.example.expt.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<Account> addAccount(@PathVariable Long userId, @RequestBody Account account) {
        Account newAccount = accountService.addAccount(userId, account);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Account>> getAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountService.getAccounts(userId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable Long accountId) {
        Account account = accountService.getAccount(accountId);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long accountId, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(accountId, account);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/pay-credit-card")
    public ResponseEntity<String> payCreditCardStatement(@RequestBody PayCreditCardRequest request) {
        accountService.payCreditCardStatement(request);
        return new ResponseEntity<>("Credit card payment processed successfully", HttpStatus.OK);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<Account> createAccount(@PathVariable Long userId, @RequestBody CreateAccountRequest request) {
        Account newAccount = accountService.createAccount(userId, request);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }
}
