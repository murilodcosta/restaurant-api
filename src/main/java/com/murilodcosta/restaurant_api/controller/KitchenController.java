package com.murilodcosta.restaurant_api.controller;

import com.murilodcosta.restaurant_api.dto.KitchenItemResponse;
import com.murilodcosta.restaurant_api.service.KitchenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kitchen")
public class KitchenController {

    private final KitchenService kitchenService;

    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @GetMapping("/pending-items")
    public List<KitchenItemResponse> listPendingItems() {
        return kitchenService.listPendingItems();
    }

    @GetMapping("/in-preparation-items")
    public List<KitchenItemResponse> listInPreparationItems() {
        return kitchenService.listInPreparationItems();
    }

    @PatchMapping("/initiate-preparation/{itemId}")
    public KitchenItemResponse setInPreparation(@PathVariable  Long itemId) {
        return kitchenService.setInPreparation(itemId);
    }

    @PatchMapping("/set-ready/{itemId}")
    public KitchenItemResponse setReady(@PathVariable Long itemId) {
        return kitchenService.setReady(itemId);
    }

    @PatchMapping("/set-delivered/{itemId}")
    public KitchenItemResponse setDelivered(@PathVariable Long itemId) {
        return kitchenService.setDelivered(itemId);
    }


}
