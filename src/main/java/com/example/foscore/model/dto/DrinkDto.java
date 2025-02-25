package com.example.foscore.model.dto;

import com.example.foscore.model.entity.Drink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class DrinkDto {

    private Long id;
    private String name;
    private Float price;
    private String stripeCustomerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

    public static DrinkDto toDto(Drink entity) {
        return DrinkDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .price(entity.getPrice())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .isDeleted(entity.isDeleted())
            .deletedAt(entity.getDeletedAt())
            .build();
    }

    public static Drink toEntity(DrinkDto dto) {
        return Drink.builder()
            .id(dto.getId())
            .name(dto.getName())
            .price(dto.getPrice())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .isDeleted(dto.isDeleted())
            .deletedAt(dto.getDeletedAt())
            .build();
    }

    public static List<DrinkDto> toDtoList(List<Drink> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream()
            .map(DrinkDto::toDto)
            .collect(Collectors.toList());
    }

    public static List<Drink> toEntityList(List<DrinkDto> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream()
            .map(DrinkDto::toEntity)
            .collect(Collectors.toList());
    }
}
