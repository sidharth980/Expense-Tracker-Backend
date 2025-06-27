package com.example.expt.repository;

import com.example.expt.entity.Expense;
import com.example.expt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findExpensesByPaidByUser(User paidByUser);

    List<Expense> findExpensesByPaidByUserAndExpenseDateBetween(User paidByUser, LocalDate expenseDateAfter, LocalDate expenseDateBefore);
}
