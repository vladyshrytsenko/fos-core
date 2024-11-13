package com.example.model.dto;

import com.example.model.entity.Meal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class MealDto {

    private Long id;
    private String name;
    private Float price;
    private Integer portionWeight;
    private Long cuisineId;
    private String stripeCustomerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

    public static MealDto toDto(Meal entity) {
        MealDto mealDto = MealDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .price(entity.getPrice())
            .portionWeight(entity.getPortionWeight())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .isDeleted(entity.isDeleted())
            .deletedAt(entity.getDeletedAt())
            .build();

        if (entity.getCuisine() != null) {
            mealDto.setCuisineId(entity.getCuisine().getId());
        }

        return mealDto;
    }

    public static Meal toEntity(MealDto dto) {
        return Meal.builder()
            .id(dto.getId())
            .name(dto.getName())
            .price(dto.getPrice())
            .portionWeight(dto.getPortionWeight())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .isDeleted(dto.isDeleted())
            .deletedAt(dto.getDeletedAt())
            .build();
    }

    public static List<MealDto> toDtoList(List<Meal> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream()
            .map(MealDto::toDto)
            .collect(Collectors.toList());
    }
}
