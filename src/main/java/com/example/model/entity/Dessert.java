package com.example.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "desserts")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SQLDelete(sql = "update desserts set is_deleted = true, deleted_at = current_timestamp where id = ?")
@Where(clause = "is_deleted = false")
public class Dessert extends BaseEntity {

    @NotNull(message = "'name' should not be null")
    @Size(min = 4, max = 32, message = "invalid 'name' size")
    private String name;

    @NotNull(message = "'price' should not be null")
    private Float price;

    @NotNull(message = "'portionWeight' should not be null")
    @Column(name = "portion_weight")
    private Integer portionWeight;
}
