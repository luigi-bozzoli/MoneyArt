package it.unisa.c02.moneyart.utils.production;

/**
 * Interfaccia che descrive un produttore di un tipo di oggetto T.
 *
 * @param <T> il tipo T degli oggetti prodotti
 */
public interface GenericProducer<T> {

  /**
   * Produce un oggetto di tipo T.
   *
   * @return l'oggetto prodotto
   */
  public T produce();

}
