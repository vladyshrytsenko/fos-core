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

@Entity(name = "orders")
@Getter @Setter
@AllArgsConstructor
@SuperBuilder
@SQLDelete(sql = "update orders set isDeleted = true and deletedAt = now() where id = ?")
@Where(clause = "isDeleted = false")
public class Order extends BaseEntity {

    private Float totalPrice;

    @OneToOne
    private Lunch lunch;

    @OneToOne
    private Drink drink;
}
