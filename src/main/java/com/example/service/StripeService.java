package com.example.service;

import com.example.model.enums.SubscriptionType;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

    public Product createProduct() throws Exception {
        ProductCreateParams productParams = ProductCreateParams.builder()
            .setName("Lunch Subscription")
            .build();

        return Product.create(productParams);
    }

    public Price createPrice(
        String productId,
        Float totalPrice,
        SubscriptionType subscriptionType) throws Exception {

        PriceCreateParams.Recurring.Interval interval = null;
        switch (subscriptionType) {
            case DAILY -> interval = PriceCreateParams.Recurring.Interval.DAY;
            case WEEKLY -> interval = PriceCreateParams.Recurring.Interval.WEEK;
            case MONTHLY -> interval = PriceCreateParams.Recurring.Interval.MONTH;
        }
        PriceCreateParams.Recurring recurring = PriceCreateParams.Recurring.builder()
            .setInterval(interval)
            .build();

        PriceCreateParams priceParams = PriceCreateParams.builder()
            .setUnitAmount(totalPrice.longValue())
            .setCurrency("usd")
            .setRecurring(recurring)
            .setProduct(productId)
            .build();

        return Price.create(priceParams);
    }

    public Subscription createSubscription(
        String customerId,
        String priceId) throws Exception {

        SubscriptionCreateParams.Item item = SubscriptionCreateParams.Item.builder()
            .setPrice(priceId)
            .build();

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
            .setCustomer(customerId)
            .addItem(item)
            .build();

        return Subscription.create(params);
    }
}
