package unit.model.dao;

import it.unisa.c02.moneyart.model.beans.*;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

// TODO: Gestire meglio i test con le eccezioni

@ExtendWith(MockitoExtension.class)
//@RunWith(JUnitPlatform.class)    TODO: Controllare la RunWith
@MockitoSettings(strictness = Strictness.LENIENT)
public class NotificaDaoImplUnitTest {

	@Mock
	private DataSource dataSource;
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement preparedStatement;
	@Mock
	private ResultSet resultSet;

	private NotificaDao notificaDao;

	public NotificaDaoImplUnitTest() {
		// Costruttore vuoto
	}

	@BeforeEach
	void setUp() throws SQLException {
		notificaDao = new NotificaDaoImpl(dataSource);
		when(dataSource.getConnection()).thenReturn(connection);
		doNothing().when(preparedStatement).setObject(anyInt(), any(), anyInt());
	}

	@AfterEach
	void tearDown() {
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4, 5, 6})
	void doCreateTest(int generatedId) throws SQLException {

		Notifica notifica = null;
		Asta a = null;
		Rivendita r = null;

		Utente u = new Utente();
		u.setId(1);

		if (generatedId % 2 == 0) {    		// Notifica per asta
			a = new Asta();
			a.setId(1);
			r = new Rivendita(); // id == null
			notifica = new Notifica(u, a, r, Notifica.Tipo.SUPERATO, "Contenuto.", false);
		} else {                          // Notifica per rivendita
			r = new Rivendita();
			r.setId(1);
			a = new Asta(); // id == null
			notifica = new Notifica(u, a, r, Notifica.Tipo.TERMINATA, "Contenuto.", true);
		}

		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
		when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(resultSet.getInt(1)).thenReturn(generatedId);

		notificaDao.doCreate(notifica);

		Assertions.assertEquals(notifica.getId(), generatedId);
	}

	@Test
	void doCreateCatchTest() throws SQLException {
		Notifica notifica = new Notifica(new Utente(), new Asta(), new Rivendita(),
				Notifica.Tipo.TERMINATA, "Contenuto.", false);
		SQLException ex = new SQLException();
		when(connection.prepareStatement(anyString(), anyInt())).thenThrow(ex);

		notificaDao.doCreate(notifica);

		Assertions.assertNull(notifica.getId());
	}

	@Test
	void doRetrieveByIdTest() throws SQLException {
		Utente u = new Utente();
		u.setId(1);
		Asta a = new Asta();
		a.setId(1);
		Rivendita r = new Rivendita();

		Notifica notifica = new Notifica(u, a, r, Notifica.Tipo.TERMINATA, "Contenuto", true);
		notifica.setId(1);

		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(Boolean.TRUE);
		when(resultSet.getObject("id", Integer.class)).thenReturn(notifica.getId());
		when(resultSet.getObject("id_utente", Integer.class)).thenReturn(notifica.getUtente().getId());
		when(resultSet.getObject("id_rivendita", Integer.class)).thenReturn(notifica.getRivendita().getId());
		when(resultSet.getObject("id_asta", Integer.class)).thenReturn(notifica.getAsta().getId());
		when(resultSet.getObject("letta", Boolean.class)).thenReturn(notifica.isLetta());
		when(resultSet.getObject("tipo", String.class)).thenReturn(notifica.getTipo().toString());
		when(resultSet.getObject("contenuto", String.class)).thenReturn(notifica.getContenuto());

		Notifica retrieved = notificaDao.doRetrieveById(notifica.getId());

		Assertions.assertEquals(notifica, retrieved);
	}

	@Test
	void doRetrieveByIdCatchTest() throws SQLException {
		SQLException ex = new SQLException();
		when(connection.prepareStatement(anyString())).thenThrow(ex);

		Notifica retrieved = notificaDao.doRetrieveById(1);

		Assertions.assertNull(retrieved);
	}

	@Test
	void doRetrieveAllTest() throws SQLException {
		Utente u = new Utente();
		u.setId(1);
		Asta a1 = new Asta();
		Asta a2 = new Asta();
		Rivendita r1 = new Rivendita();
		Rivendita r2 = new Rivendita();
		a1.setId(1);
		r2.setId(1);

		Notifica n1 = new Notifica(u, a1, r1, Notifica.Tipo.SUPERATO, "Contenuto.", false);
		Notifica n2 = new Notifica(u, a2, r2, Notifica.Tipo.TERMINATA, "Contenuto.", true);

		n1.setId(1);
		n2.setId(2);

		List<Notifica> oracle = Arrays.asList(n1, n2);

		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);  // 2 iterazioni, poi stop
		when(resultSet.getObject("id", Integer.class)).thenReturn(n1.getId(),
				n2.getId());
		when(resultSet.getObject("id_utente", Integer.class)).thenReturn(n1.getUtente().getId(),
				n2.getUtente().getId());
		when(resultSet.getObject("id_rivendita", Integer.class)).thenReturn(n1.getRivendita().getId(),
				n2.getRivendita().getId());
		when(resultSet.getObject("id_asta", Integer.class)).thenReturn(n1.getAsta().getId(),
				n2.getAsta().getId());
		when(resultSet.getObject("letta", Boolean.class)).thenReturn(n1.isLetta(),
				n2.isLetta());
		when(resultSet.getObject("tipo", String.class)).thenReturn(n1.getTipo().toString(),
				n2.getTipo().toString());
		when(resultSet.getObject("contenuto", String.class)).thenReturn(n1.getContenuto(),
				n2.getContenuto());

		List<Notifica> retrieved = notificaDao.doRetrieveAll(null);

		Assertions.assertArrayEquals(oracle.toArray(), retrieved.toArray());
	}

	@Test
	void doRetrieveAllCatchTest() throws SQLException {
		SQLException ex = new SQLException();
		when(connection.prepareStatement(anyString())).thenThrow(ex);

		List<Notifica> retrieved = notificaDao.doRetrieveAll(null);

		Assertions.assertNull(retrieved);
	}

	/*
	@Test
	void doUpdateTest() {
		// impl...
	}

	@Test doUpdateCatchTest() {
		// impl...
	}

	@Test
	void doDeleteTest() {
		// impl...
	}

	@Test
	void doDeleteCatchTest() {
		// impl...
	}
	*/

	@Test
	void doRetrieveAllByUserIdTest() throws SQLException {
		Utente u1 = new Utente();
		Utente u2 = new Utente();
		Asta a1 = new Asta();
		Asta a2 = new Asta();
		Rivendita r1 = new Rivendita();
		Rivendita r2 = new Rivendita();
		u1.setId(1);
		u2.setId(2);
		a1.setId(1);
		r2.setId(1);

		Notifica n1 = new Notifica(u1, a1, r1, Notifica.Tipo.SUPERATO, "Contenuto.", false);
		Notifica n2 = new Notifica(u2, a2, r2, Notifica.Tipo.TERMINATA, "Contenuto.", true);

		n1.setId(1);
		n2.setId(2);

		List<Notifica> oracle = Arrays.asList(n1);

		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(resultSet.getObject("id", Integer.class)).thenReturn(n1.getId());
		when(resultSet.getObject("id_utente", Integer.class)).thenReturn(n1.getUtente().getId());
		when(resultSet.getObject("id_rivendita", Integer.class)).thenReturn(n1.getRivendita().getId());
		when(resultSet.getObject("id_asta", Integer.class)).thenReturn(n1.getAsta().getId());
		when(resultSet.getObject("letta", Boolean.class)).thenReturn(n1.isLetta());
		when(resultSet.getObject("tipo", String.class)).thenReturn(n1.getTipo().toString());
		when(resultSet.getObject("contenuto", String.class)).thenReturn(n1.getContenuto());

		List<Notifica> retrieved = notificaDao.doRetrieveAllByUserId(1);

		Assertions.assertArrayEquals(oracle.toArray(), retrieved.toArray());
	}

	@Test
	void doRetrieveAllByUserIdCatchTest() throws SQLException {
		SQLException ex = new SQLException();
		when(connection.prepareStatement(anyString())).thenThrow(ex);

		List<Notifica> retrieved = notificaDao.doRetrieveAllByUserId(1);

		Assertions.assertNull(retrieved);
	}

	@Test
	void doRetrieveAllByAuctionIdTest() throws SQLException {
		Utente u1 = new Utente();
		Utente u2 = new Utente();
		Asta a1 = new Asta();
		Asta a2 = new Asta();
		Rivendita r1 = new Rivendita();
		Rivendita r2 = new Rivendita();
		u1.setId(1);
		u2.setId(2);
		a1.setId(1);
		a2.setId(2);

		Notifica n1 = new Notifica(u1, a1, r1, Notifica.Tipo.SUPERATO, "Contenuto.", false);
		Notifica n2 = new Notifica(u2, a2, r2, Notifica.Tipo.TERMINATA, "Contenuto.", true);

		n1.setId(1);
		n2.setId(2);

		List<Notifica> oracle = Arrays.asList(n2);

		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(resultSet.getObject("id", Integer.class)).thenReturn(n2.getId());
		when(resultSet.getObject("id_utente", Integer.class)).thenReturn(n2.getUtente().getId());
		when(resultSet.getObject("id_rivendita", Integer.class)).thenReturn(n2.getRivendita().getId());
		when(resultSet.getObject("id_asta", Integer.class)).thenReturn(n2.getAsta().getId());
		when(resultSet.getObject("letta", Boolean.class)).thenReturn(n2.isLetta());
		when(resultSet.getObject("tipo", String.class)).thenReturn(n2.getTipo().toString());
		when(resultSet.getObject("contenuto", String.class)).thenReturn(n2.getContenuto());

		List<Notifica> retrieved = notificaDao.doRetrieveAllByAuctionId(2);

		Assertions.assertArrayEquals(oracle.toArray(), retrieved.toArray());
	}

	@Test
	void doRetrieveAllByAuctionIdCatchTest() throws SQLException {
		SQLException ex = new SQLException();
		when(connection.prepareStatement(anyString())).thenThrow(ex);

		List<Notifica> retrieved = notificaDao.doRetrieveAllByAuctionId(1);

		Assertions.assertNull(retrieved);
	}

	@Test
	void doRetrieveAllByRivenditaIdTest() throws SQLException {
		Utente u1 = new Utente();
		Utente u2 = new Utente();
		Asta a1 = new Asta();
		Asta a2 = new Asta();
		Rivendita r1 = new Rivendita();
		Rivendita r2 = new Rivendita();
		u1.setId(1);
		u2.setId(2);
		r1.setId(1);
		r2.setId(2);

		Notifica n1 = new Notifica(u1, a1, r1, Notifica.Tipo.TERMINATA, "Contenuto.", false);
		Notifica n2 = new Notifica(u2, a2, r2, Notifica.Tipo.TERMINATA, "Contenuto.", true);

		n1.setId(1);
		n2.setId(2);

		List<Notifica> oracle = Arrays.asList(n1);

		when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
		doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
		when(preparedStatement.executeQuery()).thenReturn(resultSet);
		when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		when(resultSet.getObject("id", Integer.class)).thenReturn(n1.getId());
		when(resultSet.getObject("id_utente", Integer.class)).thenReturn(n1.getUtente().getId());
		when(resultSet.getObject("id_rivendita", Integer.class)).thenReturn(n1.getRivendita().getId());
		when(resultSet.getObject("id_asta", Integer.class)).thenReturn(n1.getAsta().getId());
		when(resultSet.getObject("letta", Boolean.class)).thenReturn(n1.isLetta());
		when(resultSet.getObject("tipo", String.class)).thenReturn(n1.getTipo().toString());
		when(resultSet.getObject("contenuto", String.class)).thenReturn(n1.getContenuto());

		List<Notifica> retrieved = notificaDao.doRetrieveAllByRivenditaId(1);

		Assertions.assertArrayEquals(oracle.toArray(), retrieved.toArray());
	}

	@Test
	void doRetrieveAllByRivenditaIdCatchTest() throws SQLException {
		SQLException ex = new SQLException();
		when(connection.prepareStatement(anyString())).thenThrow(ex);

		List<Notifica> retrieved = notificaDao.doRetrieveAllByRivenditaId(1);

		Assertions.assertNull(retrieved);
	}
}
