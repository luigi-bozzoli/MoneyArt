package it.unisa.c02.moneyart.utils.instantiation;

/**
 * Interfaccia che descrive un istanziatore di un tipo di oggetto T.
 *
 * @param <T> il tipo T degli oggetti istanziati
 */
public interface GenericInstantiator<T> {

  /**
   * Restituisce istanzia e restituisce un oggetto di tipo T.
   *
   * @return l'oggetto istanziato
   */
  public T newIstance();

}
