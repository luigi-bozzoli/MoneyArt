package it.unisa.c02.moneyart.utils.payment;

public interface PaymentAdapter {

  String makePayment(double amount) throws Exception;

  double recievePayment(String paymentId) throws Exception;
}
