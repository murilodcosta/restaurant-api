package com.murilodcosta.restaurant_api.client;

import com.murilodcosta.restaurant_api.dto.PaymentRequest;
import com.murilodcosta.restaurant_api.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-client", url = "${payment.api.url}")
public interface PaymentClient {

    @PostMapping("/payments/process")
    PaymentResponse processPayment(PaymentRequest paymentRequest);

}
