package com.example.service;

import com.example.exception.EntityNotFoundException;
import com.example.model.dto.OrderDto;
import com.example.model.entity.Dessert;
import com.example.model.entity.Drink;
import com.example.model.entity.Meal;
import com.example.model.entity.Order;
import com.example.model.request.OrderRequest;
import com.example.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MealService mealService;
    private final DessertService dessertService;
    private final DrinkService drinkService;

    @Transactional
    public OrderDto create(OrderRequest request) {
        Order entity = OrderRequest.toEntity(request);

        if (isBlank(request.getMealName()) && isBlank(request.getDessertName())) {
            throw new RuntimeException("Lunch should not be blank");
        }

        Meal mealByName = this.mealService.getByName(request.getMealName());
        entity.setMeal(mealByName);

        Dessert dessertByName = this.dessertService.getByName(request.getDessertName());
        entity.setDessert(dessertByName);

        Drink drinkByName = this.drinkService.getByName(request.getDrinkName());
        entity.setDrink(drinkByName);

        float totalPrice = drinkByName.getPrice() + mealByName.getPrice() + dessertByName.getPrice();
        entity.setTotalPrice(totalPrice);

        entity.setCreatedAt(LocalDateTime.now());
        Order saved = this.orderRepository.save(entity);
        return OrderDto.toDto(saved);
    }

    public OrderDto getById(Long id) {
        Order found = this.orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Order.class));

        return OrderDto.toDto(found);
    }

    public Page<Order> findAll(Pageable pageable) {
        return this.orderRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        this.orderRepository.deleteById(id);
    }
}
