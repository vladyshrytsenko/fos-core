package com.example.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "meals")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SQLDelete(sql = "update meals set is_deleted = true, deleted_at = current_timestamp where id = ?")
@Where(clause = "is_deleted = false")
public class Meal extends BaseEntity {

    private String name;
    private Float price;

    @Column(name = "portion_weight")
    private Integer portionWeight;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cuisine_id", referencedColumnName = "id")
    private Cuisine cuisine;
}
