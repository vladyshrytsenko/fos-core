package com.example.service;

import com.example.model.enums.SubscriptionType;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceListParams;
import com.stripe.param.PriceUpdateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductListParams;
import com.stripe.param.SubscriptionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

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

    public Price findPriceByProductName(String productName) throws StripeException {
        Product product = Product.list(ProductListParams.builder().build())
            .getData().stream()
            .filter(p -> p.getName().equals(productName))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found"));

        return Price.list(PriceListParams.builder()
                .setProduct(product.getId())
                .build()).getData().stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Price not found for product"));
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
}
