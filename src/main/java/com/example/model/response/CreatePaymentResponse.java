package com.example.model.response;

public class CreatePaymentResponse {
    private String clientSecret;
    private String dpmCheckerLink;

    public CreatePaymentResponse(String clientSecret, String transactionId) {
        this.clientSecret = clientSecret;
        // [DEV]: For demo purposes only, you should avoid exposing the PaymentIntent ID in the client-side code.
        this.dpmCheckerLink = "https://dashboard.stripe.com/settings/payment_methods/review?transaction_id="+transactionId;
    }
}
