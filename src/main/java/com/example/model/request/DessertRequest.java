package com.example.model.request;

import com.example.model.entity.Dessert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DessertRequest {

    private String name;
    private Integer portionWeight;
//    private String lunchName;

    public static Dessert toEntity(DessertRequest request) {
        return Dessert.builder()
            .name(request.getName())
            .portionWeight(request.getPortionWeight())
            .build();
    }
}
