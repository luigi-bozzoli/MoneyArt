package it.unisa.c02.moneyart.utils.payment;

public interface PayerAdapter {

  String makePayment(double amount) throws Exception;
}
