package it.unisa.c02.moneyart.utils.payment;

public interface PaymentReciverAdapter {

  double recivePayment(String paymentId) throws Exception;
}
