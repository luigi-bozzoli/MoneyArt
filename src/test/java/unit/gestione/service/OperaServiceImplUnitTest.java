package unit.gestione.service;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.opere.service.OperaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.blockchain.MoneyArtNft;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.sql.rowset.serial.SerialBlob;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OperaServiceImplUnitTest {

  @Mock
  private OperaDao operaDao;
  @Mock
  private MoneyArtNft moneyArtNft;

  private OperaService operaService;

  @BeforeEach
  void setUp() {
    operaService = new OperaServiceImpl(operaDao,moneyArtNft);
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
      Opera o1 = new Opera("The Shibosis", "Descrizione", Opera.Stato.PREVENDITA,
              b1, u1, u1, "xxxxx");
      o1.setId(1);

      String s2 = "image blob 2";
      Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
      Utente u2 = new Utente();
      u2.setId(2);
      Opera o2 = new Opera("The Shibosis 2", "Descrizione", Opera.Stato.PREVENDITA,
              b2, u2, u2, "yyyyyyyyy");
      o2.setId(2);

      String s3 = "image blob 3";
      Blob b3 = new SerialBlob(s3.getBytes(StandardCharsets.UTF_8));
      Utente u3 = new Utente();
      u3.setId(3);
      Opera o3 = new Opera("PIXELARTARTARTA", "Descrizione", Opera.Stato.PREVENDITA,
              b3, u3, u3, "zzzzzz");
      o3.setId(3);

      return Stream.of(
              Arguments.of(o1),
              Arguments.of(o2),
              Arguments.of(o3)
      );

    }
  }

  static class ListOpereProvider implements ArgumentsProvider{

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      List<Opera> opere = getListOpere();

      return Stream.of(Arguments.of(opere));
    }
  }

  static List<Opera> getListOpere() throws SQLException {
    String s1 = "image blob 1";
    Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
    Utente u1 = new Utente();
    u1.setId(1);
    Opera o1 = new Opera("The Shibosis", "Descrizione", Opera.Stato.PREVENDITA,
            b1, u1, u1, "xxxxx");
    o1.setId(1);

    String s2 = "image blob 2";
    Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
    Utente u2 = new Utente();
    u2.setId(2);
    Opera o2 = new Opera("The Shibosis 2", "Descrizione", Opera.Stato.PREVENDITA,
            b2, u2, u1, "yyyyyyyyy");
    o2.setId(2);

    String s3 = "image blob 3";
    Blob b3 = new SerialBlob(s3.getBytes(StandardCharsets.UTF_8));
    Utente u3 = new Utente();
    u3.setId(3);
    Opera o3 = new Opera("PIXELARTARTARTA", "Descrizione", Opera.Stato.PREVENDITA,
            b3, u3, u1, "zzzzzz");
    o3.setId(3);

    List<Opera> opere = new ArrayList<>();
    opere.add(o1);
    opere.add(o2);
    opere.add(o3);

    return opere;
  }

  @Nested
  @DisplayName("Test Suite addArtwork")
  class TestAddArtwork{
    @DisplayName("Add Artwork")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void addArtwork(Opera opera) throws Exception {
      RemoteFunctionCall<TransactionReceipt> transaction = Mockito.mock(RemoteFunctionCall.class);
      when(moneyArtNft.create(anyString())).thenReturn(transaction);
      when(transaction.send()).thenReturn(null);
      when(operaDao.doCreate(opera)).thenReturn(true);

      Assertions.assertTrue(operaService.addArtwork(opera));
    }

    @DisplayName("Add Artwork Catch")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void addArtworkCatch(Opera opera) throws Exception {
      RemoteFunctionCall<TransactionReceipt> transaction = Mockito.mock(RemoteFunctionCall.class);
      when(moneyArtNft.create(anyString())).thenReturn(transaction);
      when(transaction.send()).thenThrow(new Exception());

      Assertions.assertThrows(Exception.class,() -> operaService.addArtwork(opera));
    }

    @DisplayName("Add Artwork Name Null")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void addArtworkNomeNull(Opera opera) throws Exception {
      opera.setNome(null);
      Assertions.assertFalse(operaService.addArtwork(opera));
    }

    @DisplayName("Add Artwork Image Null")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void addArtworkImgNull(Opera opera) throws Exception {
      opera.setImmagine(null);
      Assertions.assertFalse(operaService.addArtwork(opera));
    }

    @DisplayName("Add Artwork False whit checkArtwork")
    @ParameterizedTest
    @ArgumentsSource(ListOpereProvider.class)
    void addArtworkFalseCheckArtwork(List<Opera> opere) throws Exception {
      Opera opera = opere.get(0);

      when(operaDao.doRetrieveAllByArtistId(opera.getArtista().getId())).thenReturn(opere);

      Assertions.assertFalse(operaService.addArtwork(opera));
    }

  }

  @Nested
  @DisplayName("Test Suite checkArtwork")
  class TestCheckArtwork{

    @DisplayName("Check Artwork")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void checkArtwork(Opera opera) throws SQLException {
      List<Opera> opere = getListOpere();

      Utente u1 = new Utente();
      u1.setId(1);

      for(Opera o : opere){
        o.setArtista(u1);
        o.setStato(Opera.Stato.IN_POSSESSO);
      }
      when(operaDao.doRetrieveAllByArtistId(u1.getId())).thenReturn(opere);

      Assertions.assertTrue(operaService.checkArtwork(u1.getId(), opera.getNome()));
    }

    @DisplayName("Check Artwork False")
    @ParameterizedTest
    @ArgumentsSource(OperaProvider.class)
    void checkArtworkFalse(Opera opera) throws SQLException {
      List<Opera> opere = new ArrayList<>();

      String s = "image blob";
      Blob b = new SerialBlob(s.getBytes(StandardCharsets.UTF_8));;
      Opera o = new Opera("OPERA UNICAAA", "Descrizione", Opera.Stato.PREVENDITA,
              b, opera.getPossessore(), opera.getArtista(), "xxxxx");
      o.setId(1);

      opere.add(o);

      when(operaDao.doRetrieveAllByArtistId(o.getArtista().getId())).thenReturn(opere);

      Assertions.assertFalse(operaService.checkArtwork(o.getArtista().getId(), opera.getNome()));
    }

    @Test
    @DisplayName("Check Artwork with name null")
    void checkArtworkNameNull() throws SQLException {
      List<Opera> opere = getListOpere();

      Utente u1 = new Utente();
      u1.setId(1);

      for(Opera o : opere){
        o.setArtista(u1);
        o.setStato(Opera.Stato.IN_POSSESSO);
      }
      when(operaDao.doRetrieveAllByArtistId(u1.getId())).thenReturn(opere);

      Assertions.assertThrows(Exception.class, () -> operaService.checkArtwork(1,null));
    }

  }

  @DisplayName("Test Get Artwork")
  @ParameterizedTest
  @ArgumentsSource(OperaProvider.class)
  void getArtwork(Opera o) {
    when(operaDao.doRetrieveById(o.getId())).thenReturn(o);

    assertEquals(o, operaService.getArtwork(o.getId()));
  }

  @DisplayName("Test Search Opera")
  @ParameterizedTest
  @ArgumentsSource(OperaProvider.class)
  void searchOpera(Opera opera) {
    List<Opera> opere = new ArrayList<>();
    opere.add(opera);

    when(operaDao.doRetrieveAllByName(opera.getNome())).thenReturn(opere);

    Assertions.assertEquals(opere, operaService.searchOpera(opera.getNome()));
  }

  @DisplayName("Test Get Artwark By artist")
  @ParameterizedTest
  @ArgumentsSource(ListOpereProvider.class)
  void getArtworkByUser(List<Opera> opere) {
    when(operaDao.doRetrieveAllByArtistId(anyInt())).thenReturn(opere);

    Assertions.assertEquals(opere, operaService.getArtworkByUser(1));
  }

  @DisplayName("Test Get Artwark By artist")
  @ParameterizedTest
  @ArgumentsSource(ListOpereProvider.class)
  void getArtworkByOwner(List<Opera> opere) {
    when(operaDao.doRetrieveAllByOwnerId(anyInt())).thenReturn(opere);

    Assertions.assertEquals(opere, operaService.getArtworkByOwner(1));
  }


}