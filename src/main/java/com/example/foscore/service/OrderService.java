package com.example.foscore.service;

import com.example.foscore.exception.EntityNotFoundException;
import com.example.foscore.model.dto.OrderDto;
import com.example.foscore.model.dto.PaymentDto;
import com.example.foscore.model.entity.Dessert;
import com.example.foscore.model.entity.Drink;
import com.example.foscore.model.entity.Meal;
import com.example.foscore.model.entity.Order;
import com.example.foscore.model.entity.Payment;
import com.example.foscore.model.enums.PaymentStatus;
import com.example.foscore.model.request.OrderRequest;
import com.example.foscore.repository.OrderRepository;
import com.example.foscore.service.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MealService mealService;
    private final DessertService dessertService;
    private final DrinkService drinkService;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public OrderDto create(OrderRequest request) {
        Order orderToSave = OrderRequest.toEntity(request);

        if (isBlank(request.getMealName()) && isBlank(request.getDessertName())) {
            throw new RuntimeException("Lunch should not be blank");
        }

        float totalPrice = 0f;
        if (isNotBlank(request.getMealName())) {
            Meal mealByName = this.mealService.getByName(request.getMealName());
            orderToSave.setMeal(mealByName);
            this.kafkaProducerService.sendOrderEvent(request.getMealName());

            totalPrice += mealByName.getPrice();
        }

        if (isNotBlank(request.getDessertName())) {
            Dessert dessertByName = this.dessertService.getByName(request.getDessertName());
            orderToSave.setDessert(dessertByName);
            this.kafkaProducerService.sendOrderEvent(request.getDessertName());

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

    public void deleteById(Long id) {
        this.orderRepository.deleteById(id);
    }
}
