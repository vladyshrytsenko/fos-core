package com.example.model.request;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class CreatePaymentItem {
    @SerializedName("id")
    String id;

    @SerializedName("amount")
    Long amount;

}
