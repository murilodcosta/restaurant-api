package com.murilodcosta.restaurant_api.repository;

import com.murilodcosta.restaurant_api.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
