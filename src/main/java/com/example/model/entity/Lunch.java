package com.example.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "lunches")
@Getter @Setter
@AllArgsConstructor
@SuperBuilder
@SQLDelete(sql = "update lunches set isDeleted = true and deletedAt = now() where id = ?")
@Where(clause = "isDeleted = false")
public class Lunch extends BaseEntity {

    private String name;
    private Float price;

    @OneToOne
    private Cuisine cuisine;

    @OneToOne
    private Meal meal;

    @OneToOne
    private Dessert dessert;
}
