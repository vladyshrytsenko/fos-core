package com.example.foscore.model.request;

import com.example.foscore.model.dto.DessertDto;
import com.example.foscore.model.dto.DrinkDto;
import com.example.foscore.model.dto.MealDto;
import com.example.foscore.model.entity.Order;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private Set<MealDto> meals;
    private Set<DessertDto> desserts;
    private Set<DrinkDto> drinks;
    private Boolean iceCubes;
    private Boolean lemon;

    @AssertTrue(message = "At least one of the fields must be filled")
    public boolean isAtLeastOneFieldFilled() {
        return (meals != null && !meals.isEmpty()) ||
               (desserts != null && !desserts.isEmpty()) ||
               (drinks != null && !drinks.isEmpty());
    }

    public static Order toEntity(OrderRequest request) {
        return Order.builder()
            .iceCubes(request.getIceCubes())
            .lemon(request.getLemon())
            .build();
    }
}
