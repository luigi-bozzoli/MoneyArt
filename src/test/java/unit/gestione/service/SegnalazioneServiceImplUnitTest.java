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

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
      asta.setId(3);
      String commento = "MarioPeluso ha copiato l'opera di alfcan attualmente all'asta.";

      Segnalazione s = new Segnalazione(asta, commento, false);
      s.setId(1);

      return Stream.of(
              Arguments.of(s)
      );
    }
  }

  @DisplayName("Test Get Reports")
  void getReports(String filter) {

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
  }

  @Test
  void removeReport() {
  }

  @Test
  void readReport() {
  }

  @Test
  void unreadReport() {
  }
}