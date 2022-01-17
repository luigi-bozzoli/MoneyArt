package it.unisa.c02.moneyart.utils.payment;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;

public class StripeReciver implements PaymentReciverAdapter {
  @Override
  public double recivePayment(String paymentId) throws Exception {
    return Session.retrieve(paymentId).getAmountTotal() / 100.0;
  }


  static {
    Stripe.apiKey =
        "sk_test_51KIvhUCoLwbVSLTbCtzTitzSKsdaV1jYWFfaeErOok1bIDuFAhxMWRwGq6dOu6IjoqWxVkutsXdIA8LU0KHPXkE800GBM7FiJD";
  }
}
