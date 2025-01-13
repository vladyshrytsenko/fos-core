package com.example.foscore.model.enums;

public enum SubscriptionType {
    DAILY,
    WEEKLY,
    MONTHLY;

    public static SubscriptionType of(String value) {
        return SubscriptionType.valueOf(value);
    }
}
