package com.murilodcosta.restaurant_api.domain.entity;

import com.murilodcosta.restaurant_api.domain.enums.StatusRestaurantTable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant_tables")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;
    private String description;
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private StatusRestaurantTable status = StatusRestaurantTable.AVAILABLE;
}
