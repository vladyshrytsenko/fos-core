package com.example.model.dto;

import com.example.model.entity.Dessert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class DessertDto {

    private Long id;
    private String name;
    private Integer portionWeight;
    private Long lunchId;

    public static DessertDto toDto(Dessert entity) {
        return DessertDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .portionWeight(entity.getPortionWeight())
            .lunchId(entity.getLunch().getId())
            .build();
    }

    public static Dessert toEntity(DessertDto dto) {
        return Dessert.builder()
            .id(dto.getId())
            .name(dto.getName())
            .portionWeight(dto.getPortionWeight())
            .build();
    }

    public static List<DessertDto> toDtoList(List<Dessert> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream()
            .map(DessertDto::toDto)
            .collect(Collectors.toList());
    }
}
