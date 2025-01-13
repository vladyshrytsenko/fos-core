package com.example.foscore.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "orders")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SQLDelete(sql = "update orders set is_deleted = true, deleted_at = current_timestamp where id = ?")
@Where(clause = "is_deleted = false")
public class Order extends BaseEntity {

    @Column(name = "total_price")
    private Float totalPrice;

    @OneToOne
    @JoinColumn(name = "dessert_id", referencedColumnName = "id")
    private Dessert dessert;

    @OneToOne
    @JoinColumn(name = "meal_id", referencedColumnName = "id")
    private Meal meal;

    @OneToOne
    @JoinColumn(name = "drink_id", referencedColumnName = "id")
    private Drink drink;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @Column(name = "ice_cubes")
    private Boolean iceCubes;

    private Boolean lemon;
}
