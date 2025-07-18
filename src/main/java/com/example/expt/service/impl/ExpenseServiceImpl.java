package com.example.expt.service.impl;

import com.example.expt.controller.DebtSettlementRequest;
import com.example.expt.controller.DebtSummaryResponse;
import com.example.expt.controller.ExpenseRequest;
import com.example.expt.entity.Account;
import com.example.expt.entity.Expense;
import com.example.expt.entity.ExpenseSplit;
import com.example.expt.entity.User;
import com.example.expt.repository.AccountRepository;
import com.example.expt.repository.ExpenseRepository;
import com.example.expt.repository.ExpenseSplitRepository;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private ExpenseRepository expenseRepository;

    private AccountService accountService;

    private UserRepository userRepository;

    private final AccountRepository accountRepository;
    
    private ExpenseSplitRepository expenseSplitRepository;

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
        List<Expense> expensesByPaidByUser = expenseRepository.findExpensesByPaidByUser(user);
        expensesByPaidByUser.sort(Comparator.comparing(Expense::getCreatedAt));
        return expensesByPaidByUser;
    }

    @Override
    public Map<String, Double> getExpensesByUserIdAndMonth(Long userId) {
        LocalDate today = LocalDate.now();
        User user = User.builder().userId(userId).build();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        List<Expense> expensesByPaidByUserAndExpenseDateBetween = expenseRepository.
                findExpensesByPaidByUserAndExpenseDateBetween(user, startOfMonth,
                        endOfMonth);

        List<ExpenseSplit> expenseSplits = expenseSplitRepository.findAllByUserIdAndExpenseDateBetween(userId, startOfMonth, endOfMonth);
        List<Expense> expensesFromSplits = expenseSplits.stream()
                .map(ExpenseSplit::getExpense)
                .toList();

        expensesByPaidByUserAndExpenseDateBetween.addAll(expensesFromSplits);
        Map<String, Double> expensesByPaidByUserAndExpenseDate = new HashMap<>();
        expensesByPaidByUserAndExpenseDateBetween.forEach(expense ->
            addToMap(expense, expensesByPaidByUserAndExpenseDate));
        return expensesByPaidByUserAndExpenseDate;
    }

    private static void addToMap(Expense expense, Map<String, Double> expensesByPaidByUserAndExpenseDate) {
        if (expense.getCategory().equals("statement")){
            return;
        }
        double values = expensesByPaidByUserAndExpenseDate.getOrDefault(expense.getCategory(), 0.0)
                + expense.getAmount().doubleValue() - expense.getShares().stream().map(ExpenseSplit::getOwesAmount).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
        expensesByPaidByUserAndExpenseDate.put(expense.getCategory(), values);
    }
    
    @Override
    public List<DebtSummaryResponse> getDebtSummary(Long userId) {
        List<ExpenseSplit> allSplits = expenseSplitRepository.findAllByIsSettled(false);
        Map<Long, DebtCalculation> debtMap = new HashMap<>();
        
        for (ExpenseSplit split : allSplits) {
            Long paidByUserId = split.getExpense().getPaidByUser().getUserId();
            Long owesUserId = split.getUser().getUserId();
            BigDecimal amount = split.getOwesAmount();

            if (paidByUserId.equals(userId)) {
                debtMap.computeIfAbsent(owesUserId, k -> new DebtCalculation())
                    .addTheyOwe(amount);
            } else if (owesUserId.equals(userId)) {
                debtMap.computeIfAbsent(paidByUserId, k -> new DebtCalculation())
                    .addYouOwe(amount);
            }
        }
        
        List<DebtSummaryResponse> result = new ArrayList<>();
        for (Map.Entry<Long, DebtCalculation> entry : debtMap.entrySet()) {
            Long otherUserId = entry.getKey();
            DebtCalculation calc = entry.getValue();
            User otherUser = userRepository.findById(otherUserId).orElse(null);
            
            if (otherUser != null) {
                BigDecimal netAmount = calc.theyOwe.subtract(calc.youOwe);
                result.add(new DebtSummaryResponse(
                    otherUserId,
                    otherUser.getUsername(),
                    calc.youOwe,
                    calc.theyOwe,
                    netAmount
                ));
            }
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public String settleDebt(DebtSettlementRequest request) {
        if (request.amountPaid() == null || request.amountPaid().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount paid must be positive");
        }

        List<ExpenseSplit> unsettledSplits = expenseSplitRepository
                .findUnsettledSplitsByUserAndSettler(request.userId(), request.settlersUserId());

        if (unsettledSplits.isEmpty()) {
            return "No unsettled debts found between these users";
        }

        BigDecimal remainingAmount = request.amountPaid();
        int settledCount = 0;
        BigDecimal totalSettled = BigDecimal.ZERO;

        for (ExpenseSplit split : unsettledSplits) {
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal owesAmount = split.getOwesAmount();
            
            if (remainingAmount.compareTo(owesAmount) >= 0) {
                split.setIsSettled(true);
//                remainingAmount = remainingAmount.subtract(owesAmount);
                totalSettled = totalSettled.add(owesAmount);
                settledCount++;
            } else {
                split.setOwesAmount(owesAmount.subtract(remainingAmount));
                totalSettled = totalSettled.add(remainingAmount);
                remainingAmount = BigDecimal.ZERO;
            }
            
            expenseSplitRepository.save(split);
        }

        ExpenseRequest expenseRequest = new ExpenseRequest(request.userId(), request.accountId(), "Settle", "Settle", totalSettled, null, null, LocalDate.now());
        addExpense(expenseRequest);


        return String.format("Settlement completed. Settled %d expense splits totaling $%.2f. Remaining amount: $%.2f", 
                settledCount, totalSettled.doubleValue(), remainingAmount.doubleValue());
    }

    @Override
    @Transactional
    public Expense updateExpense(Long expenseId, ExpenseRequest request) {
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with ID: " + expenseId));

        User paidByUser = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User (payer) not found with ID: " + request.userId()));

        Account accountUsed = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + request.accountId()));

        BigDecimal oldAmount = existingExpense.getAmount();
        BigDecimal newAmount = request.amount();
        BigDecimal amountDifference = newAmount.subtract(oldAmount);

        if (amountDifference.compareTo(BigDecimal.ZERO) != 0) {
            accountService.updateAccountBalance(accountUsed.getAccountId(), amountDifference);
        }

        existingExpense.setDescription(request.description());
        existingExpense.setAmount(newAmount);
        existingExpense.setExpenseDate(request.expenseDate());
        existingExpense.setPaidByUser(paidByUser);
        existingExpense.setAccount(accountUsed);
        existingExpense.setCategory(request.category());

        existingExpense.getShares().clear();
        
        if (request.splitUser() != null && request.splitAmount() != null) {
            User owingUser = userRepository.findById(request.splitUser())
                    .orElseThrow(() -> new IllegalArgumentException("Split user not found with ID: " + request.splitUser()));

            ExpenseSplit split = ExpenseSplit.builder()
                    .user(owingUser)
                    .owesAmount(request.splitAmount())
                    .isSettled(false)
                    .build();

            existingExpense.addShare(split);
        }

        return expenseRepository.save(existingExpense);
    }

    private static class DebtCalculation {
        BigDecimal youOwe = BigDecimal.ZERO;
        BigDecimal theyOwe = BigDecimal.ZERO;
        
        void addYouOwe(BigDecimal amount) {
            youOwe = youOwe.add(amount);
        }
        
        void addTheyOwe(BigDecimal amount) {
            theyOwe = theyOwe.add(amount);
        }
    }
}
