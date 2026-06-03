package com.murilodcosta.restaurant_api.repository;

import com.murilodcosta.restaurant_api.domain.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
