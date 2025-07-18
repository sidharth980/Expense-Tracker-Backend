package com.example.expt.controller;

import com.example.expt.entity.Expense;
import com.example.expt.service.ExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expenses")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ExpenseController {

    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseRequest expense) {
        Expense savedExpense = expenseService.addExpense(expense);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUserId(@PathVariable Long userId) {
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/expense/{userId}")
    public ResponseEntity<Map<String, Double>> getExpenseThisMonth(@PathVariable Long userId) {
        Map<String, Double> expenses = expenseService.getExpensesByUserIdAndMonth(userId);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/debt/{userId}")
    public ResponseEntity<List<DebtSummaryResponse>> getDebtSummary(@PathVariable Long userId) {
        List<DebtSummaryResponse> debtSummary = expenseService.getDebtSummary(userId);
        return new ResponseEntity<>(debtSummary, HttpStatus.OK);
    }

    @PostMapping("/settle-debt")
    public ResponseEntity<String> settleDebt(@RequestBody DebtSettlementRequest request) {
            String result = expenseService.settleDebt(request);
            return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long expenseId, @RequestBody ExpenseRequest expense) {
        Expense updatedExpense = expenseService.updateExpense(expenseId, expense);
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }
}
