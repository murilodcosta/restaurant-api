package com.murilodcosta.restaurant_api.domain.entity;

import com.murilodcosta.restaurant_api.domain.enums.StatusOrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    private String notes;

    @Enumerated(EnumType.STRING)
    private StatusOrderItem status = StatusOrderItem.PENDING;

    @Column(name = "preparation_start_date")
    private LocalDateTime preparationStartDate;

    @Column(name = "preparation_end_date")
    private LocalDateTime preparationEndDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
}
