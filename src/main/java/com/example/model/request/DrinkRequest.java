package com.example.model.request;

import com.example.model.entity.Drink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrinkRequest {

    private String name;
    private Float price;

    public static Drink toEntity(DrinkRequest request) {
        return Drink.builder()
            .name(request.getName())
            .price(request.getPrice())
            .build();
    }
}
