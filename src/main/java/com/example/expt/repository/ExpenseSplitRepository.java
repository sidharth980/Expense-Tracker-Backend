package com.example.expt.repository;

import com.example.expt.entity.ExpenseSplit;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {
    
    @Query("SELECT es FROM ExpenseSplit es WHERE es.user.userId = :userId AND es.expense.paidByUser.userId = :settlersUserId AND es.isSettled = false ORDER BY es.expense.expenseDate ASC")
    List<ExpenseSplit> findUnsettledSplitsByUserAndSettler(@Param("userId") Long userId, @Param("settlersUserId") Long settlersUserId);

    List<ExpenseSplit> findAllByIsSettled(Boolean isSettled);
}
