package com.example.model.dto;

import com.example.model.entity.Dessert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class DessertDto {

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

    public static DessertDto toDto(Dessert entity) {
        DessertDto dessertDto = DessertDto.builder()
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
            dessertDto.setCuisineId(entity.getCuisine().getId());
        }

        return dessertDto;
    }

    public static Dessert toEntity(DessertDto dto) {
        return Dessert.builder()
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

    public static List<DessertDto> toDtoList(List<Dessert> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        return list.stream()
            .map(DessertDto::toDto)
            .collect(Collectors.toList());
    }
}
