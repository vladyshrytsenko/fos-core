package com.example.model.request;

import com.example.model.entity.Meal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MealRequest {

    private String name;
    private Float price;
    private Integer portionWeight;
    private String cuisineName;

    public static Meal toEntity(MealRequest request) {
        return Meal.builder()
            .name(request.getName())
            .price(request.getPrice())
            .portionWeight(request.getPortionWeight())
            .build();
    }
}
