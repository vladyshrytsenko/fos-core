package com.example.foscore.service;

import com.example.foscore.model.User;
import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.dto.DrinkDto;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.dto.OrderDto;
import com.example.foscore.model.dto.SubscriptionDto;
import com.example.foscore.model.entity.Order;
import com.example.foscore.model.entity.Subscription;
import com.example.foscore.model.enums.SubscriptionType;
import com.example.foscore.model.request.OrderRequest;
import com.example.foscore.model.request.SubscriptionRequest;
import com.example.foscore.repository.SubscriptionRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    @Transactional
    public SubscriptionDto create(String email, String username, SubscriptionRequest request) {
        Subscription subscriptionToSave = SubscriptionRequest.toEntity(request);

        OrderDto orderById = this.orderService.getById(request.getOrderId());
        Order order = OrderDto.toEntity(orderById);
        subscriptionToSave.setOrder(order);

        String customerId = this.stripeService.createCustomer(email, username);
        subscriptionToSave.setCustomerId(customerId);

        Subscription createdSubscription = this.subscriptionRepository.save(subscriptionToSave);

        List<MealDto> mealDtos = orderById.getMeals();
        List<DessertDto> dessertDtos = orderById.getDesserts();
        List<DrinkDto> drinkDtos = orderById.getDrinks();

        try {
            this.stripeService.attachTestCardToCustomer(customerId);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        mealDtos.forEach(mealDto -> {
            try {
                Price priceByProductName = this.stripeService.getPriceByProductName(mealDto.getName());
                this.stripeService.createSubscription(
                    customerId,
                    priceByProductName.getId(),
                    request.getType()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        dessertDtos.forEach(dessertDto -> {
            try {
                Price priceByProductName = this.stripeService.getPriceByProductName(dessertDto.getName());
                this.stripeService.createSubscription(
                    customerId,
                    priceByProductName.getId(),
                    request.getType()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        drinkDtos.forEach(drinkDto -> {
            try {
                Price priceByProductName = this.stripeService.getPriceByProductName(drinkDto.getName());
                this.stripeService.createSubscription(
                    customerId,
                    priceByProductName.getId(),
                    request.getType()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return SubscriptionDto.toDto(createdSubscription);
    }

    @Scheduled(cron = "${subscription.daily.cron}")
    public void dailySubscription() {
        Optional<Subscription> subscriptionByType = this.subscriptionRepository.findByType(SubscriptionType.DAILY.name());
        if (subscriptionByType.isPresent()) {
            subscriptionByType.ifPresent(this::makeOrder);
        }
    }

    @Scheduled(cron = "${subscription.weekly.cron}")
    public void weeklySubscription() {
        Optional<Subscription> subscriptionByType = this.subscriptionRepository.findByType(SubscriptionType.WEEKLY.name());
        if (subscriptionByType.isPresent()) {
            subscriptionByType.ifPresent(this::makeOrder);
        }
    }

    @Scheduled(cron = "${subscription.monthly.cron}")
    public void monthlySubscription() {
        Optional<Subscription> subscriptionByType = this.subscriptionRepository.findByType(SubscriptionType.MONTHLY.name());
        subscriptionByType.ifPresent(this::makeOrder);
    }

    private void makeOrder(Subscription byType) {
        Order order = byType.getOrder();

        OrderRequest request = new OrderRequest();
//        if (order.getMeal() != null) {
//            request.setMealName(order.getMeal().getName());
//        }
//        if (order.getDessert() != null) {
//            request.setDessertName(order.getDessert().getName());
//        }
//        if (order.getDrink() != null) {
//            request.setDrinkName(order.getDrink().getName());
//        }
        request.setLemon(order.getLemon());
        request.setIceCubes(order.getIceCubes());

//        UserDto currentUser = this.userService.getCurrentUser();
//        this.orderService.create(toEntity(currentUser), request);
    }

    private final OrderService orderService;
//    private final UserService userService;
    private final SubscriptionRepository subscriptionRepository;
    private final StripeService stripeService;
}
