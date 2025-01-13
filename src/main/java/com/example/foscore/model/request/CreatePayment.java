package com.example.foscore.model.request;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class CreatePayment {
    @SerializedName("items")
    CreatePaymentItem[] items;

}
