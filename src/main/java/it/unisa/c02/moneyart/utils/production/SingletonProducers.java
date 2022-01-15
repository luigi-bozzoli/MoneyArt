package it.unisa.c02.moneyart.utils.production;

import it.unisa.c02.moneyart.model.blockchain.MoneyArtNft;
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import javax.enterprise.inject.Produces;

public class SingletonProducers {

  @Produces @Sing
  public AstaLockingSingleton produceAstaLockingSingleton() {
    return AstaLockingSingleton.retrieveIstance();
  }

  @Produces @Sing
  public TimerScheduler produceTimerScheduler() {
    return TimerScheduler.getInstance();
  }


}
