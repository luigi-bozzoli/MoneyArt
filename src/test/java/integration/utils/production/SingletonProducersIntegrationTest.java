package integration.utils.production;

import static org.junit.jupiter.api.Assertions.*;

import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.production.SingletonProducers;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SingletonProducersIntegrationTest {

  private SingletonProducers singletonProducers;

  @BeforeEach
  void setUp() {
    singletonProducers = new SingletonProducers();
  }


  @Test
  void produceAstaLockingSingleton() {
    Assertions.assertSame(singletonProducers.produceAstaLockingSingleton(),singletonProducers.produceAstaLockingSingleton());
    Assertions.assertSame(singletonProducers.produceAstaLockingSingleton(),AstaLockingSingleton.retrieveIstance());
  }

  @Test
  void produceTimerScheduler() {
    Assertions.assertSame(singletonProducers.produceTimerScheduler(),singletonProducers.produceTimerScheduler());
    Assertions.assertSame(singletonProducers.produceTimerScheduler(), TimerScheduler.getInstance());
  }
}