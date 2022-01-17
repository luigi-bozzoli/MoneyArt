package it.unisa.c02.moneyart.utils.payment;

import de.micromata.paypal.PayPalConfig;
import de.micromata.paypal.PayPalConnector;
import de.micromata.paypal.data.Currency;
import de.micromata.paypal.data.Payment;
import de.micromata.paypal.data.ShippingPreference;
import de.micromata.paypal.data.Transaction;

public class PayPalPayer implements PayerAdapter {



  @Override
  public String makePayment(double amount) throws  Exception {
    Transaction transaction = new Transaction(Currency.EUR);
    transaction.addItem("MoneyArt Saldo", amount);
    Payment payment = new Payment(transaction);
    payment.setShipping(ShippingPreference.NO_SHIPPING);
    Payment paymentCreated = PayPalConnector.createPayment(config, payment);
    return paymentCreated.getPayPalApprovalUrl();
  }

  private static PayPalConfig config = new PayPalConfig()
        .setClientId("AdQKkY9nhdWjnFSQFMIRi-yvpdq194KTkZxtkwD72P6H2rVwiteSMvacjAxPd61laDSWBWU7N6y1_ZG9")
        .setClientSecret("EEh8AzYy9IRpDCgF33qOI3n7R8BcOlxlezMCEVQkUECtxjOrZEVSCu7pREQQSYz2Y4PlesFvvzggiqnS")
        .setReturnUrl("http://localhost:8080/MoneyArt_war/recivePayment").setCancelUrl("http://localhost:8080/MoneyArt_war")
        .setMode(PayPalConfig.Mode.SANDBOX);
}
