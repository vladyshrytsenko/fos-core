package com.example.service;

import com.example.model.dto.OrderDto;
import com.example.model.dto.SubscriptionDto;
import com.example.model.dto.UserDto;
import com.example.model.entity.Order;
import com.example.model.entity.Subscription;
import com.example.model.entity.User;
import com.example.model.enums.SubscriptionType;
import com.example.model.request.OrderRequest;
import com.example.model.request.SubscriptionRequest;
import com.example.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final OrderService orderService;
    private final UserService userService;
    private final SubscriptionRepository subscriptionRepository;

    @Value("${subscription.daily.cron}")
    private String dailyCron;

    @Value("${subscription.weekly.cron}")
    private String weeklyCron;

    @Value("${subscription.monthly.cron}")
    private String monthlyCron;

    @Transactional
    public SubscriptionDto create(Long orderId, SubscriptionRequest request) {
        Subscription entity = SubscriptionRequest.toEntity(request);

        UserDto currentUser = this.userService.getCurrentUser();
        User user = UserDto.toEntity(currentUser);
        entity.setUser(user);

        OrderDto orderById = this.orderService.getById(orderId);
        Order order = OrderDto.toEntity(orderById);
        entity.setOrder(order);

        Subscription saved = this.subscriptionRepository.save(entity);
        return SubscriptionDto.toDto(saved);
    }

    @Scheduled(cron = "#{@lunchSubscriptionService.dailyCron}")
    public void dailySubscription() {
        Optional<Subscription> byType = this.subscriptionRepository.findByType(SubscriptionType.DAILY.name());
        if (byType.isPresent()) {
            byType.ifPresent(this::makeOrder);
        }
    }

    @Scheduled(cron = "#{@lunchSubscriptionService.weeklyCron}")
    public void weeklySubscription() {
        Optional<Subscription> byType = this.subscriptionRepository.findByType(SubscriptionType.WEEKLY.name());
        if (byType.isPresent()) {
            byType.ifPresent(this::makeOrder);
        }
    }

    @Scheduled(cron = "#{@lunchSubscriptionService.monthlyCron}")
    public void monthlySubscription() {
        Optional<Subscription> byType = this.subscriptionRepository.findByType(SubscriptionType.MONTHLY.name());
        byType.ifPresent(this::makeOrder);
    }

    private void makeOrder(Subscription byType) {
        Order order = byType.getOrder();

        OrderRequest request = new OrderRequest();
        if (order.getMeal() != null) {
            request.setMealName(order.getMeal().getName());
        }
        if (order.getDessert() != null) {
            request.setDessertName(order.getDessert().getName());
        }
        if (order.getDrink() != null) {
            request.setDrinkName(order.getDrink().getName());
        }
        request.setLemon(order.getLemon());
        request.setIceCubes(order.getIceCubes());
        this.orderService.create(request);
    }
}
