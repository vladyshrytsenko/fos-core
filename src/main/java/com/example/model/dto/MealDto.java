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
    private Integer portionWeight;
    private Long lunchId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;

    public static MealDto toDto(Meal entity) {
        MealDto mealDto = MealDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .portionWeight(entity.getPortionWeight())
            .lunchId(entity.getLunch().getId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .isDeleted(entity.isDeleted())
            .deletedAt(entity.getDeletedAt())
            .build();

        if (entity.getLunch() != null) {
            mealDto.setLunchId(entity.getLunch().getId());
        }

        return mealDto;
    }

    public static Meal toEntity(MealDto dto) {
        return Meal.builder()
            .id(dto.getId())
            .name(dto.getName())
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
