package com.murilodcosta.restaurant_api.controller;

import com.murilodcosta.restaurant_api.dto.OrderRequest;
import com.murilodcosta.restaurant_api.dto.OrderResponse;
import com.murilodcosta.restaurant_api.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody OrderRequest orderRequest) {
        return orderService.create(orderRequest);
    }

    @GetMapping
    public Page<OrderResponse> listAll(Pageable pageable) {
        return orderService.listAll(pageable);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable Long id) {
        return orderService.findById(id);
    }
}
