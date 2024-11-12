package com.example.service;

import com.example.model.dto.DessertDto;
import com.example.model.dto.DrinkDto;
import com.example.model.dto.MealDto;
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
    private final StripeService stripeService;

    @Transactional
    public SubscriptionDto create(SubscriptionRequest request) {
        Subscription entity = SubscriptionRequest.toEntity(request);

        UserDto currentUser = this.userService.getCurrentUser();
        User user = UserDto.toEntity(currentUser);
        entity.setUser(user);

        OrderDto orderById = this.orderService.getById(request.getOrderId());
        Order order = OrderDto.toEntity(orderById);
        entity.setOrder(order);

        String customerId = this.stripeService.createCustomer(user.getEmail(), user.getUsername());
        entity.setCustomerId(customerId);

        Subscription saved = this.subscriptionRepository.save(entity);

        MealDto mealDto = orderById.getMeal();
        DessertDto dessertDto = orderById.getDessert();
        DrinkDto drinkDto = orderById.getDrink();
        if (mealDto != null) {
            try {
                this.stripeService.createSubscription(customerId, mealDto.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (dessertDto != null) {
            try {
                this.stripeService.createSubscription(customerId, dessertDto.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (drinkDto != null) {
            try {
                this.stripeService.createSubscription(customerId, drinkDto.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return SubscriptionDto.toDto(saved);
    }

    @Scheduled(cron = "${subscription.daily.cron}")
    public void dailySubscription() {
        Optional<Subscription> byType = this.subscriptionRepository.findByType(SubscriptionType.DAILY.name());
        if (byType.isPresent()) {
            byType.ifPresent(this::makeOrder);
        }
    }

    @Scheduled(cron = "${subscription.weekly.cron}")
    public void weeklySubscription() {
        Optional<Subscription> byType = this.subscriptionRepository.findByType(SubscriptionType.WEEKLY.name());
        if (byType.isPresent()) {
            byType.ifPresent(this::makeOrder);
        }
    }

    @Scheduled(cron = "${subscription.monthly.cron}")
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
