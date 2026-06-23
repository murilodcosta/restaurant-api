package com.murilodcosta.restaurant_api.service;

import com.murilodcosta.restaurant_api.client.PaymentClient;
import com.murilodcosta.restaurant_api.domain.entity.Bill;
import com.murilodcosta.restaurant_api.domain.entity.Order;
import com.murilodcosta.restaurant_api.domain.entity.Payment;
import com.murilodcosta.restaurant_api.domain.entity.RestaurantTable;
import com.murilodcosta.restaurant_api.domain.enums.PaymentMethod;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrder;
import com.murilodcosta.restaurant_api.domain.enums.StatusPayment;
import com.murilodcosta.restaurant_api.domain.enums.StatusRestaurantTable;
import com.murilodcosta.restaurant_api.dto.PaymentRequest;
import com.murilodcosta.restaurant_api.dto.PaymentResponse;
import com.murilodcosta.restaurant_api.exception.BusinessRuleException;
import com.murilodcosta.restaurant_api.repository.BillRepository;
import com.murilodcosta.restaurant_api.repository.OrderRepository;
import com.murilodcosta.restaurant_api.repository.PaymentRepository;
import com.murilodcosta.restaurant_api.repository.RestaurantTableRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentClient paymentClient;
    private final BillRepository billRepository;
    private final OrderRepository orderRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentClient paymentClient, BillRepository billRepository, OrderRepository orderRepository, RestaurantTableRepository restaurantTableRepository, PaymentRepository paymentRepository) {
        this.paymentClient = paymentClient;
        this.billRepository = billRepository;
        this.orderRepository = orderRepository;
        this.restaurantTableRepository = restaurantTableRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void processPayment(Long orderId, String paymentMethod) {
        Bill bill = billRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessRuleException("Bill not found for order ID: " + orderId));

        PaymentResponse paymentResponse = paymentClient.processPayment(
                new PaymentRequest(
                        bill.getTotal(),
                        paymentMethod
                )
        );

        if ("SUCCESS".equals(paymentResponse.status())) {
            Order order = bill.getOrder();
            order.setStatus(StatusOrder.CLOSED);

            RestaurantTable restaurantTable = order.getRestaurantTable();
            restaurantTable.setStatus(StatusRestaurantTable.AVAILABLE);

            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
            payment.setStatus(StatusPayment.APPROVED);
            payment.setAmount(bill.getTotal());
            payment.setPaymentDate(bill.getClosedAt());

            orderRepository.save(order);
            restaurantTableRepository.save(restaurantTable);
            paymentRepository.save(payment);
        } else {
            throw new BusinessRuleException("Payment failed for order ID: " + orderId);
        }
    }
}
