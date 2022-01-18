package unit.model.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;

import java.sql.*;
import javax.sql.DataSource;

import org.junit.jupiter.api.*;
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

    //*********************mock settings di opera + mock delle dipendenze di opera*********************************
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

   //*********************mock settings di datasource e connection *********************************
    when(dataSource.getConnection()).thenReturn(connection);
    when(dataSource.getConnection(anyString(), anyString())).thenReturn(connection);
    doNothing().when(connection).commit();
  }

  @AfterEach
  public void tearDown() {
  }


  @Test
  @DisplayName("doCreate")
  public void doCreate() throws SQLException {

    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setObject(anyInt(), anyString(), any());
    when(preparedStatement.executeUpdate()).thenReturn(1);
    when(preparedStatement.execute()).thenReturn(Boolean.TRUE);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE); //al primo ciclo ritorna true, al secondo false giustamente perché stiamo facendo una doCreate
    int operaId = opera.getId();
    when(resultSet.getInt(anyInt())).thenReturn(operaId);

    assertTrue(new OperaDaoImpl(dataSource).doCreate(opera));
  }

  @Test
  @DisplayName("doCreateCatch")
  public void doCreateCatch() throws SQLException {

    when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);

    assertTrue(!(new OperaDaoImpl(dataSource).doCreate(opera)));
  }

  @Test
  @DisplayName("doRetrieveById")
  void doRetrieveById() throws SQLException {

    //Dipendenze bean
    Utente userFollowed = new Utente("MarioVip", "RossiVip", opera.getImmagine(), "mariorossivip@unisa.it",
            "m_red_vip", null, new byte[10], 2000000.2);
    userFollowed.setId(99);

    Utente user = new Utente("Mario", "Rossi", opera.getImmagine(), "mariorossi@unisa.it",
            "m_red", userFollowed, new byte[10], 2.2);
    user.setId(possessore.getId());

    Utente artist = new Utente("Nick", "Arte", opera.getImmagine(), "nickarte@unisa.it",
            "n_art", userFollowed, new byte[10], 5000.2);
    artist.setId(artista.getId());

    //*********************************************************************************

    //oggetto tipo della test suite
    Opera op = new Opera(opera.getNome(), opera.getDescrizione(), opera.getStato(),
    opera.getImmagine(), user, artist, opera.getCertificato());
    op.setId(opera.getId());

    /*Istruisco i mock di connessione per questo metodo +
    Istruisco il finto comportamento di prelevazione dell'opera dal db
    */
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
    when(resultSet.next()).thenReturn(Boolean.TRUE);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.getObject("id", Integer.class)).thenReturn(op.getId());
    when(resultSet.getObject("id_utente", Integer.class)).thenReturn(op.getPossessore().getId());
    when(resultSet.getObject("id_artista", Integer.class)).thenReturn(op.getArtista().getId());
    when(resultSet.getObject("nome", String.class)).thenReturn(op.getNome());
    when(resultSet.getObject("descrizione", String.class)).thenReturn(op.getDescrizione());
    when(resultSet.getObject("immagine", Blob.class)).thenReturn(op.getImmagine());
    when(resultSet.getObject("certificato", String.class)).thenReturn(op.getCertificato());
    when(resultSet.getObject("stato", String.class)).thenReturn(op.getStato().toString());


    Opera opResult = new OperaDaoImpl(dataSource).doRetrieveById(op.getId());


    assertTrue(op.getId() == opResult.getId());
  }

  /*

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