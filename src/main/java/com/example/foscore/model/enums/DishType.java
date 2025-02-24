package com.example.foscore.model.enums;

public enum DishType {
    MEAL,
    DESSERT;

    public static DishType of(String value) {
        return DishType.valueOf(value);
    }
}
