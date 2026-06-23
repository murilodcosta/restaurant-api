package com.murilodcosta.restaurant_api.dto;

import java.math.BigDecimal;

public record BillRequest(
        BigDecimal serviceFee,
        BigDecimal discount
) {
}
