package com.example.model.request;

import com.example.model.entity.Dessert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DessertRequest {

    private String name;
    private Float price;
    private Integer portionWeight;
    private String cuisineName;

    public static Dessert toEntity(DessertRequest request) {
        return Dessert.builder()
            .name(request.getName())
            .price(request.getPrice())
            .portionWeight(request.getPortionWeight())
            .build();
    }
}
