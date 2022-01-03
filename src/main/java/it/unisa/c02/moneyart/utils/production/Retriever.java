package it.unisa.c02.moneyart.utils.production;

import java.util.Map;

/**
 * classe che si occupa di restituire oggetti data l'interfaccia che devono implementare.
 */
public class Retriever {


  private static Map<RetriverKey, GenericProducer<?>> producers;

  /**
   * Setta le interfacce restituibili dalla classe, una volta settate è impossibile modificarle.
   *
   * @param newProducers mappa che associa il nome completo di un interfaccia
   *                     a un oggetto che produce oggetti che implementano quell'interfaccia
   */
  public static void setProducers(Map<RetriverKey, GenericProducer<?>> newProducers) {

    if (producers == null) {
      producers = newProducers;
    } else {
      throw new IllegalStateException("i produttori devono essere settati solo una volta");
    }

  }

  /**
   * Restituisce un istanza di un oggetto che implementa quell'interfaccia.
   *
   * @param interfaccia l'interfaccia da istanziare
   * @return l'oggetto che implementa l'interfaccia, viene usata l'implementazione di default
   */
  public static <T> T getIstance(Class<T> interfaccia) {
    if (producers == null) {
      throw new IllegalStateException("i produttori non sono stati settati");
    }
    RetriverKey key = new RetriverKey(interfaccia.getName());
    GenericProducer<T> producer = (GenericProducer<T>) producers.get(key);
    if (producer == null) {
      throw new IllegalArgumentException(
          "non è presente un produttore per l'interfaccia " + interfaccia.getName());
    }
    return producer.produce();
  }

  /**
   * Restituisce un istanza di un oggetto che implementa un interfaccia.
   *
   * @param interfaccia   l'interfaccia da istanziare
   * @param qualificatore usato per distinguere fra le possibili implementazioni disponibili dell'interfaccia
   * @return l'oggetto che implementa l'interfaccia
   */
  public static <T> T getIstance(Class<T> interfaccia, String qualificatore) {
    if (producers == null) {
      throw new IllegalStateException("i produttori non sono stati settati");
    }
    RetriverKey key = new RetriverKey(interfaccia.getName(), qualificatore);
    GenericProducer<T> producer = (GenericProducer<T>) producers.get(key);
    if (producer == null) {
      throw new IllegalArgumentException(
          "non è presente un produttore per l'interfaccia " + interfaccia.getName());
    }
    return producer.produce();
  }

  /**
   * è una coppia di due stringhe,
   * usata come chiave per individuare il produttore di una specifica interfaccia.
   */
  public static class RetriverKey {

    /**
     * Istanzia la coppia specificando entrambi i valori.
     *
     * @param interfaceName il nome dell'interfaccia
     * @param qualificatore stringa usata per distinguere
     *                      specifici produttori della stessa interfaccia
     */
    public RetriverKey(String interfaceName, String qualificatore) {
      this.interfaceName = interfaceName;
      this.qualificatore = qualificatore;
    }

    /**
     * Istanzia la coppia specificando solo il nome dell'interfaccia,
     * il qualificatore verrà impostato come default.
     *
     * @param interfaceName il nome dell'interfaccia
     */
    public RetriverKey(String interfaceName) {
      this.interfaceName = interfaceName;
      this.qualificatore = DEFAULT_QUALIFIER;
    }

    private String interfaceName;
    private String qualificatore;
    private static final String DEFAULT_QUALIFIER = "DEFAULT";
  }

}
