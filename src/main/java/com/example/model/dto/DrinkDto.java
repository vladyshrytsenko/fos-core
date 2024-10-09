package com.example.model.dto;

import com.example.model.entity.Drink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class DrinkDto {

    private Long id;
    private String name;
    private Float price;
    private Boolean iceCubes;
    private Boolean lemon;

    public static DrinkDto toDto(Drink entity) {
        return DrinkDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .price(entity.getPrice())
            .iceCubes(entity.getIceCubes())
            .lemon(entity.getLemon())
            .build();
    }

    public static Drink toEntity(DrinkDto dto) {
        return Drink.builder()
            .id(dto.getId())
            .name(dto.getName())
            .price(dto.getPrice())
            .iceCubes(dto.getIceCubes())
            .lemon(dto.getLemon())
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
}
