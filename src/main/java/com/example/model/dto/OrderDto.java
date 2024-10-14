package com.example.model.dto;

import com.example.model.entity.Dessert;
import com.example.model.entity.Drink;
import com.example.model.entity.Meal;
import com.example.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private Float totalPrice;
    private DessertDto dessert;
    private MealDto meal;
    private DrinkDto drink;
    private Boolean iceCubes;
    private Boolean lemon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

    public static OrderDto toDto(Order entity) {
        DessertDto dessertDto = DessertDto.toDto(entity.getDessert());
        MealDto mealDto = MealDto.toDto(entity.getMeal());
        DrinkDto drinkDto = DrinkDto.toDto(entity.getDrink());

        return OrderDto.builder()
            .id(entity.getId())
            .totalPrice(entity.getTotalPrice())
            .dessert(dessertDto)
            .meal(mealDto)
            .drink(drinkDto)
            .iceCubes(entity.getIceCubes())
            .lemon(entity.getLemon())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .isDeleted(entity.isDeleted())
            .deletedAt(entity.getDeletedAt())
            .build();
    }

    public static Order toEntity(OrderDto dto) {
        Dessert dessert = DessertDto.toEntity(dto.getDessert());
        Meal meal = MealDto.toEntity(dto.getMeal());
        Drink drink = DrinkDto.toEntity(dto.getDrink());

        return Order.builder()
            .id(dto.getId())
            .totalPrice(dto.getTotalPrice())
            .dessert(dessert)
            .meal(meal)
            .drink(drink)
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
