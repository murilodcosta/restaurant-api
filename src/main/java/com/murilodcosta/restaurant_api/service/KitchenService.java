package com.murilodcosta.restaurant_api.service;

import com.murilodcosta.restaurant_api.domain.enums.StatusOrderItem;
import com.murilodcosta.restaurant_api.dto.KitchenItemResponse;
import com.murilodcosta.restaurant_api.exception.BusinessRuleException;
import com.murilodcosta.restaurant_api.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitchenService {

    private final OrderItemRepository orderItemRepository;

    public KitchenService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<KitchenItemResponse> listPendingItems() {
        return orderItemRepository.findByStatusOrderByIdAsc(StatusOrderItem.PENDING)
                .stream()
                .map(KitchenItemResponse::fromEntity)
                .toList();
    }

    public List<KitchenItemResponse> listInPreparationItems() {
        return orderItemRepository.findByStatusOrderByIdAsc(StatusOrderItem.IN_PREPARATION)
                .stream()
                .map(KitchenItemResponse::fromEntity)
                .toList();
    }

    public KitchenItemResponse setInPreparation(Long itemId) {
        var orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessRuleException("Order item not found"));

        if (orderItem.getStatus() != StatusOrderItem.PENDING) {
            throw new BusinessRuleException("Only pending items can be initiated for preparation");
        }

        orderItem.setStatus(StatusOrderItem.IN_PREPARATION);

        var savedOrderItem = orderItemRepository.save(orderItem);

        return KitchenItemResponse.fromEntity(savedOrderItem);
    }

    public KitchenItemResponse setReady(Long itemId) {
        var orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessRuleException("Order item not found"));

        if (orderItem.getStatus() != StatusOrderItem.IN_PREPARATION) {
            throw new BusinessRuleException("Only items in preparation can be set as ready");
        }

        orderItem.setStatus(StatusOrderItem.READY);

        var savedOrderItem = orderItemRepository.save(orderItem);

        return KitchenItemResponse.fromEntity(savedOrderItem);
    }

    public KitchenItemResponse setDelivered(Long itemId) {
        var orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessRuleException("Order item not found"));

        if (orderItem.getStatus() != StatusOrderItem.READY) {
            throw new BusinessRuleException("Only ready items can be set as delivered");
        }

        orderItem.setStatus(StatusOrderItem.DELIVERED);

        var savedOrderItem = orderItemRepository.save(orderItem);

        return KitchenItemResponse.fromEntity(savedOrderItem);
    }
}
