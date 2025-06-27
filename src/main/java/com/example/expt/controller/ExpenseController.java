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
@CrossOrigin(origins = "http://localhost:3000")
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
}
