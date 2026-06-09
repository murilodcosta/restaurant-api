package com.murilodcosta.restaurant_api.dto;

public record OrderItemRequest(
        Long productId,
        Integer quantity,
        String notes
) {
}
