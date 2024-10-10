package com.example.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "cuisines")
@Getter @Setter
@AllArgsConstructor
@SuperBuilder
@SQLDelete(sql = "update cuisines set isDeleted = true and deletedAt = now() where id = ?")
@Where(clause = "isDeleted = false")
public class Cuisine extends BaseEntity {

    private String name;

    @OneToOne(mappedBy = "cuisine")
    private Lunch lunch;

}
