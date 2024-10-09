package com.example.model.dto;

import com.example.model.entity.Meal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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

    public static MealDto toDto(Meal entity) {
        return MealDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .portionWeight(entity.getPortionWeight())
            .lunchId(entity.getLunch().getId())
            .build();
    }

    public static Meal toEntity(MealDto dto) {
        return Meal.builder()
            .id(dto.getId())
            .name(dto.getName())
            .portionWeight(dto.getPortionWeight())
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
