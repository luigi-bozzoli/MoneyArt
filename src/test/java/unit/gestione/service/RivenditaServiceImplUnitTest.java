package unit.gestione.service;

import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import javax.sql.rowset.serial.SerialBlob;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RivenditaServiceImplUnitTest {

  @Mock
  private RivenditaDao rivenditaDao;
  @Mock
  private OperaDao operaDao;
  @Mock
  private NotificaDao notificaDao;
  @Mock
  private UtenteDao utenteDao;
  @Mock
  private RivenditaService service;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  static class OperaProvider implements ArgumentsProvider{

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

      String s1 = "image blob 1";
      Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
      Utente u1 = new Utente();
      u1.setId(1);
      Opera o1 = new Opera("The Shibosis", "Descrizione", Opera.Stato.IN_POSSESSO,
        b1, u1, u1, "xxxx");
      o1.setId(1);

      String s2 = "image blob 2";
      Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
      Utente u2 = new Utente();
      u2.setId(2);
      Opera o2 = new Opera("U2", "Descrizione", Opera.Stato.IN_POSSESSO,
        b2, u2, u2, "yyyy");
      o2.setId(2);

      String s3 = "image blob 3";
      Blob b3 = new SerialBlob(s3.getBytes(StandardCharsets.UTF_8));
      Utente u3 = new Utente();
      u3.setId(3);
      Opera o3 = new Opera("PIXELART", "Descrizione", Opera.Stato.IN_POSSESSO,
        b3, u3, u3, "zzzz");
      o3.setId(3);

      return Stream.of(
        Arguments.of(o1),
        Arguments.of(o2),
        Arguments.of(o3)
      );

    }
  }

  static class RivenditaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

      String s = "image blob 1";
      Blob b = new SerialBlob(s.getBytes(StandardCharsets.UTF_8));

      Utente owner = new Utente();
      owner.setId(1);
      owner.setSaldo(0d);

      Utente artist = new Utente();
      artist.setId(2);
      artist.setSaldo(0d);

      Opera artwork = new Opera(
        "The Shibosis", "Descrizione", Opera.Stato.IN_VENDITA,
        b, owner, artist, "xxxxx"
      );
      artwork.setId(1);

      Rivendita resell = new Rivendita(
        artwork,
        Rivendita.Stato.IN_CORSO,
        0d
      );

      resell.setId(1);

      return Stream.of(Arguments.of(resell));
    }
  }

  static class ListRivenditeProvider implements ArgumentsProvider{

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      List<Rivendita> rivendite = getListRivendite();

      return Stream.of(Arguments.of(rivendite));
    }
  }

  static List<Rivendita> getListRivendite() throws SQLException {
    String s1 = "image blob 1";
    Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
    Utente u1 = new Utente();
    u1.setId(1);
    Opera o1 = new Opera("The Shibosis", "Descrizione", Opera.Stato.IN_POSSESSO,
      b1, u1, u1, "xxxx");
    o1.setId(1);

    String s2 = "image blob 2";
    Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
    Utente u2 = new Utente();
    u2.setId(2);
    Opera o2 = new Opera("U2", "Descrizione", Opera.Stato.IN_VENDITA,
      b2, u2, u1, "yyyy");
    o2.setId(2);

    String s3 = "image blob 3";
    Blob b3 = new SerialBlob(s3.getBytes(StandardCharsets.UTF_8));
    Utente u3 = new Utente();
    u3.setId(3);
    Opera o3 = new Opera("PIXELART", "Descrizione", Opera.Stato.IN_VENDITA,
      b3, u3, u1, "zzzz");
    o3.setId(3);

    Rivendita r1 = new Rivendita(o1, Rivendita.Stato.IN_CORSO, 0d);
    r1.setId(1);
    Rivendita r2 = new Rivendita(o2, Rivendita.Stato.IN_CORSO, 0d);
    r2.setId(2);
    Rivendita r3 = new Rivendita(o3, Rivendita.Stato.IN_CORSO, 0d);
    r3.setId(3);

    List<Rivendita> rivendite = new ArrayList<>();
    rivendite.add(r1);
    rivendite.add(r2);
    rivendite.add(r3);

    return rivendite;
  }


  @ParameterizedTest
  @DisplayName("Get Resell Price Test")
  @ArgumentsSource(OperaProvider.class)
  void getResellPrice(Opera opera) {

    when(operaDao.doRetrieveById(anyInt())).thenReturn(opera);
    Opera artwork = operaDao.doRetrieveById(opera.getId());
    assertNotNull(artwork);

    double price = 0;

    when(utenteDao.doRetrieveById(anyInt())).thenReturn(artwork.getArtista());
    Utente artist = utenteDao.doRetrieveById(artwork.getArtista().getId());
    assertNotNull(artist);

    int n_followers = artist.getnFollowers();
    price = 0.25 * n_followers;

    assertEquals(0, price);
  }

  @ParameterizedTest
  @DisplayName("Make New Resell Test")
  @ArgumentsSource(OperaProvider.class)
  void resell(Opera opera) {

    when(operaDao.doRetrieveById(anyInt())).thenReturn(opera);

    Opera artwork = operaDao.doRetrieveById(opera.getId());
    assertNotNull(artwork);

    assertEquals(Opera.Stato.IN_POSSESSO, artwork.getStato());

    Rivendita resell = new Rivendita();
    resell.setOpera(artwork);
    resell.setStato(Rivendita.Stato.IN_CORSO);
    resell.setPrezzo(0d);

    when(rivenditaDao.doCreate(any())).thenReturn(true);
    boolean result = rivenditaDao.doCreate(resell);
    assertEquals(true, result);

    doNothing().when(operaDao).doUpdate(any());
    artwork.setStato(Opera.Stato.IN_VENDITA);
    operaDao.doUpdate(artwork);
    assertEquals(Opera.Stato.IN_VENDITA, artwork.getStato());
  }


  @ParameterizedTest
  @DisplayName("Buy Test")
  @ArgumentsSource(RivenditaProvider.class)
  void buy(Rivendita rivendita) {

    Utente utente = new Utente();
    utente.setSaldo(0d);
    utente.setId(3);

    when(rivenditaDao.doRetrieveById(anyInt())).thenReturn(rivendita);
    Rivendita retrieve = rivenditaDao.doRetrieveById(rivendita.getId());
    assertNotNull(retrieve);

    when(utenteDao.doRetrieveById(anyInt())).thenReturn(utente);
    Utente buyer = utenteDao.doRetrieveById(utente.getId());
    assertNotNull(buyer);

    assertEquals(Rivendita.Stato.IN_CORSO, rivendita.getStato());
    assertTrue(true, String.valueOf((buyer.getSaldo() > rivendita.getPrezzo())));

    when(operaDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera());
    Opera artwork = operaDao.doRetrieveById(rivendita.getOpera().getId());

    artwork.setPossessore(utente);
    artwork.setStato(Opera.Stato.IN_POSSESSO);

    when(utenteDao.doRetrieveById(anyInt())).thenReturn(artwork.getArtista());
    Utente owner = utenteDao.doRetrieveById(artwork.getArtista().getId());
    owner.setSaldo(0d);

    buyer.setSaldo(utente.getSaldo() - rivendita.getPrezzo());
    owner.setSaldo(owner.getSaldo() + rivendita.getPrezzo());

    rivendita.setStato(Rivendita.Stato.TERMINATA);
    Notifica notifica = new Notifica(owner, null, rivendita, Notifica.Tipo.TERMINATA, "", false);

    doNothing().when(utenteDao).doUpdate(any());
    utenteDao.doUpdate(buyer);
    utenteDao.doUpdate(owner);
    assertEquals(0, buyer.getSaldo());
    assertEquals(0,owner.getSaldo());

    doNothing().when(operaDao).doUpdate(any());
    operaDao.doUpdate(artwork);

    doNothing().when(rivenditaDao).doUpdate(any());
    rivenditaDao.doUpdate(rivendita);

    when(notificaDao.doCreate(any())).thenReturn(true);
    boolean result = notificaDao.doCreate(notifica);
    assertEquals(true, result);
  }

  @ParameterizedTest
  @DisplayName("Get Resells Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResells(List<Rivendita> rivendite) {

      List<Rivendita> result = new ArrayList<>();
      when(rivenditaDao.doRetrieveAll("")).thenReturn(rivendite);

      for(Rivendita r : rivendite) {

        when(operaDao.doRetrieveById(anyInt())).thenReturn(r.getOpera());
        Opera opera = operaDao.doRetrieveById(r.getOpera().getId());

        when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getPossessore());
        Utente owner = utenteDao.doRetrieveById(r.getOpera().getPossessore().getId());

        when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getArtista());
        Utente artist = utenteDao.doRetrieveById(r.getOpera().getArtista().getId());
        artist.setnFollowers(opera.getArtista().getnFollowers());

        opera.setPossessore(owner);
        opera.setArtista(artist);

        double prezzo = r.getPrezzo();
        Rivendita.Stato stato = r.getStato();

        Rivendita rAdd = new Rivendita(
          opera,
          stato,
          prezzo
        );
        rAdd.setId(r.getId());

        result.add(rAdd);
      }

      assertArrayEquals(rivendite.toArray(), result.toArray());
  }

  @ParameterizedTest
  @DisplayName("Get Resells By State Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsByState(List<Rivendita> rivendite) {

    List<Rivendita> result = new ArrayList<>();
    when(rivenditaDao.doRetrieveByStato(any())).thenReturn(rivendite);

    for(Rivendita r : rivendite) {

      when(operaDao.doRetrieveById(anyInt())).thenReturn(r.getOpera());
      Opera opera = operaDao.doRetrieveById(r.getOpera().getId());

      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getPossessore());
      Utente owner = utenteDao.doRetrieveById(r.getOpera().getPossessore().getId());

      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getArtista());
      Utente artist = utenteDao.doRetrieveById(r.getOpera().getArtista().getId());
      artist.setnFollowers(opera.getArtista().getnFollowers());

      opera.setPossessore(owner);
      opera.setArtista(artist);

      double prezzo = r.getPrezzo();
      Rivendita.Stato stato = r.getStato();

      Rivendita rAdd = new Rivendita(
        opera,
        stato,
        prezzo
      );
      rAdd.setId(r.getId());

      result.add(rAdd);
    }

    assertArrayEquals(rivendite.toArray(), result.toArray());

  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Price Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByPrice(List<Rivendita> rivendite) {

    String order = "";

    when(service.getResellsByState(any())).thenReturn(rivendite);
    List<Rivendita> result = service.getResellsByState(Rivendita.Stato.IN_CORSO);

    Collections.sort(result, new Comparator<Rivendita>() {
      @Override
      public int compare(Rivendita r1, Rivendita r2) {
        Double price1 = r1.getPrezzo();
        Double price2 = r2.getPrezzo();
        return Double.compare(price1, price2);
      }
    });

    assertNotEquals("DESC", order);

    assertEquals(rivendite, result);
  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Price Desc Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByPriceDesc(List<Rivendita> rivendite) {

    String order = "DESC";

    when(service.getResellsByState(any())).thenReturn(rivendite);
    List<Rivendita> result = service.getResellsByState(Rivendita.Stato.IN_CORSO);

    Collections.sort(result, new Comparator<Rivendita>() {
      @Override
      public int compare(Rivendita r1, Rivendita r2) {
        Double price1 = r1.getPrezzo();
        Double price2 = r2.getPrezzo();
        return Double.compare(price1, price2);
      }
    });

    assertEquals("DESC", order);
    Collections.reverse(result);
    Collections.reverse(rivendite);

    assertEquals(rivendite, result);
  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Most Popular Artists Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByArtistFollowers(List<Rivendita> rivendite) {

    String order = "";

    when(service.getResellsByState(any())).thenReturn(rivendite);
    List<Rivendita> result = service.getResellsByState(Rivendita.Stato.IN_CORSO);

    Collections.sort(result, new Comparator<Rivendita>() {
      @Override
      public int compare(Rivendita r1, Rivendita r2) {
        int f1 = r1.getOpera().getArtista().getnFollowers();
        int f2 = r2.getOpera().getArtista().getnFollowers();
        return Integer.compare(f1, f2);
      }
    });

    assertNotEquals("DESC", order);

    assertEquals(rivendite, result);

  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Least Popular Artists Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByArtistFollowersDesc(List<Rivendita> rivendite) {

    String order = "DESC";

    when(service.getResellsByState(any())).thenReturn(rivendite);
    List<Rivendita> result = service.getResellsByState(Rivendita.Stato.IN_CORSO);

    Collections.sort(result, new Comparator<Rivendita>() {
      @Override
      public int compare(Rivendita r1, Rivendita r2) {
        int f1 = r1.getOpera().getArtista().getnFollowers();
        int f2 = r2.getOpera().getArtista().getnFollowers();
        return Integer.compare(f1, f2);
      }
    });

    assertEquals("DESC", order);
    Collections.reverse(result);
    Collections.reverse(rivendite);

    assertEquals(rivendite, result);

  }

  @ParameterizedTest
  @DisplayName("Get Single Resell Test")
  @ArgumentsSource(RivenditaProvider.class)
  void getResell(Rivendita rivendita) {

    Integer id = rivendita.getId();
    when(rivenditaDao.doRetrieveById(anyInt())).thenReturn(rivendita);
    Rivendita retrieve = rivenditaDao.doRetrieveById(id);

    when(operaDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera());
    Opera artwork = operaDao.doRetrieveById(retrieve.getOpera().getId());

    when(utenteDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera().getPossessore());
    Utente owner = utenteDao.doRetrieveById(retrieve.getOpera().getPossessore().getId());

    when(utenteDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera().getArtista());
    Utente artist = utenteDao.doRetrieveById(retrieve.getOpera().getArtista().getId());

    artwork.setArtista(artist);
    artwork.setPossessore(owner);
    retrieve.setOpera(artwork);

    assertEquals(rivendita, retrieve);
  }
}
