package com.example.model.request;

import com.example.model.entity.Dessert;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "'price' should be greater than 0")
    private Float price;

    @Positive(message = "'portionWeight' should be greater than 0")
    private Integer portionWeight;

    public static Dessert toEntity(DessertRequest request) {
        return Dessert.builder()
            .name(request.getName())
            .price(request.getPrice())
            .portionWeight(request.getPortionWeight())
            .build();
    }
}
