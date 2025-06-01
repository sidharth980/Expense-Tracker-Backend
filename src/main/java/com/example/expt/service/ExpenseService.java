package com.example.expt.service;

import com.example.expt.controller.ExpenseRequest;
import com.example.expt.entity.Expense;
import jakarta.transaction.Transactional;

import java.util.List;


public interface ExpenseService {

     Expense addExpense(ExpenseRequest request);

    List<Expense> getExpensesByUserId(Long userId);
}
