package it.unisa.c02.moneyart.utils.payment;

import com.stripe.Stripe;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.HashMap;
import java.util.Map;

public class StripePayer implements PayerAdapter {
  @Override
  public String makePayment(double amount) throws Exception {
    Map<String, Object> paramsPrice = new HashMap<>();
    paramsPrice.put("unit_amount", (int) (amount * 100));
    paramsPrice.put("currency", "eur");
    paramsPrice.put("product", PRODUCT_ID);
    Price price = Price.create(paramsPrice);
    SessionCreateParams params =
        SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(YOUR_DOMAIN + SUCCESS_URL)
            .setCancelUrl(YOUR_DOMAIN + CANCEL_URL)
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPrice(price.getId())
                    .build())
            .build();
    Session session = Session.create(params);
    return session.getUrl();

  }

  private static final String YOUR_DOMAIN = "http://localhost:8080/MoneyArt_war";
  private static final String SUCCESS_URL =
      "/recivePayment?session_id={CHECKOUT_SESSION_ID}";
  private static final String CANCEL_URL = "/";
  private static final String PRODUCT_ID = "prod_KytYHpYu4DKRYm";

  static {
    Stripe.apiKey = "sk_test_51KIvhUCoLwbVSLTbCtzTitzSKsdaV1jYWFfaeErOok1bIDuFAhxMWRwGq6dOu6IjoqWxVkutsXdIA8LU0KHPXkE800GBM7FiJD";
  }


}
