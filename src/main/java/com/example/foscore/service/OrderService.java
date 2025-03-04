package com.example.foscore.service;

import com.example.foscore.exception.EntityNotFoundException;
import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.dto.DrinkDto;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.dto.OrderDto;
import com.example.foscore.model.dto.PaymentDto;
import com.example.foscore.model.entity.Dessert;
import com.example.foscore.model.entity.Drink;
import com.example.foscore.model.entity.Meal;
import com.example.foscore.model.entity.Order;
import com.example.foscore.model.enums.DishType;
import com.example.foscore.model.enums.PaymentStatus;
import com.example.foscore.model.request.OrderRequest;
import com.example.foscore.repository.OrderRepository;
import com.example.foscore.service.rabbitmq.PopularDishesProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Transactional
    public OrderDto create(long userId, OrderRequest request) {
        Order orderToSave = OrderRequest.toEntity(request);
        orderToSave.setCreatedBy(userId);

        AtomicReference<Float> totalPrice = new AtomicReference<>(0f);

        List<Meal> meals = this.getMeals(request.getMeals(), totalPrice);
        List<Dessert> desserts = this.getDesserts(request.getDesserts(), totalPrice);
        List<Drink> drinks = this.getDrinks(request.getDrinks(), totalPrice);

        orderToSave.setMeals(meals);
        orderToSave.setDesserts(desserts);
        orderToSave.setDrinks(drinks);
        orderToSave.setTotalPrice(totalPrice.get());

        PaymentDto payment = PaymentDto.builder()
            .status(PaymentStatus.PENDING.name())
            .totalPrice(totalPrice.get())
            .build();
        orderToSave.setPayment(PaymentDto.toEntity(payment));

        Order createdOrder = this.orderRepository.save(orderToSave);
        return OrderDto.toDto(createdOrder);
    }

    public OrderDto getById(Long id) {
        Order orderById = this.orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Order.class));

        return OrderDto.toDto(orderById);
    }

    public Page<OrderDto> findAll(Pageable pageable) {
        Page<Order> orderPage = this.orderRepository.findAll(pageable);
        return orderPage.map(OrderDto::toDto);
    }

    public List<Order> getOrdersForCurrentMonth(Long createdBy) {
        LocalDateTime startOfMonth = LocalDateTime.now()
            .withDayOfMonth(1).toLocalDate()
            .atStartOfDay();

        return this.orderRepository.findAllByCreatedByAndDateRange(
            createdBy,
            startOfMonth,
            LocalDateTime.now()
        );
    }

    public long getOrdersCountForCurrentMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now()
            .withDayOfMonth(1).toLocalDate()
            .atStartOfDay();

        return this.orderRepository.countAllByDateRange(
            startOfMonth,
            LocalDateTime.now()
        );
    }

    public void deleteById(Long id) {
        this.orderRepository.deleteById(id);
    }

    private List<Meal> getMeals(Set<MealDto> meals, AtomicReference<Float> totalPrice) {
        return meals.stream()
            .map(mealDto -> {
                Meal meal = this.mealService.getEntityByName(mealDto.getName());
                this.popularDishesProducer.sendOrderEvent(DishType.MEAL, meal.getName());
                totalPrice.updateAndGet(v -> v + meal.getPrice());
                return meal;
            })
            .collect(Collectors.toList());
    }

    private List<Dessert> getDesserts(Set<DessertDto> desserts, AtomicReference<Float> totalPrice) {
        return desserts.stream()
            .map(dessertDto -> {
                Dessert dessert = this.dessertService.getEntityByName(dessertDto.getName());
                this.popularDishesProducer.sendOrderEvent(DishType.DESSERT, dessert.getName());
                totalPrice.updateAndGet(v -> v + dessert.getPrice());
                return dessert;
            })
            .collect(Collectors.toList());
    }

    private List<Drink> getDrinks(Set<DrinkDto> drinks, AtomicReference<Float> totalPrice) {
        return drinks.stream()
            .map(drinkDto -> {
                Drink drink = this.drinkService.getEntityByName(drinkDto.getName());
                totalPrice.updateAndGet(v -> v + drink.getPrice());
                return drink;
            })
            .collect(Collectors.toList());
    }

    private final OrderRepository orderRepository;
    private final MealService mealService;
    private final DessertService dessertService;
    private final DrinkService drinkService;
    private final PopularDishesProducer popularDishesProducer;
}
