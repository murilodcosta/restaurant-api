package com.murilodcosta.restaurant_api.dto;

import com.murilodcosta.restaurant_api.domain.entity.OrderItem;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long orderId,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String notes,
        StatusOrderItem status
) {

    public static OrderItemResponse fromEntity(OrderItem orderItem){
        BigDecimal totalPrice = orderItem.getUnitPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                totalPrice,
                orderItem.getNotes(),
                orderItem.getStatus()
        );
    }

}
