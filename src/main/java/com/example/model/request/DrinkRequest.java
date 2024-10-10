package com.example.model.request;

import com.example.model.entity.Drink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DrinkRequest {

    private String name;
    private Float price;
    private Boolean iceCubes;
    private Boolean lemon;

    public static Drink toEntity(DrinkRequest request) {
        return Drink.builder()
            .name(request.getName())
            .price(request.getPrice())
            .iceCubes(request.getIceCubes())
            .lemon(request.getLemon())
            .build();
    }
}
