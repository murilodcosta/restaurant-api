package com.murilodcosta.restaurant_api.repository;

import com.murilodcosta.restaurant_api.domain.entity.OrderItem;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findByStatusOrderByIdAsc(StatusOrderItem statusOrderItem);

    List<OrderItem> findByOrderIdAndStatusNot(Long orderId, StatusOrderItem statusOrderItem);
}
