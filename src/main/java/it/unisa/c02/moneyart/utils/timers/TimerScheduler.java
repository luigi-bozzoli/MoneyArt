package it.unisa.c02.moneyart.utils.timers;

import it.unisa.c02.moneyart.utils.production.Retriever;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * classe singleton, permette di settare dei timer dopo i quali vengono attivati
 * dei servizi settati allo startup dell'applicazione.
 */
public class TimerScheduler {

  /**
   * Classe privata interna,
   * permette di inizializzare l'istanza del singleton in maniera semplice e ThreadSafe.
   */
  private static class SingletonHelper {
    private static final TimerScheduler ISTANCE = new TimerScheduler();
  }


  private TimerScheduler() {
    timedObjectDao = Retriever.getIstance(TimedObjectDao.class);
    services = new HashMap<>();
    timerSet = new HashSet<>();
  }

  /**
   * Restituisce l'unica istanza del singleton.
   *
   * @return l'istanza del singleton
   */
  public static TimerScheduler getInstance() {
    return SingletonHelper.ISTANCE;
  }

  /**
   * permette di registrare un nuovo servizio attivabile tramite un timer.
   *
   * @param serviceName il nome del servizio da registrare
   * @param service     l'esecutore del servizio
   */
  public void registerTimedService(String serviceName, TimerService service) {

    services.put(serviceName, service);
  }

  /**
   * Setta un timer per uno dei servizi registrati e lo salva nella memoria persistente.
   *
   * @param timedObject contiene le informazioni necessarie per la creazione del timer
   */
  public void scheduleTimedService(TimedObject timedObject) {
    TimerService task = services.get(timedObject.getTaskType());
    if (task == null) {
      throw new IllegalArgumentException(
          "non esiste un servizio registrato sotto il nome di " + timedObject.getTaskType());
    }
    Timer timer = new Timer();
    TimedTask timedTask = new TimedTask(task, timedObject, timedObjectDao, timerSet, timer);
    timedObjectDao.doCreate(timedObject);
    timer.schedule(timedTask, timedObject.getTaskDate());
    timerSet.add(timer);

  }

  /**
   * Disattiva i timer attuallmente attivi.
   */
  public void shutdownTimers() {
    for (Timer t : timerSet) {
      t.cancel();
    }
  }

  /**
   * Ripristina i timer che erano stati salvati nella memoria persistente e li riattiva.
   */
  public int retrivePersistentTimers() {
    List<TimedObject> timedObjects = timedObjectDao.doRetrieveAll("id");
    if (timedObjects == null) {
      return 0;
    }
    for (TimedObject timedObject : timedObjects) {
      timedObjectDao.doDelete(timedObject);
      scheduleTimedService(timedObject);
    }
    return timedObjects.size();
  }

  /**
   * rappresenta la struttura di una qualsiasi esecuzione di un servizio allo scadere del timer.
   */
  private static class TimedTask extends TimerTask {

    /**
     * Crea un oggetto TimedTask con le informazioni necessarie per la sua esecuzione.
     *
     * @param service        il servizio che deve essere effettuato alla fine del timer
     * @param timedObject    le informazioni relative al timer che è stato attivato
     * @param timedObjectDao permette di effettuare operazioni di persistenza sul timer
     * @param timedTaskSet   il set di timer attualmente attivi nel sistema
     * @param timer          il timer associato al task che deve essere eseguito
     */
    public TimedTask(TimerService service, TimedObject timedObject, TimedObjectDao timedObjectDao,
                     Set<Timer> timedTaskSet, Timer timer) {

      this.service = service;
      this.timedObject = timedObject;
      this.timedObjectDao = timedObjectDao;
      this.timedTaskSet = timedTaskSet;
      this.timer = timer;
    }

    /**
     * esegue il servizio associato al timer,
     * chiude il thread in cui viene eseguito il servizio,
     * elimina il timer dai timer attivi del sistema,
     * elimina il timer dalla memoria persistente.
     */
    @Override
    public void run() {
      try {

        service.executeTimedTask(timedObject);
      } finally {
        timedObjectDao.doDelete(timedObject);
        timedTaskSet.remove(timer);
        timer.cancel();
        cancel();
      }

    }


    private TimerService service;
    private TimedObject timedObject;
    private TimedObjectDao timedObjectDao;
    private Set<Timer> timedTaskSet;
    private Timer timer;
  }


  private Map<String, TimerService> services;

  private TimedObjectDao timedObjectDao;

  private Set<Timer> timerSet;


}
