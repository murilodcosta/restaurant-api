package com.murilodcosta.restaurant_api.service;

import com.murilodcosta.restaurant_api.domain.entity.Order;
import com.murilodcosta.restaurant_api.domain.entity.OrderItem;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrder;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrderItem;
import com.murilodcosta.restaurant_api.domain.enums.StatusRestaurantTable;
import com.murilodcosta.restaurant_api.dto.OrderItemRequest;
import com.murilodcosta.restaurant_api.dto.OrderItemResponse;
import com.murilodcosta.restaurant_api.dto.OrderRequest;
import com.murilodcosta.restaurant_api.dto.OrderResponse;
import com.murilodcosta.restaurant_api.exception.BusinessRuleException;
import com.murilodcosta.restaurant_api.repository.OrderItemRepository;
import com.murilodcosta.restaurant_api.repository.OrderRepository;
import com.murilodcosta.restaurant_api.repository.ProductRepository;
import com.murilodcosta.restaurant_api.repository.RestaurantTableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, RestaurantTableRepository restaurantTableRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.restaurantTableRepository = restaurantTableRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
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

    public OrderItemResponse addItem(Long orderId, OrderItemRequest orderItemRequest) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessRuleException("Order not found"));

        if (order.getStatus() != StatusOrder.OPEN) {
            throw new BusinessRuleException("Cannot add items to a closed order");
        }

        var product = productRepository.findById(orderItemRequest.productId())
                .orElseThrow(() -> new BusinessRuleException("Product not found"));

        if (!product.getAvailable()) {
            throw new BusinessRuleException("Product is not available");
        }

        if (orderItemRequest.quantity() == null || orderItemRequest.quantity() <= 0) {
            throw new BusinessRuleException("Quantity must be greater than zero");
        }

        var orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequest.quantity());
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setNotes(orderItemRequest.notes());
        orderItem.setStatus(StatusOrderItem.PENDING);

        var savedOrderItem = orderItemRepository.save(orderItem);

        return OrderItemResponse.fromEntity(savedOrderItem);
    }

    public List<OrderItemResponse> listItems(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessRuleException("Order not found"));

        var orderItems = orderItemRepository.findByOrderId(orderId);

        return orderItems.stream()
                .map(OrderItemResponse::fromEntity)
                .toList();
    }


}