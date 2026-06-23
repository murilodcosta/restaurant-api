package com.murilodcosta.restaurant_api.service;

import com.murilodcosta.restaurant_api.domain.entity.Bill;
import com.murilodcosta.restaurant_api.domain.entity.Order;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrder;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrderItem;
import com.murilodcosta.restaurant_api.dto.BillRequest;
import com.murilodcosta.restaurant_api.dto.BillResponse;
import com.murilodcosta.restaurant_api.exception.BusinessRuleException;
import com.murilodcosta.restaurant_api.repository.BillRepository;
import com.murilodcosta.restaurant_api.repository.OrderItemRepository;
import com.murilodcosta.restaurant_api.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BillService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BillRepository billRepository;

    public BillService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, BillRepository billRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.billRepository = billRepository;
    }

    public BillResponse setBillClosed(Long orderId, BillRequest billRequest) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessRuleException("Order not found"));

        if (order.getStatus() == StatusOrder.CLOSED) {
            throw new BusinessRuleException("Order is already closed");
        }

        if (order.getStatus() == StatusOrder.CANCELED) {
            throw new BusinessRuleException("Order is canceled and cannot be billed");
        }

        if (billRepository.existsByOrderId(orderId)) {
            throw new BusinessRuleException("Bill already exists for this order");
        }

        var orderItems = orderItemRepository.findByOrderId(orderId);

        if (orderItems.isEmpty()) {
            throw new BusinessRuleException("No items in the order to bill");
        }

        var orderItemsNotDelivered = orderItemRepository.findByOrderIdAndStatusNot(orderId, StatusOrderItem.DELIVERED);

        if (!orderItemsNotDelivered.isEmpty()) {
            throw new BusinessRuleException("There are items in the order that are not delivered yet");
        }

        var subtotal = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var serviceFee = billRequest.serviceFee() != null ? billRequest.serviceFee() : BigDecimal.ZERO;
        var discount = billRequest.discount() != null ? billRequest.discount() : BigDecimal.ZERO;

        if (serviceFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Service fee cannot be negative");
        }

        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Discount cannot be negative");
        }

        var total = subtotal.add(serviceFee).subtract(discount);

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Total cannot be negative");
        }

        var bill = new Bill();
        bill.setOrder(order);
        bill.setSubtotal(subtotal);
        bill.setServiceFee(serviceFee);
        bill.setDiscount(discount);
        bill.setTotal(total);
        bill.setClosedAt(LocalDateTime.now());
        order.setStatus(StatusOrder.CLOSED);
        order.setClosedAt(LocalDateTime.now());

        var savedBill = billRepository.save(bill);
        orderRepository.save(order);

        return BillResponse.fromEntity(savedBill);
    }

    public BillResponse findByOrder(Long orderId) {
        var bill = billRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessRuleException("Bill not found for this order"));

        return BillResponse.fromEntity(bill);
    }
}
