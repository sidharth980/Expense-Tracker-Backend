package com.example.expt.controller;

import java.math.BigDecimal;

public record DebtSummaryResponse(
    Long otherUserId,
    String otherUserName,
    BigDecimal youOwe,
    BigDecimal theyOwe,
    BigDecimal netAmount
) {}