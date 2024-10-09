package com.example.model.dto;

import com.example.model.entity.Lunch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class LunchDto {

    private Long id;
    private String name;
    private Float price;
    private Long cuisineId;
    private Long mealId;
    private Long dessertId;

    public static LunchDto toDto(Lunch entity) {
        return LunchDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .price(entity.getPrice())
            .cuisineId(entity.getCuisine().getId())
            .mealId(entity.getMeal().getId())
            .dessertId(entity.getDessert().getId())
            .build();
    }

    public static Lunch toEntity(LunchDto dto) {
        return Lunch.builder()
            .id(dto.getId())
            .name(dto.getName())
            .price(dto.getPrice())
            .build();
    }

    public static List<LunchDto> toDtoList(List<Lunch> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream()
            .map(LunchDto::toDto)
            .collect(Collectors.toList());
    }
}
