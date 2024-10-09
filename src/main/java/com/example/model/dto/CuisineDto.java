package com.example.model.dto;

import com.example.model.entity.Cuisine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class CuisineDto {

    private Long id;
    private String name;

    public static CuisineDto toDto(Cuisine entity) {
        return CuisineDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .build();
    }

    public static Cuisine toEntity(CuisineDto dto) {
        return Cuisine.builder()
            .id(dto.getId())
            .name(dto.getName())
            .build();
    }

    public static List<CuisineDto> toDtoList(List<Cuisine> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream()
            .map(CuisineDto::toDto)
            .collect(Collectors.toList());
    }
}
