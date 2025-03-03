package com.example.foscore.model.request;

import com.example.foscore.model.entity.Drink;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrinkRequest {

    @NotBlank(message = "'name' should not be null")
    @Size(min = 4, max = 32, message = "invalid 'name' size")
    private String name;

    @NotNull(message = "'price' should not be null")
    private Float price;

    public static Drink toEntity(DrinkRequest request) {
        return Drink.builder()
            .name(request.getName())
            .price(request.getPrice())
            .build();
    }
}
