package com.example.foscore.service;

import com.example.foscore.model.enums.SubscriptionType;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceListParams;
import com.stripe.param.PriceUpdateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductListParams;
import com.stripe.param.SubscriptionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StripeService {

    public PaymentIntent createPaymentIntent(long amount, String currency) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);

        return PaymentIntent.create(params);
    }

    public Product createProduct(String name) {
        ProductCreateParams productParams = ProductCreateParams.builder()
            .setName(name)
            .build();

        try {
            return Product.create(productParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public Price createPrice(
        String productId,
        Float price) {

        Float priceInCents = price * 100;
        PriceCreateParams priceParams = PriceCreateParams.builder()
            .setUnitAmount(priceInCents.longValue())
            .setCurrency("usd")
            .setProduct(productId)
            .build();

        try {
            return Price.create(priceParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public Price getPriceByProductName(String productName) throws StripeException {
        Product productByName = this.getProductByName(productName);

        return Price.list(PriceListParams.builder()
                .setProduct(productByName.getId())
                .build()).getData().stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Price not found for product"));
    }

    public Product getProductByName(String productName) throws StripeException {
        return Product.list(ProductListParams.builder().build())
            .getData().stream()
            .filter(p -> p.getName().equals(productName))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Price createRecurringPrice(
        String existingPriceId,
        SubscriptionType subscriptionType) throws StripeException {

        Price existingPrice = Price.retrieve(existingPriceId);

        PriceCreateParams.Recurring.Interval interval = switch (subscriptionType) {
            case DAILY -> PriceCreateParams.Recurring.Interval.DAY;
            case WEEKLY -> PriceCreateParams.Recurring.Interval.WEEK;
            case MONTHLY -> PriceCreateParams.Recurring.Interval.MONTH;
        };

        PriceCreateParams.Recurring recurring = PriceCreateParams.Recurring.builder()
            .setInterval(interval)
            .build();

        PriceCreateParams priceParams = PriceCreateParams.builder()
            .setUnitAmount(existingPrice.getUnitAmount())
            .setCurrency(existingPrice.getCurrency())
            .setProduct(existingPrice.getProduct())
            .setRecurring(recurring)
            .build();

        return Price.create(priceParams);
    }

    public Price removeRecurringFromExistingOnePrice(String existingPriceId) throws StripeException {
        Price existingPrice = Price.retrieve(existingPriceId);

        PriceCreateParams priceParams = PriceCreateParams.builder()
            .setUnitAmount(existingPrice.getUnitAmount())
            .setCurrency(existingPrice.getCurrency())
            .setProduct(existingPrice.getProduct())
            .build();

        Price newOneTimePrice = Price.create(priceParams);

        existingPrice.update(PriceUpdateParams.builder().setActive(false).build());

        return newOneTimePrice;
    }

    public Subscription createSubscription(
        String customerId,
        String priceId,
        String subscriptionTypeStr) throws Exception {

        SubscriptionType subscriptionType = SubscriptionType.valueOf(subscriptionTypeStr);
        Price recurringPrice = createRecurringPrice(priceId, subscriptionType);

        SubscriptionCreateParams.Item item = SubscriptionCreateParams.Item.builder()
            .setPrice(recurringPrice.getId())
            .build();

        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
            .setCustomer(customerId)
            .addItem(item)
            .build();

        return Subscription.create(params);
    }

    public Invoice createInvoice(String customerId) {
        InvoiceCreateParams params = InvoiceCreateParams.builder()
            .setCustomer(customerId)
            .build();

        try {
            return Invoice.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    public void finalizeAndSendInvoice(String invoiceId) throws Exception {
        Invoice invoice = Invoice.retrieve(invoiceId);
        invoice.finalizeInvoice();
    }

    public String createCustomer(String email, String name) {
        CustomerCreateParams params = CustomerCreateParams.builder()
            .setEmail(email)
            .setName(name)
            .build();

        Customer customer = null;
        try {
            customer = Customer.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        return customer != null ? customer.getId() : null;
    }

    public void attachTestCardToCustomer(String customerId) throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.create(
            PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.CARD)
                .setCard(PaymentMethodCreateParams.CardDetails.builder()
                    .putExtraParam("token", "tok_visa")
                    .build()
                )
                .build()
        );

        paymentMethod.attach(PaymentMethodAttachParams.builder()
            .setCustomer(customerId)
            .build());

        Customer customer = Customer.retrieve(customerId);
        customer.update(CustomerUpdateParams.builder()
            .setInvoiceSettings(CustomerUpdateParams.InvoiceSettings.builder()
                .setDefaultPaymentMethod(paymentMethod.getId())
                .build())
            .build());
    }

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    private void setStripeSecretKey() {
        Stripe.apiKey = stripeSecretKey;
    }
}
