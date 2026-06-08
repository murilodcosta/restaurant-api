package com.murilodcosta.restaurant_api.dto;

public record OrderRequest(
        Long restaurantTableId,
        String notes
) {
}
