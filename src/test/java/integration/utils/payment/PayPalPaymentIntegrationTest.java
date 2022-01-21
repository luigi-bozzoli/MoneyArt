package integration.utils.payment;

import it.unisa.c02.moneyart.utils.payment.PayPalPayment;
import it.unisa.c02.moneyart.utils.payment.PaymentAdapter;
import it.unisa.c02.moneyart.utils.payment.StripePayment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PayPalPaymentIntegrationTest {

  private PaymentAdapter paymentAdapter;

  @BeforeEach
  void setUp() {
    paymentAdapter = new PayPalPayment();
  }


  @Nested
  @DisplayName("Payment")
  public class PaymentTest {

    @ParameterizedTest
    @ValueSource(doubles = {20, 50, 18.10})
    public void makePaymentSuccess(double amount) throws Exception {

      String result = paymentAdapter.makePayment(amount);
      Assertions.assertTrue(result.contains("https://www.sandbox.paypal.com/"));

    }

    @ParameterizedTest
    @ValueSource(doubles = {1000000000,2000000000,10000000000d})
    public void makePaymentInsuccess(double amount) {
      Assertions.assertThrows(Exception.class,() -> paymentAdapter.makePayment(amount));
    }

  }

  @Nested
  class ReciveTest{

    @Test
    public void recivePaymentSuccess() throws Exception {

      Assertions.assertEquals(100,paymentAdapter.recievePayment("PAYID-MHT65MY84A54453916521533"));
    }

    @Test
    public void recivePaymentInsuccess(){
      Assertions.assertThrows(Exception.class,() -> paymentAdapter.recievePayment("id_non_esistente"));
    }
  }

}