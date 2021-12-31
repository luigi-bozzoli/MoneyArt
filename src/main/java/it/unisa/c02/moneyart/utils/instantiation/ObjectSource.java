package it.unisa.c02.moneyart.utils.instantiation;

import java.util.Map;

/**
 * classe che si occupa di istanziare oggetti data l'interfaccia che devono implementare.
 */
public class ObjectSource {


  private static Map<String, GenericInstantiator<?>> istantiators;

  /**
   * Setta le interfacce istanziabili dalla classe, una volta settate è impossibile modificarle.
   *
   * @param newIstantiators mappa che associa il nome completo di un interfaccia
   *                        a un oggetto che istanzia oggetti che implementano quell'interfaccia
   */
  public static void setIstantiator(Map<String, GenericInstantiator<?>> newIstantiators) {

    if (istantiators == null) {
      istantiators = newIstantiators;
    } else {
      throw new IllegalStateException("gli istanziatori devono essere settati solo una volta");
    }

  }

  /**
   * Data un interfaccia restituisce un istanza di un oggetto che implementa quell'interfaccia.
   *
   * @param interfaccia l'interfaccia da istanziare
   * @return l'oggetto istanziato che implementa l'interfaccia
   */
  public static Object getIstance(Class<?> interfaccia) {
    if (istantiators == null) {
      throw new IllegalStateException("gli istanziatori non sono stati settati");
    }
    GenericInstantiator<?> istantiator = istantiators.get(interfaccia.getName());
    if (istantiator == null) {
      throw new IllegalArgumentException(
          "non è presente un istanziatore per l'interfaccia " + interfaccia.getName());
    }
    return istantiator.newIstance();
  }

}
