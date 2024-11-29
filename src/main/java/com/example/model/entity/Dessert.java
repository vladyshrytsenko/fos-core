package com.example.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    private String name;
    private Float price;

    @Column(name = "portion_weight")
    private Integer portionWeight;
}
