package com.shopsphere.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * Handles Stripe payments with a graceful MOCK fallback if keys are not configured.
 */
public class StripeService {
    private final String secretKey;
    private final String currency;

    public StripeService() {
        Properties p = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("stripe.properties")) {
            if (in != null) p.load(in);
        } catch (Exception ignored) {}
        this.secretKey = p.getProperty("stripe.secret.key", "").trim();
        this.currency = p.getProperty("stripe.currency", "inr").trim();
        if (!secretKey.isEmpty()) Stripe.apiKey = secretKey;
    }

    public boolean isMockMode() { return secretKey.isEmpty(); }

    /**
     * Create a payment intent. Returns a "clientSecret" string that the frontend could use.
     * In MOCK mode returns a fake reference so demo works fully offline.
     */
    public String createPaymentIntent(double amount, String orderRef) throws Exception {
        long cents = Math.round(amount * 100);
        if (isMockMode()) {
            return "MOCK_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        }
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(cents)
                .setCurrency(currency)
                .setDescription("ShopSphere order " + orderRef)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true).build())
                .build();
        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getClientSecret();
    }
}
