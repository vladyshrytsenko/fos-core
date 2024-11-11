package com.example.model.request;

import com.example.model.entity.Subscription;
import com.example.model.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionRequest {

    private String type;
    private Long userId;
    private Long orderId;

    public static Subscription toEntity(SubscriptionRequest request) {
        return Subscription.builder()
            .type(SubscriptionType.valueOf(request.getType()))
            .build();
    }
}
