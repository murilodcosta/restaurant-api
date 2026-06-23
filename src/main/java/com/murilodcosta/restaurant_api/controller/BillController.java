package com.murilodcosta.restaurant_api.controller;

import com.murilodcosta.restaurant_api.dto.BillRequest;
import com.murilodcosta.restaurant_api.dto.BillResponse;
import com.murilodcosta.restaurant_api.service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders/{orderId}/bill")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BillResponse setBillClosed(@PathVariable Long orderId, @RequestBody BillRequest billRequest) {
        return billService.setBillClosed(orderId, billRequest);
    }

    @GetMapping
    public BillResponse setBillClosed(@PathVariable Long orderId) {
        return billService.findByOrder(orderId);
    }
}
