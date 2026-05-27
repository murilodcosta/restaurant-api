package com.murilodcosta.restaurant_api.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private BigDecimal subtotal;
    @Column(name = "service_fee")

    private BigDecimal serviceFee;
    private BigDecimal discount;
    private BigDecimal total;
    private LocalDateTime closedAt;

    @PrePersist
    public void prePersist() {
        closedAt = LocalDateTime.now();
    }
}
