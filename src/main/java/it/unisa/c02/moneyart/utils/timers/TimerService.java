package it.unisa.c02.moneyart.utils.timers;

import java.io.Serializable;

/**
 * Interfaccia che descrive un servizio eseguibile allo scadere di un timer.
 */
public interface TimerService {

  /**
   * Il servizio che deve essere eseguito allo scadere del timer.
   *
   * @param item dati necessari per l'esecuzione del servizio
   */
  void executeTimedTask(TimedObject item);

}
