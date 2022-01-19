package unit.utils.locking;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AstaLockingSingletonUnitTest {

  private AstaLockingSingleton astaLockingSingleton;


  @BeforeEach
  void setUp() throws IllegalAccessException, InvocationTargetException, InstantiationException {
    //reinizializza il singleton

    Constructor<?> constructor = AstaLockingSingleton.class.getDeclaredConstructors()[1];

    constructor.setAccessible(true);

    astaLockingSingleton = (AstaLockingSingleton) constructor.newInstance();
  }


  @Test
  void retrieveIstance() {
    AstaLockingSingleton astaLockingSingleton1 = AstaLockingSingleton.retrieveIstance();
    AstaLockingSingleton astaLockingSingleton2 = AstaLockingSingleton.retrieveIstance();
    Assertions.assertSame(astaLockingSingleton1, astaLockingSingleton2);
  }

  @Nested
  @DisplayName("Locking")
  class LockAndUnlockTest {

    private Long oracolo;
    

    private Asta asta;

    private static final int NUM_THREADS = 10;

    private static final int NUM_ITERATIONS = 1000000;

    @BeforeEach
    public void setUp() {
      oracolo = 0L;
      asta = new Asta();
      asta.setId(1);

    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
      Runnable runnable = () -> {
        for (int i = 0; i < NUM_ITERATIONS; i++) {
          astaLockingSingleton.lockAsta(asta);
          oracolo++;
          astaLockingSingleton.unlockAsta(asta);
        }

      };
      Thread[] threads = new Thread[NUM_THREADS];
      for(int i = 0; i < threads.length; i++){
        threads[i] = new Thread(runnable);
      }

      for (Thread thread : threads){
        thread.start();
      }

      for (Thread thread: threads){
        thread.join();
      }
      Assertions.assertEquals(NUM_ITERATIONS*NUM_THREADS,oracolo);
    }


  }
}