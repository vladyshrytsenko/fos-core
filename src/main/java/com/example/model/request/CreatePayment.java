package com.example.model.request;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class CreatePayment {
    @SerializedName("items")
    CreatePaymentItem[] items;

}
