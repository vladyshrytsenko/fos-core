package com.example.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "meals")
@Getter @Setter
@AllArgsConstructor
@SuperBuilder
@SQLDelete(sql = "update meals set isDeleted = true and deletedAt = now() where id = ?")
@Where(clause = "isDeleted = false")
public class Meal extends BaseEntity {

    private String name;
    private Integer portionWeight;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lunch_id", referencedColumnName = "id")
    private Lunch lunch;
}
