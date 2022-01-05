package it.unisa.c02.moneyart.utils.timers;

import it.unisa.c02.moneyart.utils.production.Retriever;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class TimerScheduler {


  private static class SingletonHelper {
    private static TimerScheduler ISTANCE = new TimerScheduler();
  }

  private TimerScheduler() {
    timedObjectDao = Retriever.getIstance(TimedObjectDao.class);
    services = new HashMap<String, TimerService>();
    timerSet = new HashSet<>();
  }

  public static TimerScheduler getInstance() {
    return SingletonHelper.ISTANCE;
  }

  public void registerTimedService(String serviceName, TimerService service) {

    services.put(serviceName, service);
  }

  public void scheduleTimedService(TimedObject timedObject) {
    TimerService task = services.get(timedObject.getTaskType());
    Timer timer = new Timer();
    TimedTask timedTask = new TimedTask(task, timedObject, timedObjectDao, timerSet, timer);
    timedObjectDao.doCreate(timedObject);
    timer.schedule(timedTask, timedObject.getTaskDate());
    timerSet.add(timer);

  }

  public void shutdownTimers() {
    for (Timer t : timerSet) {
      t.cancel();
    }
  }

  public void retrivePersistentTimers() {
    List<TimedObject> timedObjects = timedObjectDao.doRetrieveAll("id");
    for (TimedObject timedObject : timedObjects) {
      timedObjectDao.doDelete(timedObject);
      scheduleTimedService(timedObject);
    }
  }

  private static class TimedTask extends TimerTask {

    public TimedTask(TimerService task, TimedObject timedObject, TimedObjectDao timedObjectDao,
                     Set<Timer> timedTaskSet, Timer timer) {

      this.task = task;
      this.timedObject = timedObject;
      this.timedObjectDao = timedObjectDao;
      this.timedTaskSet = timedTaskSet;
      this.timer = timer;
    }

    @Override
    public void run() {
      try {

        task.executeTimedTask(timedObject);
      } finally {
        timedObjectDao.doDelete(timedObject);
        timedTaskSet.remove(timer);
        timer.cancel();
        cancel();
      }

    }


    private TimerService task;
    private TimedObject timedObject;
    private TimedObjectDao timedObjectDao;
    private Set<Timer> timedTaskSet;
    private Timer timer;
  }


  private Map<String, TimerService> services;

  private TimedObjectDao timedObjectDao;

  private Set<Timer> timerSet;


}
