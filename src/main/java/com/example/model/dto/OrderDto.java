package com.example.model.dto;

import com.example.model.entity.Drink;
import com.example.model.entity.Lunch;
import com.example.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private Float totalPrice;
    private LunchDto lunch;
    private DrinkDto drink;

    public static OrderDto toDto(Order entity) {
        LunchDto lunchDto = LunchDto.toDto(entity.getLunch());
        DrinkDto drinkDto = DrinkDto.toDto(entity.getDrink());

        return OrderDto.builder()
            .id(entity.getId())
            .totalPrice(entity.getTotalPrice())
            .lunch(lunchDto)
            .drink(drinkDto)
            .build();
    }

    public static Order toEntity(OrderDto dto) {
        Lunch lunch = LunchDto.toEntity(dto.getLunch());
        Drink drink = DrinkDto.toEntity(dto.getDrink());

        return Order.builder()
            .id(dto.getId())
            .totalPrice(dto.getTotalPrice())
            .lunch(lunch)
            .drink(drink)
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
