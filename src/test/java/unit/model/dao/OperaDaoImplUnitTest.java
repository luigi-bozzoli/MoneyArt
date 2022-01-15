package unit.model.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;

import java.io.InputStream;
import java.io.OutputStream;
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

  //__________________________immagine opera___________________________
  Blob image = new Blob() {
    @Override
    public long length() throws SQLException {
      return 0;
    }
  //____________________________________________________________________

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
      return new byte[0];
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
      return null;
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
      return 0;
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
      return 0;
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
      return 0;
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
      return 0;
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
      return null;
    }

    @Override
    public void truncate(long len) throws SQLException {

    }

    @Override
    public void free() throws SQLException {

    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
      return null;
    }
  }


  @BeforeAll
  public static void setUpClass() {
  }

  @AfterAll
  public static void tearDownClass() {
  }

  @BeforeEach
  public void setUp() throws SQLException {

    /*istruisco l'opera moccata
    when(opera.getId()).thenReturn(100);
    when(opera.getPossessore()).thenReturn(possessore);
    when(possessore.getId()).thenReturn(100); //il possessore ha id 100
    when(opera.getArtista()).thenReturn(artista);
    when(artista.getId()).thenReturn(90); //l'artista ha id 90
    when(opera.getNome()).thenReturn("Mario");
    when(opera.getDescrizione()).thenReturn("descrizioneDellOperaDiMario");
    doNothing().when(opera).getImmagine(); //fault possibile
    when(opera.getCertificato()).thenReturn("certificatoDellOperaDiMario");
    when(opera.getStato()).thenReturn(stato);
    when(stato.toString()).thenReturn("IN_POSSESSO");

    __________________________*/
    when(dataSource.getConnection()).thenReturn(connection);
    when(dataSource.getConnection(anyString(), anyString())).thenReturn(connection);
    doNothing().when(connection).commit();
    when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
    doNothing().when(preparedStatement).setObject(anyInt(), anyString(), any());
    when(preparedStatement.executeUpdate()).thenReturn(1);
    when(preparedStatement.execute()).thenReturn(Boolean.TRUE);
    when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE); //al primo ciclo ritorna true, al secondo false giustamente perché stiamo facendo una doCreate
    when(resultSet.getInt(anyInt())).thenReturn(opera.getId());
    System.out.println("\naooooooooooo"+opera.getDescrizione()+" "+opera.getId()); /*debug*/
  }

  @AfterEach
  public void tearDown() {
  }


  @Test
  public void doCreate() {

    when(opera.getId()).thenReturn(100);
    when(opera.getPossessore()).thenReturn(possessore);
    when(possessore.getId()).thenReturn(100); /*il possessore ha id 100*/
    when(opera.getArtista()).thenReturn(artista);
    when(artista.getId()).thenReturn(90); /*l'artista ha id 90*/
    when(opera.getNome()).thenReturn("Mario");
    when(opera.getDescrizione()).thenReturn("descrizioneDellOperaDiMario");
    when(opera.getImmagine()).thenReturn(image); /* fault possibile*/
    when(opera.getCertificato()).thenReturn("certificatoDellOperaDiMario");
    when(opera.getStato()).thenReturn(any());  /*fault certo*/

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