package unit.gestione.service;

import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaServiceImpl;
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

  private RivenditaService service;

  @BeforeEach
  void setUp() {
    service = new RivenditaServiceImpl(utenteDao, operaDao, rivenditaDao, notificaDao);
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
    assertEquals(0, service.getResellPrice(opera));
  }

  @ParameterizedTest
  @DisplayName("Make New Resell Test")
  @ArgumentsSource(OperaProvider.class)
  void resell(Opera opera) {

    when(operaDao.doRetrieveById(anyInt())).thenReturn(opera);
    assertEquals(true, service.resell(opera.getId()));
  }


  @ParameterizedTest
  @DisplayName("Buy Test")
  @ArgumentsSource(RivenditaProvider.class)
  void buy(Rivendita rivendita) {

    Utente utente = new Utente();
    utente.setSaldo(0d);
    utente.setId(3);

    when(rivenditaDao.doRetrieveById(anyInt())).thenReturn(rivendita);
    when(utenteDao.doRetrieveById(anyInt())).thenReturn(utente);
    when(operaDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera());
    when(utenteDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera().getPossessore());
    when(utenteDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera().getArtista());
    doNothing().when(utenteDao).doUpdate(any());
    doNothing().when(operaDao).doUpdate(any());
    doNothing().when(rivenditaDao).doUpdate(any());

    assertEquals(true, service.buy(rivendita.getId(), utente.getId()));
  }

  @ParameterizedTest
  @DisplayName("Get Resells Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResells(List<Rivendita> rivendite) {

      when(rivenditaDao.doRetrieveAll("")).thenReturn(rivendite);

      for(Rivendita r : rivendite) {

        when(operaDao.doRetrieveById(anyInt())).thenReturn(r.getOpera());
        when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getPossessore());
        when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getArtista());
      }

      assertEquals(rivendite, service.getResells());
  }

  @ParameterizedTest
  @DisplayName("Get Resells Test By State")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsByState(List<Rivendita> rivendite) {

    when(rivenditaDao.doRetrieveByStato(any())).thenReturn(rivendite);

    for(Rivendita r : rivendite) {

      when(operaDao.doRetrieveById(anyInt())).thenReturn(r.getOpera());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getPossessore());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getArtista());
    }
    assertEquals(rivendite, service.getResellsByState(Rivendita.Stato.IN_CORSO));

  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Price Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByPrice(List<Rivendita> rivendite) {

    String order = "";
    when(rivenditaDao.doRetrieveByStato(any())).thenReturn(rivendite);

    for(Rivendita r : rivendite) {

      when(operaDao.doRetrieveById(anyInt())).thenReturn(r.getOpera());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getPossessore());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getArtista());
    }

    assertEquals(rivendite, service.getResellsSortedByPrice(order, Rivendita.Stato.IN_CORSO));
  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Price Desc Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByPriceDesc(List<Rivendita> rivendite) {

    String order = "DESC";

    when(rivenditaDao.doRetrieveByStato(any())).thenReturn(rivendite);

    for(Rivendita r : rivendite) {

      when(operaDao.doRetrieveById(anyInt())).thenReturn(r.getOpera());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getPossessore());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getArtista());
    }

    assertEquals(rivendite, service.getResellsSortedByPrice(order, Rivendita.Stato.IN_CORSO));
  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Most Popular Artists Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByArtistFollowers(List<Rivendita> rivendite) {

    String order = "";

    when(service.getResellsByState(any())).thenReturn(rivendite);

    for(Rivendita r : rivendite) {

      when(operaDao.doRetrieveById(anyInt())).thenReturn(r.getOpera());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getPossessore());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getArtista());
    }

    assertEquals(rivendite, service.getResellsSortedByArtistFollowers(order, Rivendita.Stato.IN_CORSO));

  }

  @ParameterizedTest
  @DisplayName("Get Resells Sorted By Least Popular Artists Test")
  @ArgumentsSource(ListRivenditeProvider.class)
  void getResellsSortedByArtistFollowersDesc(List<Rivendita> rivendite) {

    String order = "DESC";

    when(service.getResellsByState(any())).thenReturn(rivendite);

    for(Rivendita r : rivendite) {

      when(operaDao.doRetrieveById(anyInt())).thenReturn(r.getOpera());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getPossessore());
      when(utenteDao.doRetrieveById(anyInt())).thenReturn(r.getOpera().getArtista());
    }

    assertEquals(rivendite, service.getResellsSortedByArtistFollowers(order, Rivendita.Stato.IN_CORSO));

  }

  @ParameterizedTest
  @DisplayName("Get Single Resell Test")
  @ArgumentsSource(RivenditaProvider.class)
  void getResell(Rivendita rivendita) {

    when(rivenditaDao.doRetrieveById(anyInt())).thenReturn(rivendita);
    when(operaDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera());
    when(utenteDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera().getPossessore());
    when(utenteDao.doRetrieveById(anyInt())).thenReturn(rivendita.getOpera().getArtista());

    assertEquals(rivendita, service.getResell(rivendita.getId()));
  }
}
