package com.murilodcosta.restaurant_api.dto;

import com.murilodcosta.restaurant_api.domain.entity.OrderItem;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrderItem;

import java.math.BigDecimal;

public record KitchenItemResponse(
        Long itemId,
        Long orderId,
        Integer restaurantTableNumber,
        String productName,
        Integer quantity,
        String notes,
        BigDecimal unitPrice,
        StatusOrderItem statusOrderItem
) {

    public static KitchenItemResponse fromEntity(OrderItem orderItem) {
        return new KitchenItemResponse(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getOrder().getRestaurantTable().getNumber(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getNotes(),
                orderItem.getUnitPrice(),
                orderItem.getStatus()
        );
    }
}
