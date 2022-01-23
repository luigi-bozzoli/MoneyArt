package it.unisa.c02.moneyart.utils.payment;

import com.stripe.Stripe;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe che implementa i metodi dell'interfaccia di PaymentAdapter
 * per interfacciarsi con il servizio Stripe.
 */

public class StripePayment implements PaymentAdapter {
  /**
   * Richiede un pagamento e restituisce l'url
   * dover reindirizzare l'utente per completare il pagamento.
   *
   * @param amount l'ammontare da pagare
   * @return l'url dover reindirizzare l'utente per completare il pagamento
   * @throws Exception in caso Stripe da un errore
   */
  @Override
  public String makePayment(double amount) throws Exception {
    Map<String, Object> paramsPrice = new HashMap<>();
    paramsPrice.put("unit_amount", (int) (amount * 100));
    paramsPrice.put("currency", "eur");
    paramsPrice.put("product", PRODUCT_ID);
    Price price = Price.create(paramsPrice);
    SessionCreateParams params =
        SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(YOUR_DOMAIN + SUCCESS_URL).setCancelUrl(YOUR_DOMAIN + CANCEL_URL)
            .addLineItem(
                SessionCreateParams.LineItem.builder().setQuantity(1L).setPrice(price.getId())
                    .build()).build();
    Session session = Session.create(params);
    return session.getUrl();

  }

  /**
   * Restituisce l'ammontare pagato dato l'id di un pagamento.
   *
   * @param paymentId l'id del pagamento
   * @return l'ammontare speso nel pagamento
   * @throws Exception nel caso Stripe da errore
   */
  @Override
  public double recievePayment(String paymentId) throws Exception {
    return Session.retrieve(paymentId).getAmountTotal() / 100.0;
  }

  private static final String YOUR_DOMAIN = "http://localhost:8080/MoneyArt_war";
  private static final String SUCCESS_URL = "/recivePayment?session_id={CHECKOUT_SESSION_ID}";
  private static final String CANCEL_URL = "/walletInfo";
  private static final String PRODUCT_ID = "prod_KytYHpYu4DKRYm";

  static {
    Stripe.apiKey = "sk_test_51KIvhUCoLwbVSLTbCtzTitzSKsdaV1jYWFfae"
        +
        "ErOok1bIDuFAhxMWRwGq6dOu6IjoqWxVkutsXdIA8LU0KHPXkE800GBM7FiJD";
  }


}
