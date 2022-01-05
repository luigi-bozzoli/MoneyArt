import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteServiceImpl;
import it.unisa.c02.moneyart.model.beans.Utente;
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
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import org.apache.commons.io.FileUtils;

/**
 * permette di inizializzare il sistema all'avvio.
 */
@WebListener
public class MainContext implements ServletContextListener {

  /**
   * Inizializza il sistema settando le implementazioni delle interfacce,
   * inizializza i servizi attivabili tramite un timer,
   * riattiva i timer presenti nella memoria persistente.
   *
   * @param sce l'evento di attivazione del sistema
   */
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
    Map<Retriever.RetrieverKey, GenericProducer<?>> producers = initializeProducers();
    Retriever.setProducers(producers);

    initializeTimerService();

    context.setAttribute("DataSource", ds);
    System.out.println("DataSource creation: " + ds.toString());

    try {
      populateDatabase(sce.getServletContext().getRealPath(""));
    } catch (NoSuchAlgorithmException | IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Effettua operazioni di deallocazione di risorse prima della terminazione del sistema
   * dealloca il datasource e disattiva i timer attualmente attivi.
   *
   * @param sce l'evento di attivazione del sistema
   */
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();

    DataSource ds = (DataSource) context.getAttribute("DataSource");
    context.removeAttribute("DataSource");
    TimerScheduler.getInstance().shutdownTimers();

    System.out.println("DataSource deletion: " + ds.toString());
  }

  private HashMap<Retriever.RetrieverKey, GenericProducer<?>> initializeProducers() {
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

  private void initializeTimerService() {
    TimerScheduler timerService = TimerScheduler.getInstance();
    TimerService avviaAsta = (serializable) -> {
      System.out.println(serializable);
    };
    timerService.registerTimedService("avviaAsta", avviaAsta);

    timerService.retrivePersistentTimers();
  }

  private void populateDatabase(String filePath)
          throws NoSuchAlgorithmException, IOException, SQLException {
    // Necessario per salvare le password crittografate
    MessageDigest md = MessageDigest.getInstance("SHA-256");

    // Recupero Dao necessari per il popolamento del database
    UtenteDao utenteDao = Retriever.getIstance(UtenteDao.class);
    OperaDao operaDao = Retriever.getIstance(OperaDao.class);
    AstaDao astaDao = Retriever.getIstance(AstaDao.class);
    RivenditaDao rivenditaDao = Retriever.getIstance(RivenditaDao.class);
    PartecipazioneDao partecipazioneDao = Retriever.getIstance(PartecipazioneDao.class);
    NotificaDao notificaDao = Retriever.getIstance(NotificaDao.class);
    SegnalazioneDao segnalazioneDao = Retriever.getIstance(SegnalazioneDao.class);

    logger.info("-- Inizio popolamento database --");
    logger.info("-- Path immagini profilo utente: "
            + filePath.concat("static\\demo\\profilePics\\") + " --");

    // Attualmente nessun utente segue un altro utente ("followed" ha id = null)
    // TODO: Impostare dei "follow" di esempio
    Utente followed = new Utente();

    // Le password non rispettano le regex dei documenti
    // TODO: Scrivere delle password valide
    Utente admin = new Utente(
            "Money",
            "Art",
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\profilePics\\gattoProfilePic.jpg")))),
            "admin@moneyart.it",
            "admin",
            followed,
            md.digest("admin".getBytes()),
            0d
    );

    Utente utente0 = new Utente(
            "Alfonso",
            "Cannavale",
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\profilePics\\gattoProfilePic.jpg")))),
            "alfonso.cannavale@gmail.com",
            "alfcan",
            followed,
            md.digest("pippo123".getBytes()),
            1000d
    );

    Utente utente1 = new Utente(
            "Nicolò",
            "Delogu",
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\profilePics\\gattoProfilePic.jpg")))),
            "nicolò.delogu@gmail.com",
            "XJustUnluckyX",
            followed,
            md.digest("pippo123".getBytes()),
            1500d
    );

    Utente utente2 = new Utente(
            "Michael",
            "De Santis",
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\profilePics\\gattoProfilePic.jpg")))),
            "michael.desantis@gmail.com",
            "shoyll",
            followed,
            md.digest("pippo123".getBytes()),
            2000d
    );

    Utente utente3 = new Utente(
            "Daniele",
            "Galloppo",
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\profilePics\\gattoProfilePic.jpg")))),
            "daniele.galloppo@gmail.com",
            "DG266",
            followed,
            md.digest("pippo123".getBytes()),
            2500d
    );

    Utente utente4 = new Utente(
            "Dario",
            "Mazza",
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\profilePics\\gattoProfilePic.jpg")))),
            "dario.mazza@gmail.com",
            "xDaryamo",
            followed,
            md.digest("pippo123".getBytes()),
            3000d
    );

    Utente utente5 = new Utente(
            "Mario",
            "Peluso",
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\profilePics\\gattoProfilePic.jpg")))),
            "mario.peluso@gmail.com",
            "MarioPeluso",
            followed,
            md.digest("pippo123".getBytes()),
            100d
    );

    Utente utente6 = new Utente(
            "Aurelio",
            "Sepe",
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\profilePics\\gattoProfilePic.jpg")))),
            "aurelio.sepe@gmail.com",
            "AurySepe",
            followed,
            md.digest("pippo123".getBytes()),
            200d
    );

    Utente utente7 = new Utente(
            "Stefano",
            "Zarro",
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\profilePics\\gattoProfilePic.jpg")))),
            "stefano.zarro@gmail.com",
            "stepzar",
            followed,
            md.digest("pippo123".getBytes()),
            10d
    );

    if (utenteDao.doRetrieveByUsername("admin") == null) {
      utenteDao.doCreate(admin);
    }

    if (utenteDao.doRetrieveByUsername("alfcan") == null) {
      utenteDao.doCreate(utente0);
    }

    if (utenteDao.doRetrieveByUsername("XJustUnluckyX") == null) {
      utenteDao.doCreate(utente1);
    }

    if (utenteDao.doRetrieveByUsername("shoyll") == null) {
      utenteDao.doCreate(utente2);
    }

    if (utenteDao.doRetrieveByUsername("DG266") == null) {
      utenteDao.doCreate(utente3);
    }

    if (utenteDao.doRetrieveByUsername("xDaryamo") == null) {
      utenteDao.doCreate(utente4);
    }

    if (utenteDao.doRetrieveByUsername("MarioPeluso") == null) {
      utenteDao.doCreate(utente5);
    }

    if (utenteDao.doRetrieveByUsername("AurySepe") == null) {
      utenteDao.doCreate(utente6);
    }

    if (utenteDao.doRetrieveByUsername("stepzar") == null) {
      utenteDao.doCreate(utente7);
    }

    logger.info("-- Popolamento della tabella \"utente\" terminato (1/7) --");

    // TODO: Popolamento delle altre tabelle
  }

  private Logger logger = Logger.getLogger("MainContext.class");
}