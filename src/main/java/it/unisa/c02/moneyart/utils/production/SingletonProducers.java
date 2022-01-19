package it.unisa.c02.moneyart.utils.production;

import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import javax.enterprise.inject.Produces;

/**
 * Classe che restituisce un oggetto singleton, usato per CDI.
 *
 */

public class SingletonProducers {

  @Produces
  @Sing
  public AstaLockingSingleton produceAstaLockingSingleton() {
    return AstaLockingSingleton.retrieveIstance();
  }

  @Produces
  @Sing
  public TimerScheduler produceTimerScheduler() {
    return TimerScheduler.getInstance();
  }


}
