package com.example.model.request;

import com.example.model.entity.Lunch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LunchRequest {

    private String name;
    private Float price;
    private String cuisineName;
    private String mealName;
    private String dessertName;

    public static Lunch toEntity(LunchRequest request) {
        return Lunch.builder()
            .name(request.getName())
            .price(request.getPrice())
            .build();
    }
}
