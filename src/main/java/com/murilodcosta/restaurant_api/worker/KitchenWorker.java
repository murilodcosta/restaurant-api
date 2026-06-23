package com.murilodcosta.restaurant_api.worker;

import com.murilodcosta.restaurant_api.domain.entity.OrderItem;
import com.murilodcosta.restaurant_api.domain.enums.StatusOrderItem;
import com.murilodcosta.restaurant_api.repository.OrderItemRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class KitchenWorker {

    private final OrderItemRepository orderItemRepository;

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public KitchenWorker(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void checkOverdueItems() {
        List<OrderItem> inPreparationItems = orderItemRepository.findItemsWithProductsAndOrder(StatusOrderItem.IN_PREPARATION);

        for (OrderItem orderItem : inPreparationItems) {
            executorService.submit(() -> checkItem(orderItem));
        }
    }

    private void checkItem(OrderItem orderItem) {
        if (orderItem.getPreparationStartDate() == null) {
            return;
        }

        Integer preparationTime = orderItem.getProduct().getPreparationTime();

        if (preparationTime == null || preparationTime <= 0) {
            return;
        }

        long elapsedTime = Duration.between(orderItem.getPreparationStartDate(), java.time.LocalDateTime.now()).toMinutes();

        if (elapsedTime > preparationTime) {
            System.out.println(
                    """
                    [ALERT KITCHEN]
                    Item is overdue!
                    Order: %d,
                    Table: %d,
                    Product: %s,
                    Preparation time: %d minutes,
                    Elapsed time: %d minutes.
                    """.formatted(
                            orderItem.getOrder().getId(),
                            orderItem.getOrder().getRestaurantTable().getId(),
                            orderItem.getProduct().getName(),
                            preparationTime,
                            elapsedTime
                    )
            );
        }
    }
}
