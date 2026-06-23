package com.murilodcosta.restaurant_api.dto;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        String paymentMethod
) {
}
