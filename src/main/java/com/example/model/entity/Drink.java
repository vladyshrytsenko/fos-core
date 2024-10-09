package com.example.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "drinks")
@Getter @Setter
@AllArgsConstructor
@SuperBuilder
@SQLDelete(sql = "update drinks set isDeleted = true and deletedAt = now() where id = ?")
@Where(clause = "isDeleted = false")
public class Drink extends BaseEntity {

    private String name;
    private Float price;
    private Boolean iceCubes;
    private Boolean lemon;
}
