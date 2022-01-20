package integration.gestione.service;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.AstaDaoImpl;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.timers.TimedObject;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AstaServiceImplIntegrationTest {
  private static DataSource dataSource;

  private AstaService astaService;

  private AstaDao astaDao;

  private NotificaDao notificaDao;

  private OperaDao operaDao;

  private UtenteDao utenteDao;

  private PartecipazioneDao partecipazioneDao;

  private TimerScheduler timerScheduler;

  private AstaLockingSingleton astaLockingSingleton;


  @BeforeAll
  public static void generalSetUp() throws SQLException, FileNotFoundException {
    Context initCtx;
    Context envCtx = null;
    try {
      initCtx = new InitialContext();
      envCtx = (Context) initCtx.lookup("java:comp/env");
      dataSource = (DataSource) envCtx.lookup("jdbc/storage");
    } catch (NamingException e) {
      TomcatJNDI tomcatJNDI = new TomcatJNDI();
      tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
      tomcatJNDI.start();
    }
    if (envCtx == null) {
      try {
        initCtx = new InitialContext();
        envCtx = (Context) initCtx.lookup("java:comp/env");
        dataSource = (DataSource) envCtx.lookup("jdbc/storage");
      } catch (NamingException e) {
        e.printStackTrace();
      }
    }
  }

  @BeforeEach
  public void setUp()
      throws SQLException, FileNotFoundException, InvocationTargetException, InstantiationException,
      IllegalAccessException {
    notificaDao = new NotificaDaoImpl(dataSource);
    operaDao = new OperaDaoImpl(dataSource);
    utenteDao = new UtenteDaoImpl(dataSource);
    partecipazioneDao = new PartecipazioneDaoImpl(dataSource);
    astaDao = new AstaDaoImpl(dataSource);

    Constructor constructor = TimerScheduler.class.getDeclaredConstructors()[1];

    constructor.setAccessible(true);

    timerScheduler = (TimerScheduler) constructor.newInstance();

    astaLockingSingleton = AstaLockingSingleton.retrieveIstance();
    astaService =
        new AstaServiceImpl(astaDao, operaDao, utenteDao, partecipazioneDao, timerScheduler,
            astaLockingSingleton, notificaDao);
    timerScheduler.registerTimedService("avviaAsta", (AstaServiceImpl) astaService);
    timerScheduler.registerTimedService("terminaAsta", (AstaServiceImpl) astaService);
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader =
        new BufferedReader(new FileReader("./src/test/database/test_partecipazione.sql"));
    runner.runScript(reader);
    connection.close();
  }

  @AfterEach
  public void destroy() throws SQLException, FileNotFoundException {
    Connection connection = dataSource.getConnection();
    ScriptRunner runner = new ScriptRunner(connection);
    runner.setLogWriter(null);
    Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_database.sql"));
    runner.runScript(reader);
    connection.close();
  }


  @Test
  void getAuction() {
    Asta asta = astaDao.doRetrieveById(2);
    Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
    opera.setArtista(utenteDao.doRetrieveById(opera.getArtista().getId()));
    Utente utente2 = utenteDao.doRetrieveById(3);
    Utente utente3 = utenteDao.doRetrieveById(4);

    Partecipazione partecipazione1 = new Partecipazione(asta, utente2, 100);
    Partecipazione partecipazione2 = new Partecipazione(asta, utente3, 300);
    Partecipazione partecipazione3 = new Partecipazione(asta, utente3, 500);
    partecipazioneDao.doCreate(partecipazione1);
    partecipazioneDao.doCreate(partecipazione2);
    partecipazioneDao.doCreate(partecipazione3);

    opera.getArtista()
        .setnFollowers(utenteDao.doRetrieveFollowersByUserId(opera.getArtista().getId()).size());

    Asta result = astaService.getAuction(2);

    Assertions.assertEquals(asta.getId(), result.getId());
    Assertions.assertEquals(opera, result.getOpera());
    Assertions.assertEquals(3, result.getPartecipazioni().size());


  }


  @Test
  void getAllAuctions() {

    List<Asta> aste = astaService.getAllAuctions();
    Assertions.assertEquals(4, aste.size());
    for (int i = 0; i < 4; i++) {
      Asta asta = astaDao.doRetrieveById(i + 1);
      Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
      opera.setArtista(utenteDao.doRetrieveById(opera.getArtista().getId()));
      opera.getArtista()
          .setnFollowers(utenteDao.doRetrieveFollowersByUserId(opera.getArtista().getId()).size());
      Assertions.assertEquals(asta.getId(), aste.get(i).getId());
      Assertions.assertEquals(opera, aste.get(i).getOpera());
      Assertions.assertEquals(opera.getArtista().getnFollowers(), aste.get(i).getOpera().getArtista().getnFollowers());
    }

  }

  @Nested
  class PriceSortTest{
    @ParameterizedTest
    @ValueSource(strings = {"CREATA","TERMINATA","IN_CORSO","ELIMINATA"})
    void getAuctionsSortedByPriceAsc(String statoS) {
      Utente utente3 = utenteDao.doRetrieveById(4);
      Asta asta1 = astaDao.doRetrieveById(1);
      Asta asta3 = astaDao.doRetrieveById(3);
      Asta asta4 = astaDao.doRetrieveById(4);

      Partecipazione partecipazione1 = new Partecipazione(asta4, utente3, 100);
      Partecipazione partecipazione2 = new Partecipazione(asta1, utente3, 150);
      Partecipazione partecipazione3 = new Partecipazione(asta4, utente3, 200);
      Partecipazione partecipazione4 = new Partecipazione(asta3, utente3, 250);
      partecipazioneDao.doCreate(partecipazione1);
      partecipazioneDao.doCreate(partecipazione2);
      partecipazioneDao.doCreate(partecipazione3);
      partecipazioneDao.doCreate(partecipazione4);



      Asta.Stato stato = Asta.Stato.valueOf(statoS);
      int[] ids;
      if(stato.equals(Asta.Stato.CREATA)){
        ids = new int[] {};
      }else if(stato.equals(Asta.Stato.TERMINATA)){
        ids = new int[] {1};
      }else if(stato.equals(Asta.Stato.IN_CORSO)){
        ids = new int[] {2,4,3};
      }else {
        ids = new int[] {};
      }

      List<Asta> aste = astaService.getAuctionsSortedByPrice("ASC",stato);
      Assertions.assertEquals(ids.length,aste.size());
      int k = 0;
      for (int i : ids){

        Asta asta = astaDao.doRetrieveById(i);
        Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
        opera.setArtista(utenteDao.doRetrieveById(opera.getArtista().getId()));
        opera.getArtista()
            .setnFollowers(utenteDao.doRetrieveFollowersByUserId(opera.getArtista().getId()).size());
        Assertions.assertEquals(stato,aste.get(k).getStato());
        Assertions.assertEquals(asta.getId(), aste.get(k).getId());
        Assertions.assertEquals(opera, aste.get(k).getOpera());
        Assertions.assertEquals(opera.getArtista().getnFollowers(), aste.get(k).getOpera().getArtista().getnFollowers());
        k++;

      }
    }
    @ParameterizedTest
    @ValueSource(strings = {"CREATA","TERMINATA","IN_CORSO","ELIMINATA"})
    void getAuctionsSortedByPriceDesc(String statoS) {
      Utente utente3 = utenteDao.doRetrieveById(4);
      Asta asta1 = astaDao.doRetrieveById(1);
      Asta asta3 = astaDao.doRetrieveById(3);
      Asta asta4 = astaDao.doRetrieveById(4);

      Partecipazione partecipazione1 = new Partecipazione(asta4, utente3, 100);
      Partecipazione partecipazione2 = new Partecipazione(asta1, utente3, 150);
      Partecipazione partecipazione3 = new Partecipazione(asta4, utente3, 200);
      Partecipazione partecipazione4 = new Partecipazione(asta3, utente3, 250);
      partecipazioneDao.doCreate(partecipazione1);
      partecipazioneDao.doCreate(partecipazione2);
      partecipazioneDao.doCreate(partecipazione3);
      partecipazioneDao.doCreate(partecipazione4);



      Asta.Stato stato = Asta.Stato.valueOf(statoS);
      int[] ids;
      if(stato.equals(Asta.Stato.CREATA)){
        ids = new int[] {};
      }else if(stato.equals(Asta.Stato.TERMINATA)){
        ids = new int[] {1};
      }else if(stato.equals(Asta.Stato.IN_CORSO)){
        ids = new int[] {3,4,2};
      }else {
        ids = new int[] {};
      }

      List<Asta> aste = astaService.getAuctionsSortedByPrice("DESC",stato);
      Assertions.assertEquals(ids.length,aste.size());
      int k = 0;
      for (int i : ids){

        Asta asta = astaDao.doRetrieveById(i);
        Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
        opera.setArtista(utenteDao.doRetrieveById(opera.getArtista().getId()));
        opera.getArtista()
            .setnFollowers(utenteDao.doRetrieveFollowersByUserId(opera.getArtista().getId()).size());
        Assertions.assertEquals(stato,aste.get(k).getStato());
        Assertions.assertEquals(asta.getId(), aste.get(k).getId());
        Assertions.assertEquals(opera, aste.get(k).getOpera());
        Assertions.assertEquals(opera.getArtista().getnFollowers(), aste.get(k).getOpera().getArtista().getnFollowers());
        k++;

      }
    }

  }


  @Nested
  class FollowerSortTest{

    @ParameterizedTest
    @ValueSource(strings = {"CREATA","TERMINATA","IN_CORSO","ELIMINATA"})
    void getAuctionsSortedByArtistFollowersAsc(String statoS) {
      Utente alf = utenteDao.doRetrieveById(2);
      Utente mar = utenteDao.doRetrieveById(7);
      Utente aur = utenteDao.doRetrieveById(8);
      alf.setSeguito(aur);
      mar.setSeguito(aur);
      aur.setSeguito(alf);
      utenteDao.doUpdate(aur);
      utenteDao.doUpdate(alf);
      utenteDao.doUpdate(mar);

      Asta.Stato stato = Asta.Stato.valueOf(statoS);
      int[] ids;
      if (stato.equals(Asta.Stato.CREATA)) {
        ids = new int[] {};
      } else if (stato.equals(Asta.Stato.TERMINATA)) {
        ids = new int[] {1};
      } else if (stato.equals(Asta.Stato.IN_CORSO)) {
        ids = new int[] {3,2,4};
      } else {
        ids = new int[] {};
      }

      List<Asta> aste = astaService.getAuctionsSortedByArtistFollowers("ASC", stato);
      Assertions.assertEquals(ids.length, aste.size());
      int k = 0;
      for (int i : ids) {

        Asta asta = astaDao.doRetrieveById(i);
        Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
        opera.setArtista(utenteDao.doRetrieveById(opera.getArtista().getId()));
        opera.getArtista()
            .setnFollowers(utenteDao.doRetrieveFollowersByUserId(opera.getArtista().getId()).size());
        Assertions.assertEquals(stato, aste.get(k).getStato());
        Assertions.assertEquals(asta.getId(), aste.get(k).getId());
        Assertions.assertEquals(opera, aste.get(k).getOpera());
        Assertions.assertEquals(opera.getArtista().getnFollowers(),
            aste.get(k).getOpera().getArtista().getnFollowers());
        k++;

      }
    }

    @ParameterizedTest
    @ValueSource(strings = {"CREATA","TERMINATA","IN_CORSO","ELIMINATA"})
    void getAuctionsSortedByArtistFollowersDesc(String statoS) throws InterruptedException {
      Utente alf = utenteDao.doRetrieveById(2);
      Utente mar = utenteDao.doRetrieveById(7);
      Utente aur = utenteDao.doRetrieveById(8);
      alf.setSeguito(aur);
      mar.setSeguito(aur);
      aur.setSeguito(alf);
      utenteDao.doUpdate(aur);
      utenteDao.doUpdate(alf);
      utenteDao.doUpdate(mar);

      Asta.Stato stato = Asta.Stato.valueOf(statoS);
      int[] ids;
      if (stato.equals(Asta.Stato.CREATA)) {
        ids = new int[] {};
      } else if (stato.equals(Asta.Stato.TERMINATA)) {
        ids = new int[] {1};
      } else if (stato.equals(Asta.Stato.IN_CORSO)) {
        ids = new int[] {4,2,3};
      } else {
        ids = new int[] {};
      }

      List<Asta> aste = astaService.getAuctionsSortedByArtistFollowers("DESC", stato);
      Assertions.assertEquals(ids.length, aste.size());
      int k = 0;
      for (int i : ids) {

        Asta asta = astaDao.doRetrieveById(i);
        Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
        opera.setArtista(utenteDao.doRetrieveById(opera.getArtista().getId()));
        opera.getArtista()
            .setnFollowers(utenteDao.doRetrieveFollowersByUserId(opera.getArtista().getId()).size());
        Assertions.assertEquals(stato, aste.get(k).getStato());
        Assertions.assertEquals(asta.getId(), aste.get(k).getId());
        Assertions.assertEquals(opera, aste.get(k).getOpera());
        Assertions.assertEquals(opera.getArtista().getnFollowers(),
            aste.get(k).getOpera().getArtista().getnFollowers());
        k++;

      }
    }
  }



  @Nested
  class TimeSortTest{
    @ParameterizedTest
    @ValueSource(strings = {"CREATA","TERMINATA","IN_CORSO","ELIMINATA"})
    void getAuctionsSortedByExpirationTimeAsc(String statoS) {
      Asta.Stato stato = Asta.Stato.valueOf(statoS);
      int[] ids;
      if (stato.equals(Asta.Stato.CREATA)) {
        ids = new int[] {};
      } else if (stato.equals(Asta.Stato.TERMINATA)) {
        ids = new int[] {1};
      } else if (stato.equals(Asta.Stato.IN_CORSO)) {
        ids = new int[] {2,3,4};
      } else {
        ids = new int[] {};
      }

      List<Asta> aste = astaService.getAuctionsSortedByExpirationTime("ASC", stato);
      Assertions.assertEquals(ids.length, aste.size());
      int k = 0;
      for (int i : ids) {

        Asta asta = astaDao.doRetrieveById(i);
        Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
        opera.setArtista(utenteDao.doRetrieveById(opera.getArtista().getId()));
        opera.getArtista()
            .setnFollowers(utenteDao.doRetrieveFollowersByUserId(opera.getArtista().getId()).size());
        Assertions.assertEquals(stato, aste.get(k).getStato());
        Assertions.assertEquals(asta.getId(), aste.get(k).getId());
        Assertions.assertEquals(opera, aste.get(k).getOpera());
        Assertions.assertEquals(opera.getArtista().getnFollowers(),
            aste.get(k).getOpera().getArtista().getnFollowers());
        k++;

      }
    }

    @ParameterizedTest
    @ValueSource(strings = {"CREATA","TERMINATA","IN_CORSO","ELIMINATA"})
    void getAuctionsSortedByExpirationTimeDesc(String statoS) {
      Asta.Stato stato = Asta.Stato.valueOf(statoS);
      int[] ids;
      if (stato.equals(Asta.Stato.CREATA)) {
        ids = new int[] {};
      } else if (stato.equals(Asta.Stato.TERMINATA)) {
        ids = new int[] {1};
      } else if (stato.equals(Asta.Stato.IN_CORSO)) {
        ids = new int[] {4,3,2};
      } else {
        ids = new int[] {};
      }

      List<Asta> aste = astaService.getAuctionsSortedByExpirationTime("DESC", stato);
      Assertions.assertEquals(ids.length, aste.size());
      int k = 0;
      for (int i : ids) {

        Asta asta = astaDao.doRetrieveById(i);
        Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
        opera.setArtista(utenteDao.doRetrieveById(opera.getArtista().getId()));
        opera.getArtista()
            .setnFollowers(utenteDao.doRetrieveFollowersByUserId(opera.getArtista().getId()).size());
        Assertions.assertEquals(stato, aste.get(k).getStato());
        Assertions.assertEquals(asta.getId(), aste.get(k).getId());
        Assertions.assertEquals(opera, aste.get(k).getOpera());
        Assertions.assertEquals(opera.getArtista().getnFollowers(),
            aste.get(k).getOpera().getArtista().getnFollowers());
        k++;

      }
    }
  }



  @ParameterizedTest
  @ValueSource(strings = {"CREATA","TERMINATA","IN_CORSO","ELIMINATA"})
  void getAuctionsByState(String statoS) {
    Asta.Stato stato = Asta.Stato.valueOf(statoS);
    int[] ids;
    if(stato.equals(Asta.Stato.CREATA)){
      ids = new int[] {};
    }else if(stato.equals(Asta.Stato.TERMINATA)){
      ids = new int[] {1};
    }else if(stato.equals(Asta.Stato.IN_CORSO)){
      ids = new int[] {2,3,4};
    }else {
      ids = new int[] {};
    }

    List<Asta> aste = astaService.getAuctionsByState(stato);
    Assertions.assertEquals(ids.length,aste.size());
    int k = 0;
    for (int i : ids){

      Asta asta = astaDao.doRetrieveById(i);
      Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
      opera.setArtista(utenteDao.doRetrieveById(opera.getArtista().getId()));
      opera.getArtista()
          .setnFollowers(utenteDao.doRetrieveFollowersByUserId(opera.getArtista().getId()).size());
      Assertions.assertEquals(stato,aste.get(k).getStato());
      Assertions.assertEquals(asta.getId(), aste.get(k).getId());
      Assertions.assertEquals(opera, aste.get(k).getOpera());
      Assertions.assertEquals(opera.getArtista().getnFollowers(), aste.get(k).getOpera().getArtista().getnFollowers());
      k++;

    }
  }

  @Nested
  class PartecipateAstaTest {

    @Test
    public void NotActiveAuction() {
      Asta asta = astaDao.doRetrieveById(1);
      Utente utente = utenteDao.doRetrieveById(2);
      Assertions.assertFalse(astaService.partecipateAuction(utente, asta, 100));
    }

    @Test
    public void saldoInsufficenteTest() {
      Asta asta = astaDao.doRetrieveById(2);
      Utente utente = utenteDao.doRetrieveById(2);
      Assertions.assertFalse(astaService.partecipateAuction(utente, asta, 10000));
    }

    @Test
    public void OffertaInsufficente() {
      Asta asta = astaDao.doRetrieveById(2);
      Utente utente = utenteDao.doRetrieveById(2);
      Utente utente2 = utenteDao.doRetrieveById(3);
      Partecipazione partecipazione = new Partecipazione(asta, utente2, 1500);
      partecipazioneDao.doCreate(partecipazione);
      Assertions.assertFalse(astaService.partecipateAuction(utente, asta, 200));
    }

    @Test
    public void offertaRiuscitaNessuAltroOfferente() {
      Asta asta = astaDao.doRetrieveById(2);
      Utente utente = utenteDao.doRetrieveById(2);
      double saldoPrecedente = utente.getSaldo();
      Assertions.assertTrue(astaService.partecipateAuction(utente, asta, 100));
      utente = utenteDao.doRetrieveById(2);
      Assertions.assertEquals(saldoPrecedente - 100, utente.getSaldo());
    }

    @Test
    public void offertaRiuscitaNuovoMiglioreOfferente() {
      Asta asta = astaDao.doRetrieveById(2);
      Utente utente = utenteDao.doRetrieveById(2);
      Utente utente2 = utenteDao.doRetrieveById(3);
      double saldoPrecedente = utente.getSaldo();
      int notifichePrima = notificaDao.doRetrieveAllByUserId(utente2.getId()).size();
      Partecipazione partecipazione = new Partecipazione(asta, utente2, 100);
      partecipazioneDao.doCreate(partecipazione);
      Assertions.assertTrue(astaService.partecipateAuction(utente, asta, 200));
      utente = utenteDao.doRetrieveById(2);
      Assertions.assertEquals(saldoPrecedente - 200, utente.getSaldo());
      Assertions.assertEquals(notifichePrima + 1,
          notificaDao.doRetrieveAllByUserId(utente2.getId()).size());

    }

  }

  @Nested
  public class AddAstaTest {

    @Test
    public void addFailInizioSuperato() {
      Opera opera = operaDao.doRetrieveById(1);

      Asta asta = new Asta(opera, creaData(-1), creaData(4),
          Asta.Stato.CREATA);
      Assertions.assertFalse(astaService.addAsta(asta));
    }

    @Test
    public void addFailFinePrecedenteInizio() throws ParseException {

      Opera opera = operaDao.doRetrieveById(1);

      Asta asta = new Asta(opera, creaData(0), creaData(-1), Asta.Stato.CREATA);
      Assertions.assertFalse(astaService.addAsta(asta));
    }

    @Test
    public void addFailOperaNotPrevendita() {
      Opera opera = operaDao.doRetrieveById(1);

      Asta asta = new Asta(opera, creaData(0), creaData(2), Asta.Stato.CREATA);
      Assertions.assertFalse(astaService.addAsta(asta));
    }

    @Test
    public void addSuccessGiornoCorrente() throws InterruptedException {
      Opera opera = operaDao.doRetrieveById(3);

      Asta asta = new Asta(opera, creaData(0), creaData(2), Asta.Stato.CREATA);
      Assertions.assertTrue(astaService.addAsta(asta));
      Thread.sleep(1000);
      Asta result = astaDao.doRetrieveById(asta.getId());
      Assertions.assertNotNull(result);
      Assertions.assertEquals(result.getStato(), Asta.Stato.IN_CORSO);

    }

    @Test
    public void addSuccessGiornoFuturo() {
      Opera opera = operaDao.doRetrieveById(3);

      Asta asta = new Asta(opera, creaData(1), creaData(3), Asta.Stato.CREATA);
      Assertions.assertTrue(astaService.addAsta(asta));
      Asta result = astaDao.doRetrieveById(asta.getId());
      Assertions.assertNotNull(result);
      Assertions.assertEquals(result.getStato(), Asta.Stato.CREATA);

    }
  }


  @Nested
  class RemoveAstaTest {

    @Test
    public void failRemoveAstaTerminata() {
      Asta asta = astaDao.doRetrieveById(1);
      Assertions.assertFalse(astaService.removeAsta(asta));
    }

    @Test
    public void failRemoveAstaAnnullata() {
      Asta asta = astaDao.doRetrieveById(1);
      asta.setStato(Asta.Stato.ELIMINATA);
      astaDao.doUpdate(asta);
      Assertions.assertFalse(astaService.removeAsta(asta));
    }

    @Test
    public void successRemoveAsta() {
      Asta asta = astaDao.doRetrieveById(2);
      Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
      Utente utente2 = utenteDao.doRetrieveById(3);
      Partecipazione partecipazione = new Partecipazione(asta, utente2, 100);
      partecipazioneDao.doCreate(partecipazione);
      int numeroNotificheProprietario =
          notificaDao.doRetrieveAllByUserId(opera.getArtista().getId()).size();
      int numeroNotificheVincitore = notificaDao.doRetrieveAllByUserId(3).size();

      Assertions.assertTrue(astaService.removeAsta(asta));

      asta = astaDao.doRetrieveById(2);
      Assertions.assertEquals(Asta.Stato.ELIMINATA, asta.getStato());
      Assertions.assertEquals(numeroNotificheProprietario + 1,
          notificaDao.doRetrieveAllByUserId(opera.getArtista().getId()).size());
      Assertions.assertEquals(numeroNotificheVincitore + 1,
          notificaDao.doRetrieveAllByUserId(3).size());

    }
  }

  @Nested
  class AnnullaTest {
    @Test
    public void failAnnullaAstaTerminata() {
      Asta asta = astaDao.doRetrieveById(1);
      Assertions.assertFalse(astaService.annullaAsta(asta));
    }

    @Test
    public void failAnnullaAstaAnnullata() {
      Asta asta = astaDao.doRetrieveById(1);
      asta.setStato(Asta.Stato.ELIMINATA);
      astaDao.doUpdate(asta);
      Assertions.assertFalse(astaService.annullaAsta(asta));
    }

    @Test
    public void successAnnullaAsta() {
      Asta asta = astaDao.doRetrieveById(2);
      Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
      Utente utente2 = utenteDao.doRetrieveById(3);
      Partecipazione partecipazione = new Partecipazione(asta, utente2, 100);
      partecipazioneDao.doCreate(partecipazione);
      int numeroNotificheVincitore = notificaDao.doRetrieveAllByUserId(3).size();

      Assertions.assertTrue(astaService.annullaAsta(asta));

      asta = astaDao.doRetrieveById(2);
      Assertions.assertEquals(Asta.Stato.ELIMINATA, asta.getStato());
      Assertions.assertEquals(numeroNotificheVincitore + 1,
          notificaDao.doRetrieveAllByUserId(3).size());

    }
  }


  @Nested
  class BestOfferTest {

    @Test
    public void emptyBestOffer() {

      Asta asta = astaDao.doRetrieveById(2);
      Assertions.assertNull(astaService.bestOffer(asta));
    }

    @Test
    public void getBestOffer() {

      Asta asta = astaDao.doRetrieveById(2);
      Utente utente2 = utenteDao.doRetrieveById(3);
      Utente utente3 = utenteDao.doRetrieveById(4);
      Partecipazione partecipazione1 = new Partecipazione(asta, utente2, 100);
      Partecipazione partecipazione2 = new Partecipazione(asta, utente3, 300);
      Partecipazione partecipazione3 = new Partecipazione(asta, utente3, 500);
      partecipazioneDao.doCreate(partecipazione1);
      partecipazioneDao.doCreate(partecipazione2);
      partecipazioneDao.doCreate(partecipazione3);
      Assertions.assertEquals(partecipazione3.getId(), astaService.bestOffer(asta).getId());
    }
  }


  @Test
  void getWonAuctions() {
    Asta asta1 = astaDao.doRetrieveById(1);
    Asta asta2 = astaDao.doRetrieveById(2);
    Asta asta3 = astaDao.doRetrieveById(3);
    asta1.setStato(Asta.Stato.TERMINATA);
    asta2.setStato(Asta.Stato.TERMINATA);
    astaDao.doUpdate(asta1);
    astaDao.doUpdate(asta2);
    Utente utente3 = utenteDao.doRetrieveById(4);
    Partecipazione partecipazione1 = new Partecipazione(asta1, utente3, 100);
    Partecipazione partecipazione2 = new Partecipazione(asta1, utente3, 300);
    Partecipazione partecipazione3 = new Partecipazione(asta1, utente3, 500);
    Partecipazione partecipazione4 = new Partecipazione(asta2, utente3, 300);
    Partecipazione partecipazione5 = new Partecipazione(asta2, utente3, 500);
    Partecipazione partecipazione6 = new Partecipazione(asta3,utente3,2000);
    partecipazioneDao.doCreate(partecipazione1);
    partecipazioneDao.doCreate(partecipazione2);
    partecipazioneDao.doCreate(partecipazione3);
    partecipazioneDao.doCreate(partecipazione4);
    partecipazioneDao.doCreate(partecipazione5);
    partecipazioneDao.doCreate(partecipazione6);

    List<Asta> asteVinte = astaService.getWonAuctions(utente3);
    Assertions.assertEquals(2,asteVinte.size());
    Assertions.assertEquals(asta1.getId(),asteVinte.get(0).getId());
    Assertions.assertEquals(asta2.getId(),asteVinte.get(1).getId());
  }

  @Test
  void getLostAuctions() {
    Asta asta1 = astaDao.doRetrieveById(1);
    Asta asta2 = astaDao.doRetrieveById(2);
    Asta asta3 = astaDao.doRetrieveById(3);
    Asta asta4 = astaDao.doRetrieveById(4);
    asta1.setStato(Asta.Stato.TERMINATA);
    asta2.setStato(Asta.Stato.TERMINATA);
    asta4.setStato(Asta.Stato.TERMINATA);
    astaDao.doUpdate(asta1);
    astaDao.doUpdate(asta2);
    astaDao.doUpdate(asta4);
    Utente utente3 = utenteDao.doRetrieveById(4);
    Utente utente4 = utenteDao.doRetrieveById(5);
    Partecipazione partecipazione1 = new Partecipazione(asta1, utente3, 100);
    Partecipazione partecipazione2 = new Partecipazione(asta1, utente3, 300);
    Partecipazione partecipazione3 = new Partecipazione(asta1, utente3, 500);
    Partecipazione partecipazione4 = new Partecipazione(asta2, utente3, 300);
    Partecipazione partecipazione5 = new Partecipazione(asta2, utente3, 500);
    Partecipazione partecipazione6 = new Partecipazione(asta3,utente3,2000);
    Partecipazione partecipazione7 = new Partecipazione(asta1,utente4,200);
    Partecipazione partecipazione8 = new Partecipazione(asta4,utente4, 300);
    Partecipazione partecipazione9 = new Partecipazione(asta4,utente3,200);
    Partecipazione partecipazione10 = new Partecipazione(asta2,utente4,100);
    partecipazioneDao.doCreate(partecipazione1);
    partecipazioneDao.doCreate(partecipazione2);
    partecipazioneDao.doCreate(partecipazione3);
    partecipazioneDao.doCreate(partecipazione4);
    partecipazioneDao.doCreate(partecipazione5);
    partecipazioneDao.doCreate(partecipazione6);
    partecipazioneDao.doCreate(partecipazione7);
    partecipazioneDao.doCreate(partecipazione8);
    partecipazioneDao.doCreate(partecipazione9);
    partecipazioneDao.doCreate(partecipazione10);



    List<Asta> astePerse1 = astaService.getLostAuctions(utente3);
    List<Asta> astePerse2 = astaService.getLostAuctions(utente4);
    Assertions.assertEquals(1,astePerse1.size());
    Assertions.assertEquals(asta4.getId(),astePerse1.get(0).getId());
    Assertions.assertEquals(2,astePerse2.size());
    Assertions.assertEquals(asta1.getId(),astePerse2.get(0).getId());
    Assertions.assertEquals(asta2.getId(),astePerse2.get(1).getId());
  }

  @Test
  void getCurrentAuctions() {
    Asta asta1 = astaDao.doRetrieveById(1);
    Asta asta2 = astaDao.doRetrieveById(2);
    Asta asta3 = astaDao.doRetrieveById(3);
    Asta asta4 = astaDao.doRetrieveById(4);
    asta1.setStato(Asta.Stato.TERMINATA);
    asta2.setStato(Asta.Stato.TERMINATA);
    asta4.setStato(Asta.Stato.TERMINATA);
    astaDao.doUpdate(asta1);
    astaDao.doUpdate(asta2);
    astaDao.doUpdate(asta4);
    Utente utente3 = utenteDao.doRetrieveById(4);
    Utente utente4 = utenteDao.doRetrieveById(5);
    Partecipazione partecipazione1 = new Partecipazione(asta1, utente3, 100);
    Partecipazione partecipazione2 = new Partecipazione(asta1, utente3, 300);
    Partecipazione partecipazione3 = new Partecipazione(asta1, utente3, 500);
    Partecipazione partecipazione4 = new Partecipazione(asta2, utente3, 300);
    Partecipazione partecipazione5 = new Partecipazione(asta2, utente3, 500);
    Partecipazione partecipazione6 = new Partecipazione(asta3,utente3,2000);
    Partecipazione partecipazione7 = new Partecipazione(asta1,utente4,200);
    Partecipazione partecipazione8 = new Partecipazione(asta4,utente4, 300);
    Partecipazione partecipazione9 = new Partecipazione(asta4,utente3,200);
    Partecipazione partecipazione10 = new Partecipazione(asta2,utente4,100);
    partecipazioneDao.doCreate(partecipazione1);
    partecipazioneDao.doCreate(partecipazione2);
    partecipazioneDao.doCreate(partecipazione3);
    partecipazioneDao.doCreate(partecipazione4);
    partecipazioneDao.doCreate(partecipazione5);
    partecipazioneDao.doCreate(partecipazione6);
    partecipazioneDao.doCreate(partecipazione7);
    partecipazioneDao.doCreate(partecipazione8);
    partecipazioneDao.doCreate(partecipazione9);
    partecipazioneDao.doCreate(partecipazione10);



    List<Asta> asteCorrenti1 = astaService.getCurrentAuctions(utente3);
    List<Asta> asteCorrenti2 = astaService.getCurrentAuctions(utente4);
    Assertions.assertEquals(1,asteCorrenti1.size());
    Assertions.assertEquals(asta3.getId(),asteCorrenti1.get(0).getId());
    Assertions.assertEquals(0,asteCorrenti2.size());
  }

  @Test
  void getAllOffers() {
    Asta asta1 = astaDao.doRetrieveById(1);
    Asta asta2 = astaDao.doRetrieveById(2);
    Asta asta3 = astaDao.doRetrieveById(3);
    Asta asta4 = astaDao.doRetrieveById(4);
    asta1.setStato(Asta.Stato.TERMINATA);
    asta2.setStato(Asta.Stato.TERMINATA);
    asta4.setStato(Asta.Stato.TERMINATA);
    astaDao.doUpdate(asta1);
    astaDao.doUpdate(asta2);
    astaDao.doUpdate(asta4);
    Utente utente3 = utenteDao.doRetrieveById(4);
    Utente utente4 = utenteDao.doRetrieveById(5);
    Partecipazione partecipazione1 = new Partecipazione(asta1, utente3, 100);
    Partecipazione partecipazione2 = new Partecipazione(asta1, utente3, 300);
    Partecipazione partecipazione3 = new Partecipazione(asta1, utente3, 500);
    Partecipazione partecipazione4 = new Partecipazione(asta2, utente3, 300);
    Partecipazione partecipazione5 = new Partecipazione(asta2, utente3, 500);
    Partecipazione partecipazione6 = new Partecipazione(asta3,utente3,2000);
    Partecipazione partecipazione7 = new Partecipazione(asta1,utente4,200);
    Partecipazione partecipazione8 = new Partecipazione(asta4,utente4, 300);
    Partecipazione partecipazione9 = new Partecipazione(asta4,utente3,200);
    Partecipazione partecipazione10 = new Partecipazione(asta2,utente4,100);
    partecipazioneDao.doCreate(partecipazione1);
    partecipazioneDao.doCreate(partecipazione2);
    partecipazioneDao.doCreate(partecipazione3);
    partecipazioneDao.doCreate(partecipazione4);
    partecipazioneDao.doCreate(partecipazione5);
    partecipazioneDao.doCreate(partecipazione6);
    partecipazioneDao.doCreate(partecipazione7);
    partecipazioneDao.doCreate(partecipazione8);
    partecipazioneDao.doCreate(partecipazione9);
    partecipazioneDao.doCreate(partecipazione10);

    Assertions.assertEquals(10,astaService.getAllOffers().size());
  }


  @Nested
  class AvviaTerminaTest {

    @Test
    public void failAvviaTestAstaNonCreata() {

      Asta asta = astaDao.doRetrieveById(2);
      asta.setStato(Asta.Stato.ELIMINATA);
      astaDao.doUpdate(asta);
      TimedObject timedObject = new TimedObject(asta.getId(), "avviaAsta", asta.getDataInizio());
      AstaServiceImpl astaServiceImpl = (AstaServiceImpl) astaService;
      astaServiceImpl.executeTimedTask(timedObject);
      asta = astaDao.doRetrieveById(2);
      Assertions.assertEquals(asta.getStato(), Asta.Stato.ELIMINATA);

    }

    @Test
    public void successAvviaAsta() {
      Asta asta = astaDao.doRetrieveById(2);
      TimedObject timedObject = new TimedObject(asta.getId(), "avviaAsta", asta.getDataInizio());
      AstaServiceImpl astaServiceImpl = (AstaServiceImpl) astaService;
      astaServiceImpl.executeTimedTask(timedObject);
      asta = astaDao.doRetrieveById(2);
      Assertions.assertEquals(asta.getStato(), Asta.Stato.IN_CORSO);
    }

    @Test
    public void terminaAstaNessunOfferente() {
      Asta asta = astaDao.doRetrieveById(2);
      TimedObject timedObject = new TimedObject(asta.getId(), "terminaAsta", asta.getDataInizio());
      AstaServiceImpl astaServiceImpl = (AstaServiceImpl) astaService;
      astaServiceImpl.executeTimedTask(timedObject);
      asta = astaDao.doRetrieveById(2);
      Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
      Assertions.assertEquals(asta.getStato(), Asta.Stato.TERMINATA);
      Assertions.assertEquals(opera.getStato(), Opera.Stato.PREVENDITA);
    }

    @Test
    public void terminaAstaConOfferta() {
      Asta asta = astaDao.doRetrieveById(2);
      TimedObject timedObject = new TimedObject(asta.getId(), "terminaAsta", asta.getDataInizio());
      AstaServiceImpl astaServiceImpl = (AstaServiceImpl) astaService;

      Utente utente2 = utenteDao.doRetrieveById(3);
      Utente utente3 = utenteDao.doRetrieveById(4);

      Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());

      Utente artista = utenteDao.doRetrieveById(opera.getArtista().getId());
      Partecipazione partecipazione1 = new Partecipazione(asta, utente2, 100);
      Partecipazione partecipazione2 = new Partecipazione(asta, utente3, 300);
      Partecipazione partecipazione3 = new Partecipazione(asta, utente3, 500);
      partecipazioneDao.doCreate(partecipazione1);
      partecipazioneDao.doCreate(partecipazione2);
      partecipazioneDao.doCreate(partecipazione3);

      int numeroNotificheVincitore = notificaDao.doRetrieveAllByUserId(utente3.getId()).size();
      double saldoArtista = artista.getSaldo();
      astaServiceImpl.executeTimedTask(timedObject);

      asta = astaDao.doRetrieveById(2);
      opera = operaDao.doRetrieveById(asta.getOpera().getId());
      utente3 = utenteDao.doRetrieveById(4);
      artista = utenteDao.doRetrieveById(opera.getArtista().getId());

      Assertions.assertEquals(asta.getStato(), Asta.Stato.TERMINATA);
      Assertions.assertEquals(opera.getStato(), Opera.Stato.IN_POSSESSO);
      Assertions.assertEquals(opera.getPossessore().getId(), utente3.getId());
      Assertions.assertEquals(numeroNotificheVincitore + 1,
          notificaDao.doRetrieveAllByUserId(4).size());
      Assertions.assertEquals(saldoArtista + 500, artista.getSaldo());

    }

  }

  private Date creaData(int giorni) {
    Date now = new Date();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(now);
    calendar.add(Calendar.DAY_OF_MONTH, giorni);
    return calendar.getTime();
  }

}