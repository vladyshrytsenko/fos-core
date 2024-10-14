package com.example.model.request;

import com.example.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private String mealName;
    private String dessertName;
    private String drinkName;
    private Boolean iceCubes;
    private Boolean lemon;

    public static Order toEntity(OrderRequest request) {
        return Order.builder()
            .iceCubes(request.getIceCubes())
            .lemon(request.getLemon())
            .build();
    }
}
