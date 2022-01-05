import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteServiceImpl;
import it.unisa.c02.moneyart.model.dao.AstaDaoImpl;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.RivenditaDaoImpl;
import it.unisa.c02.moneyart.model.dao.SegnalazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.production.GenericProducer;
import it.unisa.c02.moneyart.utils.production.Retriever;
import it.unisa.c02.moneyart.utils.timers.TimedObjecDaoImpl;
import it.unisa.c02.moneyart.utils.timers.TimedObjectDao;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import it.unisa.c02.moneyart.utils.timers.TimerService;
import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;


@WebListener
public class MainContext implements ServletContextListener {

  public void contextInitialized(ServletContextEvent sce) {
    System.out.println("Startup web application");
    DataSource ds = null;
    ServletContext context = sce.getServletContext();
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      ds = (DataSource) envCtx.lookup("jdbc/storage");

    } catch (NamingException e) {
      e.printStackTrace();
    }
    Map<Retriever.RetrieverKey, GenericProducer<?>> producers = inizializeProducers();
    Retriever.setProducers(producers);

    inizializeTimerService();

    context.setAttribute("DataSource", ds);
    System.out.println("DataSource creation: " + ds.toString());

  }

  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();

    DataSource ds = (DataSource) context.getAttribute("DataSource");
    context.removeAttribute("DataSource");
    TimerScheduler.getInstance().shutdownTimers();

    System.out.println("DataSource deletion: " + ds.toString());
  }

  private HashMap<Retriever.RetrieverKey, GenericProducer<?>> inizializeProducers() {
    HashMap<Retriever.RetrieverKey, GenericProducer<?>> producers = new HashMap<>();

    //creazione DataSource
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");


      DataSource ds = (DataSource) envCtx.lookup("jdbc/storage");
      GenericProducer<DataSource> dataSourceInstantiator = () -> ds;
      producers.put(new Retriever.RetrieverKey(DataSource.class.getName()),
          dataSourceInstantiator);


    } catch (NamingException e) {
      e.printStackTrace();
    }


    GenericProducer<NotificaDao> notificaProducer = () -> new NotificaDaoImpl();
    producers.put(new Retriever.RetrieverKey(NotificaDao.class.getName()), notificaProducer);
    GenericProducer<AstaDao> astaProducer = () -> new AstaDaoImpl();
    producers.put(new Retriever.RetrieverKey(AstaDao.class.getName()), astaProducer);
    GenericProducer<UtenteDao> utenteProducer = () -> new UtenteDaoImpl();
    producers.put(new Retriever.RetrieverKey(UtenteDao.class.getName()), utenteProducer);
    GenericProducer<OperaDao> operaProducer = () -> new OperaDaoImpl();
    producers.put(new Retriever.RetrieverKey(OperaDao.class.getName()), operaProducer);
    GenericProducer<RivenditaDao> rivenditaProducer = () -> new RivenditaDaoImpl();
    producers.put(new Retriever.RetrieverKey(RivenditaDao.class.getName()), rivenditaProducer);
    GenericProducer<SegnalazioneDao> segnalazioneProducer = () -> new SegnalazioneDaoImpl();
    producers.put(new Retriever.RetrieverKey(SegnalazioneDao.class.getName()),
        segnalazioneProducer);
    GenericProducer<PartecipazioneDao> partecipazioneProducer =
        () -> new PartecipazioneDaoImpl();
    producers.put(new Retriever.RetrieverKey(PartecipazioneDao.class.getName()),
        partecipazioneProducer);
    GenericProducer<UtenteService> utenteServiceProducer = () -> new UtenteServiceImpl();
    producers.put(new Retriever.RetrieverKey(UtenteService.class.getName()), utenteServiceProducer);
    GenericProducer<TimerScheduler> timerServiceProducer = () -> TimerScheduler.getInstance();
    producers.put(new Retriever.RetrieverKey(TimerScheduler.class.getName()), timerServiceProducer);
    GenericProducer<TimedObjectDao> timedObjectDao = () -> new TimedObjecDaoImpl();
    producers.put(new Retriever.RetrieverKey(TimedObjectDao.class.getName()), timedObjectDao);
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");


      DataSource ds = (DataSource) envCtx.lookup("jdbc/timer");
      GenericProducer<DataSource> dataSourceInstantiator = () -> ds;
      producers.put(new Retriever.RetrieverKey(DataSource.class.getName(), "Timer"),
          dataSourceInstantiator);


    } catch (NamingException e) {
      e.printStackTrace();
    }


    return producers;
  }

  private void inizializeTimerService() {
    TimerScheduler timerService = TimerScheduler.getInstance();
    TimerService avviaAsta = (timedObject) -> {
      System.out.println(" ");
    };
    timerService.registerTimedService("avviaAsta", avviaAsta);

    timerService.retrivePersistentTimers();
  }
}