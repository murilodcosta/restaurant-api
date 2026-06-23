package com.murilodcosta.restaurant_api.repository;

import com.murilodcosta.restaurant_api.domain.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    boolean existsByOrderId(Long orderId);

    Optional<Bill> findByOrderId(Long orderId);
}
