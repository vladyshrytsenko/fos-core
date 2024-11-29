package com.example.model.request;

import com.example.model.entity.Cuisine;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuisineRequest {

    @Size(min = 4, max = 32, message = "invalid 'name' size")
    private String name;

    public static Cuisine toEntity(CuisineRequest request) {
        return Cuisine.builder()
            .name(request.getName())
            .build();
    }
}
