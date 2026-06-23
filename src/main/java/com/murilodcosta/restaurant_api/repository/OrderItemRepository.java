package com.murilodcosta.restaurant_api.repository;

import com.murilodcosta.restaurant_api.domain.entity.OrderItem;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findByStatusOrderByIdAsc(StatusOrderItem statusOrderItem);

    List<OrderItem> findByOrderIdAndStatusNot(Long orderId, StatusOrderItem statusOrderItem);

    @Query(
            """
            SELECT oi 
            FROM OrderItem oi
            JOIN FETCH oi.product p
            JOIN FETCH oi.order o
            JOIN FETCH o.restaurantTable rt
            WHERE oi.status = :statusOrderItem
            ORDER BY oi.id ASC
            """
    )
    List<OrderItem> findItemsWithProductsAndOrder(StatusOrderItem statusOrderItem);

}
