package com.example.model.request;

import com.example.model.entity.Cuisine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuisineRequest {

    private String name;

    public static Cuisine toEntity(CuisineRequest request) {
        return Cuisine.builder()
            .name(request.getName())
            .build();
    }
}
