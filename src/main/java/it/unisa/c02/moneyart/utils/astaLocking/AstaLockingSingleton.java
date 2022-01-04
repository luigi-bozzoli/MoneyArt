package it.unisa.c02.moneyart.utils.astaLocking;

import it.unisa.c02.moneyart.model.beans.Asta;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Classe Singleton utilizzata per evitare operazioni di
 * aggiornamento concorrente delle offerte nelle Aste.
 */
public class AstaLockingSingleton {

  /**
   * Classe privata interna,
   * permette di inizializzare l'istanza del singleton in maniera semplice e ThreadSafe.
   */
  private static class SingletonHelper {
    private static AstaLockingSingleton ISTANCE = new AstaLockingSingleton();
  }

  /**
   * Restituisce l'unica istanza del singleton.
   *
   * @return l'istanza del singleton
   */
  public static AstaLockingSingleton retrieveIstance() {
    return SingletonHelper.ISTANCE;
  }

  /**
   * costruttore privato del Singleton.
   */
  private AstaLockingSingleton() {

    locks = new HashMap<>();
    createAndDestroyLock = new ReentrantLock();

  }

  /**
   * Ottiene il lock per l'asta passata come parametro.
   *
   * @param asta l'asta per cui ottenere il lock
   */
  public void lockAsta(Asta asta) {

    createAndDestroyLock.lock();
    CountingLock countingLock = locks.get(asta.getId());
    if (countingLock == null) {
      countingLock = new CountingLock();
      locks.put(asta.getId(), countingLock);
    }
    countingLock.setNumberOfLocks(countingLock.getNumberOfLocks() + 1);
    createAndDestroyLock.unlock();
    countingLock.getLock().lock();


  }


  /**
   * Rilascia il lock per l'asta passata come parametro.
   *
   * @param asta l'asta per cui rilasciare il lock
   */
  public void unlockAsta(Asta asta) {
    createAndDestroyLock.lock();
    CountingLock countingLock = locks.get(asta.getId());
    countingLock.setNumberOfLocks(countingLock.getNumberOfLocks() - 1);
    if (countingLock.getNumberOfLocks() == 0) {
      locks.remove(asta.getId());
    }
    countingLock.getLock().unlock();
    createAndDestroyLock.unlock();


  }

  private Lock createAndDestroyLock;

  private Map<Integer, CountingLock> locks;

  /**
   * classe interna che definisce un lock che conta
   * il numero di elementi che sono interessati ad ottenere il lock.
   */
  private static class CountingLock {


    public CountingLock() {
      lock = new ReentrantLock();
      numberOfLocks = 0;
    }

    public Lock getLock() {
      return lock;
    }

    public void setLock(Lock lock) {
      this.lock = lock;
    }

    public int getNumberOfLocks() {
      return numberOfLocks;
    }

    public void setNumberOfLocks(int numberOfLocks) {
      this.numberOfLocks = numberOfLocks;
    }

    private Lock lock;
    private int numberOfLocks;
  }

}
