package com.example.foscore.service;

import com.example.foscore.exception.EntityNotFoundException;
import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.dto.OrderDto;
import com.example.foscore.model.dto.PaymentDto;
import com.example.foscore.model.entity.Drink;
import com.example.foscore.model.entity.Order;
import com.example.foscore.model.entity.Payment;
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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Transactional
    public OrderDto create(long userId, OrderRequest request) {
        Order orderToSave = OrderRequest.toEntity(request);

        if (isBlank(request.getMealName()) && isBlank(request.getDessertName()) && isBlank(request.getDrinkName())) {
            throw new RuntimeException("Lunch should not be blank");
        }

        float totalPrice = 0f;
        if (isNotBlank(request.getMealName())) {
            MealDto mealByName = this.mealService.getByName(request.getMealName());
            orderToSave.setMeal(MealDto.toEntity(mealByName));
            this.popularDishesProducer.sendOrderEvent(DishType.MEAL, mealByName.getName());

            totalPrice += mealByName.getPrice();
        }

        if (isNotBlank(request.getDessertName())) {
            DessertDto dessertByName = this.dessertService.getByName(request.getDessertName());
            orderToSave.setDessert(DessertDto.toEntity(dessertByName));
            this.popularDishesProducer.sendOrderEvent(DishType.DESSERT, dessertByName.getName());

            totalPrice += dessertByName.getPrice();
        }

        if (isNotBlank(request.getDrinkName())) {
            Drink drinkByName = this.drinkService.getByName(request.getDrinkName());
            orderToSave.setDrink(drinkByName);

            totalPrice += drinkByName.getPrice();
        }

        orderToSave.setTotalPrice(totalPrice);

        PaymentDto payment = PaymentDto.builder()
            .status(PaymentStatus.PENDING.name())
            .totalPrice(totalPrice)
            .build();

        Payment paymentEntity = PaymentDto.toEntity(payment);
        orderToSave.setPayment(paymentEntity);
        orderToSave.setCreatedBy(userId);

        Order createdOrder = this.orderRepository.save(orderToSave);
        return OrderDto.toDto(createdOrder);
    }

    public OrderDto getById(Long id) {
        Order orderById = this.orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Order.class));

        return OrderDto.toDto(orderById);
    }

    public Page<Order> findAll(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
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

    private final OrderRepository orderRepository;
    private final MealService mealService;
    private final DessertService dessertService;
    private final DrinkService drinkService;
    private final PopularDishesProducer popularDishesProducer;
}
