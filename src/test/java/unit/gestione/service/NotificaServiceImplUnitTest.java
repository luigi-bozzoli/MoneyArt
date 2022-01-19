package unit.gestione.service;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaServiceImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotificaServiceImplUnitTest {

  @Mock
  private UtenteDao utenteDao;

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

  @DisplayName("Test get notification by user")
  void getNotificationsByUser() {

  }

  @DisplayName("Test get notification")
  void getNotification() {
  }

  @DisplayName("Test read notification")
  void readNotification() {
  }

  @DisplayName("Add Artwork Catch")
  void unreadNotification() {
  }

  @DisplayName("Add Artwork Catch")
  void deleteNotification() {
  }

  @DisplayName("Add Artwork Catch")
  void addNotification() {
  }
}