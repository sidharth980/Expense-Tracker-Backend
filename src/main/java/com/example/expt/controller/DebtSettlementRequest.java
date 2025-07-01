package com.example.expt.controller;

import java.math.BigDecimal;

public record DebtSettlementRequest(Long userId, Long settlersUserId, BigDecimal amountPaid,
                                    Long accountId) {
}