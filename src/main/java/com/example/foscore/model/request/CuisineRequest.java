package com.example.foscore.model.request;

import com.example.foscore.model.entity.Cuisine;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "'name' cannot be blank")
    @Size(min = 4, max = 32, message = "invalid 'name' size")
    private String name;

    public static Cuisine toEntity(CuisineRequest request) {
        return Cuisine.builder()
            .name(request.getName())
            .build();
    }
}
