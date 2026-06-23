package com.murilodcosta.restaurant_api.dto;

public record PaymentResponse(
        String status,
        String transactionId
) {
}
