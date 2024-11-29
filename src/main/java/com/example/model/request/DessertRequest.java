package com.example.model.request;

import com.example.model.entity.Dessert;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DessertRequest {

    @NotNull(message = "'name' should not be null")
    @Size(min = 4, max = 32, message = "invalid 'name' size")
    private String name;

    @NotNull(message = "'price' should not be null")
    @Positive(message = "'price' should be greater than 0")
    private Float price;

    @NotNull(message = "'portionWeight' should not be null")
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
