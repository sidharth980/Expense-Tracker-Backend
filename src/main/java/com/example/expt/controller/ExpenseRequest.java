package com.example.expt.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(Long userId, Long accountId, String category, String description,
                             BigDecimal amount, Long splitUser, BigDecimal splitAmount, LocalDate expenseDate) {
}
