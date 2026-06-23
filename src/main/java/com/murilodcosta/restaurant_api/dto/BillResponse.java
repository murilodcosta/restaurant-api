package com.murilodcosta.restaurant_api.dto;

import com.murilodcosta.restaurant_api.domain.entity.Bill;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BillResponse(
        Long id,
        Long orderId,
        Integer restaurantTableNumber,
        BigDecimal subtotal,
        BigDecimal serviceFee,
        BigDecimal discount,
        BigDecimal total,
        LocalDateTime closedAt
) {
    public static BillResponse fromEntity(Bill bill) {
        return new BillResponse(
                bill.getId(),
                bill.getOrder().getId(),
                bill.getOrder().getRestaurantTable().getNumber(),
                bill.getSubtotal(),
                bill.getServiceFee(),
                bill.getDiscount(),
                bill.getTotal(),
                bill.getClosedAt()
        );
    }
}
