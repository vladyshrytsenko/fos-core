package com.example.model.request;

import com.example.model.entity.Drink;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "'price' should be greater than 0")
    private Float price;

    public static Drink toEntity(DrinkRequest request) {
        return Drink.builder()
            .name(request.getName())
            .price(request.getPrice())
            .build();
    }
}
