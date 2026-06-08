package com.murilodcosta.restaurant_api.dto;

import com.murilodcosta.restaurant_api.domain.entity.Order;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrder;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long restaurantTableId,
        Integer restaurantTableNumber,
        LocalDateTime openedAt,
        LocalDateTime closedAt,
        StatusOrder status,
        String notes
) {
    public static OrderResponse fromEntity(Order order){
        return new OrderResponse(
                order.getId(),
                order.getRestaurantTable().getId(),
                order.getRestaurantTable().getNumber(),
                order.getOpenedAt(),
                order.getClosedAt(),
                order.getStatus(),
                order.getNotes()
        );
    }
}
