package com.example.model.dto;

import com.example.model.entity.Subscription;
import com.example.model.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SubscriptionDto {

    private Long id;
    private String type;
    private Long userId;
    private String customerId;
    private Long orderId;

    public static SubscriptionDto toDto(Subscription entity) {
        return SubscriptionDto.builder()
            .id(entity.getId())
            .type(entity.getType().name())
            .customerId(entity.getCustomerId())
            .orderId(entity.getOrder().getId())
            .build();
    }

    public static Subscription toEntity(SubscriptionDto dto) {
        return Subscription.builder()
            .id(dto.getId())
            .customerId(dto.getCustomerId())
            .type(SubscriptionType.valueOf(dto.getType()))
            .build();
    }
}
