package unit.gestione.service;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotificaServiceImplUnitTest {

  @Mock
  private NotificaDao notificaDao;

  private NotificaService notificaService;

  @BeforeEach
  void setUp() {
    notificaService = new NotificaServiceImpl(notificaDao);
  }

  @AfterEach
  void tearDown() {
  }

  static class NotificaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

      Utente u1 = new Utente();
      u1.setId(1);
      Rivendita r1 = new Rivendita();
      r1.setId(1);
      Asta a1 = new Asta();
      a1.setId(1);
      Notifica n1 = new Notifica(u1, a1, r1, Notifica.Tipo.SUPERATO, null, true);
      n1.setId(1);

      Utente u2 = new Utente();
      u2.setId(2);
      Rivendita r2 = new Rivendita();
      r2.setId(2);
      Asta a2 = new Asta();
      a2.setId(2);
      Notifica n2 = new Notifica(u2, a2, r2, Notifica.Tipo.VITTORIA, null, false);
      n2.setId(2);

      Utente u3 = new Utente();
      u3.setId(3);
      Rivendita r3 = new Rivendita();
      r3.setId(3);
      Asta a3 = new Asta();
      a3.setId(3);
      Notifica n3 = new Notifica(u3, a3, r3, Notifica.Tipo.TERMINATA, null, true);
      n3.setId(3);
      return Stream.of(Arguments.of(n1), Arguments.of(n2), Arguments.of(n3));
    }

  }



  @Test
  @DisplayName("Test getNotificationByUser")
  void getNotificationsByUser() {

    List<Notifica> lista = new ArrayList<Notifica>();

    Utente u1 = new Utente();
    u1.setId(1);
    Rivendita r1 = new Rivendita();
    r1.setId(1);
    Asta a1 = new Asta();
    a1.setId(1);
    Notifica n1 = new Notifica(u1, a1, r1, Notifica.Tipo.SUPERATO, null, true);

    Rivendita r2 = new Rivendita();
    r2.setId(2);
    Asta a2 = new Asta();
    a2.setId(2);
    Notifica n2 = new Notifica(u1, a2, r2, Notifica.Tipo.SUPERATO, null, false);

    when(notificaDao.doRetrieveAllByUserId(u1.getId())).thenReturn(lista);

    Assertions.assertArrayEquals(lista.toArray(), notificaService.getNotificationsByUser(u1.getId()).toArray());
  }

  @ParameterizedTest
  @ArgumentsSource(NotificaProvider.class)
  @DisplayName("Test getNotification")
  void getNotification(Notifica n) {

      when(notificaDao.doRetrieveById(n.getId())).thenReturn(n);
      Assertions.assertEquals(n, notificaService.getNotification(n.getId()));
  }

  @ParameterizedTest
  @ArgumentsSource(NotificaProvider.class)
  @DisplayName("Test readNotification")
  void readNotification(Notifica n) {
    n.setLetta(false);

    when(notificaDao.doRetrieveById(n.getId())).thenReturn(n);
    doNothing().when(notificaDao).doUpdate(n);
    notificaService.readNotification(n);

    Assertions.assertTrue(n.isLetta());
  }

  @ParameterizedTest
  @ArgumentsSource(NotificaProvider.class)
  @DisplayName("unreadNotification")
  void unreadNotification(Notifica n) {

    when(notificaDao.doRetrieveById(n.getId())).thenReturn(n);
    doNothing().when(notificaDao).doUpdate(n);
    notificaService.unreadNotification(n);

    Assertions.assertFalse(n.isLetta());
  }
  @ParameterizedTest
  @ArgumentsSource(NotificaProvider.class)
  @DisplayName("deleteNotification")
  void deleteNotification(Notifica n) {

    when(notificaDao.doRetrieveById(n.getId())).thenReturn(n);
    doNothing().when(notificaDao).doDelete(n);
    notificaService.deleteNotification(n);

    verify(notificaDao, times(1)).doDelete(n);
  }

  @ParameterizedTest
  @ArgumentsSource(NotificaProvider.class)
  @DisplayName("addNotification")
  void addNotification(Notifica n) {

    when(notificaDao.doCreate(n)).thenReturn(Boolean.TRUE);

    boolean b = notificaService.addNotification(n);

    Assertions.assertTrue(b);
  }
}