import it.unisa.c02.moneyart.gestione.aste.service.AstaServiceImpl;
import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaServiceImpl;
import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneServiceImpl;
import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.opere.service.OperaServiceImpl;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteServiceImpl;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
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
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
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
import java.util.Date;
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
    System.out.println("-- Inizializzazione dei producer --");
    Map<Retriever.RetrieverKey, GenericProducer<?>> producers = initializeProducers();
    Retriever.setProducers(producers);

    System.out.println("-- Ripristino dei timer persistenti --");
    int timerRipristinati = initializeTimerService();
    System.out.println("-- " + timerRipristinati + " timer sono stati ripristinati --");

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

    //creazione dei DataSource
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      DataSource ds = (DataSource) envCtx.lookup("jdbc/storage");
      GenericProducer<DataSource> dataSourceInstantiator = () -> ds;
      producers.put(new Retriever.RetrieverKey(DataSource.class.getName()),
          dataSourceInstantiator);

      DataSource dsTimer = (DataSource) envCtx.lookup("jdbc/timer");
      GenericProducer<DataSource> dataSourceInstantiatorTimer = () -> dsTimer;
      producers.put(new Retriever.RetrieverKey(DataSource.class.getName(), "Timer"),
          dataSourceInstantiatorTimer);

    } catch (NamingException e) {
      e.printStackTrace();
    }

    //creazione dei producer per i dao
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

    GenericProducer<TimedObjectDao> timedObjectDao = () -> new TimedObjecDaoImpl();
    producers.put(new Retriever.RetrieverKey(TimedObjectDao.class.getName()), timedObjectDao);


    //creazione producer per i service
    GenericProducer<UtenteService> utenteServiceProducer = () -> new UtenteServiceImpl();
    producers.put(new Retriever.RetrieverKey(UtenteService.class.getName()), utenteServiceProducer);

    GenericProducer<OperaService> operaServiceProducer = () -> new OperaServiceImpl();
    producers.put(new Retriever.RetrieverKey(OperaService.class.getName()), operaServiceProducer);

    GenericProducer<RivenditaService> rivenditaServiceProducer = () -> new RivenditaServiceImpl();
    producers.put(new Retriever.RetrieverKey(RivenditaService.class.getName()),
        rivenditaServiceProducer);

    GenericProducer<NotificaService> notificaServiceProducer = () -> new NotificaServiceImpl();
    producers.put(new Retriever.RetrieverKey(NotificaService.class.getName()),
        notificaServiceProducer);

    GenericProducer<SegnalazioneService> segnalazioneServiceProducer =
        () -> new SegnalazioneServiceImpl();
    producers.put(new Retriever.RetrieverKey(SegnalazioneService.class.getName()),
        segnalazioneServiceProducer);

    //una volta creato Asta service decommentare
    /*
    GenericProducer<AstaService> astaServiceProducer = () -> new AstaServiceImpl();
    producers.put(new Retriever.RetrieverKey(AstaService.class.getName()),
        astaServiceProducer);

     */
    //crezione producer per utilities

    GenericProducer<AstaLockingSingleton> astaLockingSingletonProducer =
        () -> AstaLockingSingleton.retrieveIstance();
    producers.put(new Retriever.RetrieverKey(AstaLockingSingleton.class.getName()),
        astaLockingSingletonProducer);

    GenericProducer<TimerScheduler> timerServiceProducer = () -> TimerScheduler.getInstance();
    producers.put(new Retriever.RetrieverKey(TimerScheduler.class.getName()), timerServiceProducer);


    return producers;
  }

  private int initializeTimerService() {
    TimerScheduler timerService = TimerScheduler.getInstance();
    TimerService avviaAsta = new AstaServiceImpl();
    timerService.registerTimedService("avviaAsta", avviaAsta);
    TimerService terminaAsta = new AstaServiceImpl();
    timerService.registerTimedService("terminaAsta", avviaAsta);

    return timerService.retrivePersistentTimers();
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
    logger.info("-- Path immagini opere: "
            + filePath.concat("static\\demo\\") + " --");

    // POPOLAMENTO TABELLA UTENTE ------------------------

    Utente retrieved = null;
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
    admin.setId(1);

    if (utenteDao.doRetrieveByUsername("alfcan") == null) {
      utenteDao.doCreate(utente0);
    }
    utente0.setId(2);

    if (utenteDao.doRetrieveByUsername("XJustUnluckyX") == null) {
      utenteDao.doCreate(utente1);
    }
    utente1.setId(3);

    if (utenteDao.doRetrieveByUsername("shoyll") == null) {
      utenteDao.doCreate(utente2);
    }
    utente2.setId(4);

    if (utenteDao.doRetrieveByUsername("DG266") == null) {
      utenteDao.doCreate(utente3);
    }
    utente3.setId(5);

    if (utenteDao.doRetrieveByUsername("xDaryamo") == null) {
      utenteDao.doCreate(utente4);
    }
    utente4.setId(6);

    if (utenteDao.doRetrieveByUsername("MarioPeluso") == null) {
      utenteDao.doCreate(utente5);
    }
    utente5.setId(7);

    if (utenteDao.doRetrieveByUsername("AurySepe") == null) {
      utenteDao.doCreate(utente6);
    }
    utente6.setId(8);

    if (utenteDao.doRetrieveByUsername("stepzar") == null) {
      utenteDao.doCreate(utente7);
    }
    utente7.setId(9);

    logger.info("-- Popolamento della tabella \"utente\" terminato (1/7) --");

    // POPOLAMENTO TABELLA OPERA ------------------------

    Opera opera0 = new Opera(
            "Bears Deluxe #3742",
            "Descrizione",
            Opera.Stato.ALL_ASTA,
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\bears-deluxe-3742.png")))),
            utente0,
            utente0,
            null
    );

    Opera opera1 = new Opera(
            "The Shibosis",
            "Descrizione",
            Opera.Stato.IN_VENDITA,
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\shibosis.jpg")))),
            utente1,
            utente0,
            null
    );

    Opera opera2 = new Opera(
            "CupCat",
            "Descrizione",
            Opera.Stato.PREVENDITA,
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\cupcat.jpg")))),
            utente3,
            utente3,
            null
    );

    Opera opera3 = new Opera(
            "TIGXR",
            "Descrizione",
            Opera.Stato.PREVENDITA,
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\tiger.jpg")))),
            utente4,
            utente4,
            null
    );

    Opera opera4 = new Opera(
            "Bears Deluxe #3742",
            "Descrizione",
            Opera.Stato.ALL_ASTA,
            new SerialBlob(FileUtils.readFileToByteArray(
                    new File(filePath.concat("static\\demo\\bears-deluxe-3742.png")))),
            utente5,
            utente5,
            null
    );

    if (operaDao.doRetrieveById(1) == null) {
      operaDao.doCreate(opera0);
    }
    opera0.setId(1);

    if (operaDao.doRetrieveById(2) == null) {
      operaDao.doCreate(opera1);
    }
    opera1.setId(2);

    if (operaDao.doRetrieveById(3) == null) {
      operaDao.doCreate(opera2);
    }
    opera2.setId(3);

    if (operaDao.doRetrieveById(4) == null) {
      operaDao.doCreate(opera3);
    }
    opera3.setId(4);

    if (operaDao.doRetrieveById(5) == null) {
      operaDao.doCreate(opera4);
    }
    opera4.setId(5);

    logger.info("-- Popolamento della tabella \"opera\" terminato (2/7) --");

    // POPOLAMENTO TABELLA ASTA ------------------------

    long giornoMillis = 1 * 1000 * 60 * 60 * 24;

    Asta asta0 = new Asta(
            opera0,
            new Date(System.currentTimeMillis() - giornoMillis * 1),
            new Date(System.currentTimeMillis() + giornoMillis * 6),
            Asta.Stato.IN_CORSO
    );

    Asta asta1 = new Asta(
            opera4,
            new Date(System.currentTimeMillis()),
            new Date(System.currentTimeMillis() + giornoMillis * 7),
            Asta.Stato.IN_CORSO
    );

    if (astaDao.doRetrieveById(1) == null) {
      astaDao.doCreate(asta0);
    }
    asta0.setId(1);

    if (astaDao.doRetrieveById(2) == null) {
      astaDao.doCreate(asta1);
    }
    asta1.setId(2);

    logger.info("-- Popolamento della tabella \"asta\" terminato (3/7) --");

    // POPOLAMENTO TABELLA RIVENDITA ------------------------

    Rivendita rivendita0 = new Rivendita(
            opera1,
            Rivendita.Stato.IN_CORSO,
            999.99d
    );

    if (rivenditaDao.doRetrieveById(1) == null) {
      rivenditaDao.doCreate(rivendita0);
    }
    rivendita0.setId(1);

    logger.info("-- Popolamento della tabella \"rivendita\" terminato (4/7) --");

    // POPOLAMENTO TABELLA PARTECIPAZIONE ------------------------

    Partecipazione partecipazione0 = new Partecipazione(
            asta0,
            utente3,
            485.99d
    );

    Partecipazione partecipazione1 = new Partecipazione(
            asta0,
            utente4,
            486d
    );

    Partecipazione partecipazione2 = new Partecipazione(
            asta0,
            utente5,
            499.99d
    );

    if (partecipazioneDao.doRetrieveById(1) == null) {
      partecipazioneDao.doCreate(partecipazione0);
    }
    partecipazione0.setId(1);

    if (partecipazioneDao.doRetrieveById(2) == null) {
      partecipazioneDao.doCreate(partecipazione1);
    }
    partecipazione1.setId(2);

    if (partecipazioneDao.doRetrieveById(3) == null) {
      partecipazioneDao.doCreate(partecipazione2);
    }
    partecipazione2.setId(3);

    logger.info("-- Popolamento della tabella \"partecipazione\" terminato (5/7) --");

    // POPOLAMENTO TABELLA NOTIFICA ------------------------

    Rivendita noRivendita = new Rivendita();

    Notifica notifica0 = new Notifica(
            utente3,
            asta0,
            noRivendita,
            Notifica.Tipo.SUPERATO,
            "Contenuto della notifica.",
            false
    );

    Notifica notifica1 = new Notifica(
            utente4,
            asta0,
            noRivendita,
            Notifica.Tipo.SUPERATO,
            "Contenuto della notifica.",
            true
    );

    if (notificaDao.doRetrieveById(1) == null) {
      notificaDao.doCreate(notifica0);
    }
    notifica0.setId(1);

    if (notificaDao.doRetrieveById(2) == null) {
      notificaDao.doCreate(notifica1);
    }
    notifica1.setId(2);

    logger.info("-- Popolamento della tabella \"notifica\" terminato (6/7) --");

    // POPOLAMENTO TABELLA SEGNALAZIONE ------------------------

    Segnalazione segnalazione0 = new Segnalazione(
            asta1,
            utente5.getUsername() + " ha copiato l'opera di "
                    + utente0.getUsername() + "attualmente all'asta.",
            false
    );

    if (segnalazioneDao.doRetrieveById(1) == null) {
      segnalazioneDao.doCreate(segnalazione0);
    }
    segnalazione0.setId(1);

    logger.info("-- Popolamento della tabella \"segnalazione\" terminato (7/7) --");
  }

  private static final Logger logger = Logger.getLogger("MainContext.class");
}