package com.example.foscore.model.request;

import com.example.foscore.model.entity.Subscription;
import com.example.foscore.model.enums.SubscriptionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionRequest {

    @NotBlank(message = "'type' should not be blank")
    private String type;

    @Positive(message = "'userId' should be greater than 0")
    private Long userId;

    @Positive(message = "'orderId' should be greater than 0")
    private Long orderId;

    public static Subscription toEntity(SubscriptionRequest request) {
        return Subscription.builder()
            .type(SubscriptionType.valueOf(request.getType()))
            .build();
    }
}
