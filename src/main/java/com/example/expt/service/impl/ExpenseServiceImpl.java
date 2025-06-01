package com.example.expt.service.impl;

import com.example.expt.controller.ExpenseRequest;
import com.example.expt.entity.Account;
import com.example.expt.entity.Expense;
import com.example.expt.entity.ExpenseSplit;
import com.example.expt.entity.User;
import com.example.expt.repository.AccountRepository;
import com.example.expt.repository.ExpenseRepository;
import com.example.expt.repository.UserRepository;
import com.example.expt.service.AccountService;
import com.example.expt.service.ExpenseService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private ExpenseRepository expenseRepository;

    private AccountService accountService;

    private UserRepository userRepository;

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Expense addExpense(ExpenseRequest request) { // Renamed for clarity: this is the incoming request data
        User paidByUser = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User (payer) not found with ID: " + request.userId()));

        Account accountUsed = accountRepository.findById(request.accountId()) // Use request.accountId() if you fix the typo
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + request.accountId()));

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive.");
        }

        Expense newExpense = Expense.builder()
                .account(accountUsed)
                .paidByUser(paidByUser)
                .amount(request.amount())
                .expenseDate(request.expenseDate())
                .description(request.description())
                .category(request.category())
                .shares(new ArrayList<>())
                .createdAt(LocalDateTime.now()).build();

        newExpense = expenseRepository.save(newExpense);

        if (request.splitUser() != null && request.splitAmount() != null) {
            if (request.splitAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Split amount must be positive.");
            }
            if (request.splitAmount().compareTo(request.amount()) > 0) {
                throw new IllegalArgumentException("Split amount cannot exceed the total expense amount.");
            }

            User userInSplit = userRepository.findById(request.splitUser())
                    .orElseThrow(() -> new IllegalArgumentException("User (in split) not found with ID: " + request.splitUser()));

            ExpenseSplit split = ExpenseSplit.builder()
                    .owesAmount(request.splitAmount())
                    .isSettled(false)
                    .created_at(LocalDate.now())
                    .expense(newExpense)
                    .user(userInSplit).build();
            if(newExpense.getShares() == null){
            newExpense.setShares(List.of(split));
            }
            else{
                newExpense.getShares().add(split);
            }
        }

        Expense savedExpense = expenseRepository.save(newExpense);

        accountService.updateAccountBalance(accountUsed.getAccountId(), request.amount().negate());

        return savedExpense;
    }

    @Override
    public List<Expense> getExpensesByUserId(Long userId) {
        User user = userRepository.findById(userId).get();
        return expenseRepository.findExpensesByPaidByUser(user);
    }
}
