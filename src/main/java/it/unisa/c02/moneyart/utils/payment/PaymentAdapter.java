package it.unisa.c02.moneyart.utils.payment;

/**
 * Classe che espone i servizi di un pagamento verso sistemi esterni.
 */
public interface PaymentAdapter {

  /**
   * Richiede un pagamento e restituisce l'url
   * dover reindirizzare l'utente per completare il pagamento.
   *
   * @param amount l'ammontare da pagare
   * @return l'url dover reindirizzare l'utente per completare il pagamento
   * @throws Exception in caso il sistema esterno di pagamento causa un errore
   */
  String makePayment(double amount) throws Exception;

  /**
   * Restituisce l'ammontare pagato dato l'id di un pagamento.
   *
   * @param paymentId l'id del pagamento
   * @return l'ammontare speso nel pagamento
   * @throws Exception nel caso il servizio esterno di pagamento da errore
   */
  double recievePayment(String paymentId) throws Exception;
}
