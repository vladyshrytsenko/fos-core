package com.example.model.request;

import com.example.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OrderRequest {

    private Float totalPrice;
    private String lunchName;
    private String drinkName;

//    public static Order toEntity(OrderRequest request) {
//        return Order.builder()
//            .totalPrice(request.getTotalPrice())
//            .build();
//    }
}
