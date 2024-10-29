package com.example.model.enums;

public enum PaymentStatus {
    PAID,
    PENDING,
    FAILED;

    public static PaymentStatus of(String value) {
        return PaymentStatus.valueOf(value);
    }
}
