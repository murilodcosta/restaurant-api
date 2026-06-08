package com.murilodcosta.restaurant_api.service;

import com.murilodcosta.restaurant_api.domain.entity.Order;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrder;
import com.murilodcosta.restaurant_api.domain.enums.StatusRestaurantTable;
import com.murilodcosta.restaurant_api.dto.OrderRequest;
import com.murilodcosta.restaurant_api.dto.OrderResponse;
import com.murilodcosta.restaurant_api.exception.BusinessRuleException;
import com.murilodcosta.restaurant_api.repository.OrderRepository;
import com.murilodcosta.restaurant_api.repository.RestaurantTableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public OrderService(OrderRepository orderRepository, RestaurantTableRepository restaurantTableRepository) {
        this.orderRepository = orderRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    public OrderResponse create(OrderRequest orderRequest) {
        var restaurantTable = restaurantTableRepository.findById(orderRequest.restaurantTableId())
                .orElseThrow(() -> new BusinessRuleException("Restaurant table not found"));

        if (restaurantTable.getStatus() != StatusRestaurantTable.AVAILABLE) {
            throw new BusinessRuleException("Restaurant table is not available");
        }

        var order = new Order();
        order.setRestaurantTable(restaurantTable);
        order.setStatus(StatusOrder.OPEN);
        order.setNotes(orderRequest.notes());

        restaurantTable.setStatus(StatusRestaurantTable.OCCUPIED);

        var savedOrder = orderRepository.save(order);
        restaurantTableRepository.save(restaurantTable);
        
        return OrderResponse.fromEntity(savedOrder);
    }

    public Page<OrderResponse> listAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::fromEntity);
    }

    public OrderResponse findById(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Order not found"));
        return OrderResponse.fromEntity(order);
    }
}