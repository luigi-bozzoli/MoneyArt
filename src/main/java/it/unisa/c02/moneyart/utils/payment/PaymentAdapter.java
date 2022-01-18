package it.unisa.c02.moneyart.utils.payment;

/**
 * Classe che espone i servizi di un pagamento.
 */
public interface PaymentAdapter {

  String makePayment(double amount) throws Exception;

  double recievePayment(String paymentId) throws Exception;
}
