package com.example.foscore.model.dto;

import com.example.foscore.model.entity.Dessert;
import com.example.foscore.model.entity.Drink;
import com.example.foscore.model.entity.Meal;
import com.example.foscore.model.entity.Order;
import com.example.foscore.model.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private Float totalPrice;
    private List<DessertDto> desserts;
    private List<MealDto> meals;
    private List<DrinkDto> drinks;
    private Boolean iceCubes;
    private Boolean lemon;
    private PaymentDto payment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

    public static OrderDto toDto(Order entity) {
        List<DessertDto> dessertDtos = new ArrayList<>();
        if (entity.getDesserts() != null) {
            dessertDtos = DessertDto.toDtoList(entity.getDesserts());
        }
        List<MealDto> mealDtos = new ArrayList<>();
        if (entity.getMeals() != null) {
            mealDtos = MealDto.toDtoList(entity.getMeals());
        }

        List<DrinkDto> drinkDtos = new ArrayList<>();
        if (entity.getDrinks() != null) {
            drinkDtos = DrinkDto.toDtoList(entity.getDrinks());
        }
        PaymentDto paymentDto = PaymentDto.toDto(entity.getPayment());

        return OrderDto.builder()
            .id(entity.getId())
            .totalPrice(entity.getTotalPrice())
            .desserts(dessertDtos)
            .meals(mealDtos)
            .drinks(drinkDtos)
            .payment(paymentDto)
            .iceCubes(entity.getIceCubes())
            .lemon(entity.getLemon())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .isDeleted(entity.isDeleted())
            .deletedAt(entity.getDeletedAt())
            .build();
    }

    public static Order toEntity(OrderDto dto) {
        List<Dessert> desserts = DessertDto.toEntityList(dto.getDesserts());
        List<Meal> meals = MealDto.toEntityList(dto.getMeals());
        List<Drink> drinks = DrinkDto.toEntityList(dto.getDrinks());
        Payment payment = PaymentDto.toEntity(dto.getPayment());

        return Order.builder()
            .id(dto.getId())
            .totalPrice(dto.getTotalPrice())
            .desserts(desserts)
            .meals(meals)
            .drinks(drinks)
            .payment(payment)
            .iceCubes(dto.getIceCubes())
            .lemon(dto.getLemon())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .isDeleted(dto.isDeleted())
            .deletedAt(dto.getDeletedAt())
            .build();
    }

    public static List<OrderDto> toDtoList(List<Order> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream()
            .map(OrderDto::toDto)
            .collect(Collectors.toList());
    }
}
