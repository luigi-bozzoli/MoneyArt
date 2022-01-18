package unit.model.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;

import java.sql.*;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OperaDaoImplUnitTest {

  @Mock
  private static DataSource dataSource;
  @Mock
  private static Connection connection;
  @Mock
  private static PreparedStatement preparedStatement;
  @Mock
  private static ResultSet resultSet;

  @Mock
  private static Opera opera; /*l'opera ha id 100*/
  @Mock
  private static Utente possessore; /*il possessore ha id 100*/
  @Mock
  private static Utente artista; /*l'artista dell'opera*/

  public enum StatoOpera {
    ALL_ASTA, IN_VENDITA, IN_POSSESSO, PREVENDITA
  }



  @BeforeAll
  public static void setUpClass() {

  }

  @AfterAll
  public static void tearDownClass() {
  }

  @BeforeEach
  public void setUp() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(dataSource.getConnection(anyString(), anyString())).thenReturn(connection);
    doNothing().when(connection).commit();
  }

  @AfterEach
  public void tearDown() {
  }


  @Test
  public void doCreate() throws SQLException {
    when(opera.getId()).thenReturn(100);  //opera ha id 100
    when(opera.getPossessore()).thenReturn(possessore);
    when(possessore.getId()).thenReturn(100); //il possessore ha id 100
    when(opera.getArtista()).thenReturn(artista);
    when(artista.getId()).thenReturn(90); //l'artista ha id 90
    when(opera.getNome()).thenReturn("Mario");
    when(opera.getDescrizione()).thenReturn("descrizioneDellOperaDiMario");
    Blob image = mock(Blob.class); //mocco un Blob per trattare l'immagine
    when(opera.getImmagine()).thenReturn(image); // fault possibile
    when(opera.getCertificato()).thenReturn("certificatoDellOperaDiMario");
    when(opera.getStato()).thenReturn(Opera.Stato.IN_POSSESSO);  //fault certo

    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setObject(anyInt(), anyString(), any());
    when(preparedStatement.executeUpdate()).thenReturn(1);
    when(preparedStatement.execute()).thenReturn(Boolean.TRUE);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE); //al primo ciclo ritorna true, al secondo false giustamente perché stiamo facendo una doCreate
    System.out.println("\naooooooooooo"+opera.getDescrizione()+" "+opera.getId()); //debug
    int operaId = opera.getId();
    when(resultSet.getInt(anyInt())).thenReturn(operaId);
    System.out.println("\naooooooooooo"+opera.getDescrizione()+" "+opera.getId()); //debug


    assertTrue(new OperaDaoImpl(dataSource).doCreate(opera));
  }


  /*

  @Test
  void doRetrieveById() {
  }

  @Test
  void doRetrieveAll() {
  }

  @Test
  void doUpdate() {
  }

  @Test
  void doDelete() {
  }

  @Test
  void doRetrieveAllByOwnerId() {
  }

  @Test
  void doRetrieveAllByArtistId() {
  }

  @Test
  void doRetrieveAllByName() {
  }

  */

}