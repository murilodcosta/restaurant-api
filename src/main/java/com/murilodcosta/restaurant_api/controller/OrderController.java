package com.murilodcosta.restaurant_api.controller;

import com.murilodcosta.restaurant_api.dto.OrderItemRequest;
import com.murilodcosta.restaurant_api.dto.OrderItemResponse;
import com.murilodcosta.restaurant_api.dto.OrderRequest;
import com.murilodcosta.restaurant_api.dto.OrderResponse;
import com.murilodcosta.restaurant_api.service.OrderService;
import com.murilodcosta.restaurant_api.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
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

    @PostMapping("/{orderId}/items")
    public OrderItemResponse addItem(@PathVariable Long orderId, @RequestBody OrderItemRequest orderItemRequest) {
        return orderService.addItem(orderId, orderItemRequest);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemResponse> listItems(@PathVariable Long orderId) {
        return orderService.listItems(orderId);
    }

    @PostMapping("{orderId}/pay")
    public void payOrder(@PathVariable Long orderId, @RequestParam String paymentMethod) {
        paymentService.processPayment(orderId, paymentMethod);
    }
}
