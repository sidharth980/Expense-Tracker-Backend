package com.example.expt.service;

import com.example.expt.controller.DebtSettlementRequest;
import com.example.expt.controller.DebtSummaryResponse;
import com.example.expt.controller.ExpenseRequest;
import com.example.expt.entity.Expense;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public interface ExpenseService {

     Expense addExpense(ExpenseRequest request);

    List<Expense> getExpensesByUserId(Long userId);

    Map<String, Double> getExpensesByUserIdAndMonth(Long userId);

    List<DebtSummaryResponse> getDebtSummary(Long userId);
    
    String settleDebt(DebtSettlementRequest request);
    
    Expense updateExpense(Long expenseId, ExpenseRequest request);
}
