package unit.gestione.service;

import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneServiceImpl;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
import org.codehaus.plexus.util.cli.Arg;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.web3j.abi.datatypes.Bool;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SegnalazioneServiceImplUnitTest {

  @Mock
  private SegnalazioneDao segnalazioneDao;

  private SegnalazioneService segnalazioneService;

  @BeforeEach
  void setUp() {
    segnalazioneService = new SegnalazioneServiceImpl(segnalazioneDao);
  }

  @AfterEach
  void tearDown() {
  }

  static class SegnalazioneProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      Asta asta = new Asta();
      asta.setId(1);
      String commento = "MarioPeluso ha copiato l'opera di alfcan attualmente all'asta.";
      Segnalazione s = new Segnalazione(asta, commento, false);
      s.setId(1);

      Asta asta2 = new Asta();
      asta2.setId(2);
      String commento2 = "Asta illecita";
      Segnalazione s2 = new Segnalazione(asta2, commento2, false);
      s2.setId(2);

      Asta asta3 = new Asta();
      asta3.setId(3);
      String commento3 = "Altra Asta illecita";
      Segnalazione s3 = new Segnalazione(asta3, commento3, false);
      s3.setId(3);

      return Stream.of(
              Arguments.of(s),
              Arguments.of(s2),
              Arguments.of(s3)
      );
    }
  }

  static class ListSegnalazioneProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
      Asta asta = new Asta();
      asta.setId(1);
      String commento = "MarioPeluso ha copiato l'opera di alfcan attualmente all'asta.";
      Segnalazione s = new Segnalazione(asta, commento, false);
      s.setId(1);

      Asta asta2 = new Asta();
      asta2.setId(2);
      String commento2 = "Asta illecita";
      Segnalazione s2 = new Segnalazione(asta2, commento2, false);
      s2.setId(2);

      Asta asta3 = new Asta();
      asta3.setId(3);
      String commento3 = "Altra Asta illecita";
      Segnalazione s3 = new Segnalazione(asta3, commento3, false);
      s3.setId(3);

      ArrayList<Segnalazione> segnalazioni = new ArrayList<Segnalazione>();
      segnalazioni.add(s);
      segnalazioni.add(s2);
      segnalazioni.add(s3);

      return Stream.of(
              Arguments.of(segnalazioni)
      );
    }
  }

  @DisplayName("Test Get Report")
  @ParameterizedTest
  @ArgumentsSource(ListSegnalazioneProvider.class)
  void getReports(List<Segnalazione> segnalazioni) {
    when(segnalazioneDao.doRetrieveAll(null)).thenReturn(segnalazioni);
    List<Segnalazione> segnalazioni1 = segnalazioneService.getReports(null);
    assertArrayEquals(segnalazioni1.toArray(), segnalazioni.toArray());
  }

  @DisplayName("Test Get Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void getReport(Segnalazione s) {
    when(segnalazioneDao.doRetrieveById(s.getId())).thenReturn(s);

    assertEquals(s, segnalazioneService.getReport(s.getId()));
  }

  @DisplayName("Test Add Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void addReport(Segnalazione s) {
    when(segnalazioneDao.doCreate(s)).thenReturn(Boolean.TRUE);
    Boolean bool = segnalazioneService.addReport(s);

    assertTrue(bool);
  }

  @DisplayName("Test Delete Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void removeReport(Segnalazione s) {
    when(segnalazioneDao.doRetrieveById(s.getId())).thenReturn(s);
    doNothing().when(segnalazioneDao).doDelete(s);
    segnalazioneService.removeReport(s);

    verify(segnalazioneDao, times(1)).doDelete(s);
  }

  @DisplayName("Test Read Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void readReport(Segnalazione s) {
    when(segnalazioneDao.doRetrieveById(s.getId())).thenReturn(s);
    doNothing().when(segnalazioneDao).doUpdate(s);
    segnalazioneService.readReport(s);

    assertTrue(s.isLetta());
  }

  @DisplayName("Test Unread Report")
  @ParameterizedTest
  @ArgumentsSource(SegnalazioneProvider.class)
  void unreadReport(Segnalazione s) {
    when(segnalazioneDao.doRetrieveById(s.getId())).thenReturn(s);
    doNothing().when(segnalazioneDao).doUpdate(s);
    segnalazioneService.unreadReport(s);

    assertFalse(s.isLetta());
  }
}