package unit.gestione.service;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void getNotificationsByUser() {
  }

  @Test
  void getNotification() {
  }

  @Test
  void readNotification() {
  }

  @Test
  void unreadNotification() {
  }

  @Test
  void deleteNotification() {
  }

  @Test
  void addNotification() {
  }


}