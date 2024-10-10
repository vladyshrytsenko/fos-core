package com.example.model.request;

import com.example.model.dto.MealDto;
import com.example.model.entity.Meal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MealRequest {

    private String name;
    private Integer portionWeight;
//    private String lunchName;

    public static Meal toEntity(MealRequest request) {
        return Meal.builder()
            .name(request.getName())
            .portionWeight(request.getPortionWeight())
            .build();
    }
}
