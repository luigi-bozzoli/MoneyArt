package integration.model.dao;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.*;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("NotificaDao")
public class NotificaDaoImplIntegrationTest {

	private static DataSource dataSource;

	private NotificaDao notificaDao;

	@BeforeAll
	public static void generalSetUp() throws SQLException, FileNotFoundException {
		Context initCtx = null;
		Context envCtx = null;
		try{
			initCtx = new InitialContext();
			envCtx = (Context) initCtx.lookup("java:comp/env");
			dataSource = (DataSource) envCtx.lookup("jdbc/storage");
		} catch (NamingException e) {
			TomcatJNDI tomcatJNDI = new TomcatJNDI();
			tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
			tomcatJNDI.start();
		}
		if(envCtx == null){
			try{
				initCtx = new InitialContext();
				envCtx = (Context) initCtx.lookup("java:comp/env");
				dataSource = (DataSource) envCtx.lookup("jdbc/storage");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		// Creazione database
		Connection connection = dataSource.getConnection();
		ScriptRunner runner = new ScriptRunner(connection);
		runner.setLogWriter(null);
		Reader reader = new BufferedReader(new FileReader("./src/main/java/it/unisa/c02/moneyart/model/db/ddl_moneyart.sql"));
		runner.runScript(reader);
		connection.close();
	}

	@BeforeEach
	public void setUp() throws SQLException, FileNotFoundException {
		notificaDao = new NotificaDaoImpl(dataSource);
		Connection connection = dataSource.getConnection();
		ScriptRunner runner = new ScriptRunner(connection);
		runner.setLogWriter(null);
		Reader reader = new BufferedReader(new FileReader("./src/test/database/test_notifica.sql"));
		runner.runScript(reader);
		connection.close();
	}

	@AfterEach
	public void tearDown() throws SQLException, FileNotFoundException {
		Connection connection = dataSource.getConnection();
		ScriptRunner runner = new ScriptRunner(connection);
		runner.setLogWriter(null);
		Reader reader = new BufferedReader(new FileReader("./src/test/database/clean_database.sql"));
		runner.runScript(reader);
		connection.close();
	}

	static class NotificaProvider implements ArgumentsProvider {

		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

			Utente noFollowed = new Utente();
			Asta noAsta = new Asta();
			Rivendita noRivendita = new Rivendita();

			Utente u3 = new Utente();
			u3.setId(3);
			Utente u4 = new Utente();
			u4.setId(4);

			Asta a1 = new Asta();
			a1.setId(1);

			Rivendita r1 = new Rivendita();
			r1.setId(1);

			return Stream.of(
				Arguments.of(new Notifica(
					u4,
					a1,
					noRivendita,
					Notifica.Tipo.SUPERATO,
					"Contenuto della notifica.",
					false
					)),
				Arguments.of(new Notifica(
					u3,
					noAsta,
					r1,
					Notifica.Tipo.TERMINATA,
					"Contenuto della notifica.",
					true
					))
				);
		}
	}

	static class ListNotificaProvider implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

			List<Notifica> list1 = new ArrayList<>(), list2 = new ArrayList<>();

			Utente noFollowed = new Utente();
			Asta noAsta = new Asta();
			Rivendita noRivendita = new Rivendita();

			Utente u2 = new Utente();
			u2.setId(2);
			Utente u3 = new Utente();
			u3.setId(3);
			Utente u4 = new Utente();
			u4.setId(4);
			Utente u5 = new Utente();
			u5.setId(5);
			Utente u6 = new Utente();
			u6.setId(6);


			Asta a1 = new Asta();
			a1.setId(1);
			Asta a2 = new Asta();
			a2.setId(2);

			Rivendita r1 = new Rivendita();
			r1.setId(1);

			/**** LISTA 1 ****/

			list1.add(new Notifica(
				u4,
				a1,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			));

			list1.add(new Notifica(
				u3,
				a1,
				noRivendita,
				Notifica.Tipo.VITTORIA,
				"Contenuto della notifica.",
				true
			));

			/**** LISTA 2 ****/

			list2.add(new Notifica(
				u5,
				a2,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			));

			list2.add(new Notifica(
				u6,
				a2,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				true
			));

			return Stream.of(
				Arguments.of(list1),
				Arguments.of(list2),
				Arguments.of(new ArrayList<Notifica>())
			);
		}
	}

	@Nested
	@DisplayName("Create")
	class CreateTest {

		@DisplayName("Create New Notification")
		@ParameterizedTest
		@ArgumentsSource(NotificaProvider.class)
		void doCreateNewNotificationTest(Notifica notifica) {
			notificaDao.doCreate(notifica);
			Notifica created = notificaDao.doRetrieveById(notifica.getId());
			assertEquals(notifica, created);
		}

		@DisplayName("Create New Notification with wrong user id")
		@Test
		void doCreateNewNotificationWithWrongUserIdTest() {

			Utente u1 = new Utente();
			Asta a1 = new Asta();
			Rivendita noRivendita = new Rivendita();

			u1.setId(999);
			a1.setId(1);

			Notifica notifica = new Notifica(
				u1,
				a1,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			);

			notificaDao.doCreate(notifica);

			assertNull(notifica.getId());
		}

		@DisplayName("Create New Notification with wrong auction id")
		@Test
		void doCreateNewNotificationWithWrongAuctionIdTest() {

			Utente u1 = new Utente();
			Asta a1 = new Asta();
			Rivendita noRivendita = new Rivendita();

			u1.setId(1);
			a1.setId(999);

			Notifica notifica = new Notifica(
				u1,
				a1,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			);

			notificaDao.doCreate(notifica);

			assertNull(notifica.getId());
		}

		@DisplayName("Create New Notification with wrong rivendita id")
		@Test
		void doCreateNewNotificationWithWrongRivenditaIdTest() {

			Utente u3 = new Utente();
			Asta noAsta = new Asta();
			Rivendita r1 = new Rivendita();

			u3.setId(3);
			r1.setId(999);

			Notifica notifica = new Notifica(
				u3,
				noAsta,
				r1,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			);

			notificaDao.doCreate(notifica);

			assertNull(notifica.getId());
		}
	}

	@Nested
	@DisplayName("Retrieve")
	class RetrieveTest {

		@DisplayName("Retrieve existing notification")
		@ParameterizedTest
		@ArgumentsSource(NotificaProvider.class)
		void doRetrieveExistingNotificationTest(Notifica notifica) {
			notificaDao.doCreate(notifica);

			Notifica retrieved = notificaDao.doRetrieveById(notifica.getId());

			assertEquals(notifica, retrieved);
		}

		@DisplayName("Retrieve non existing notification")
		@Test
		void doRetrieveNonExistingNotificationTest() {
			Utente u4 = new Utente();
			Asta a1 = new Asta();
			Rivendita noRivendita = new Rivendita();

			u4.setId(4);
			a1.setId(1);

			Notifica notifica = new Notifica(
				u4,
				a1,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			);

			notificaDao.doCreate(notifica);

			Notifica retrieved = notificaDao.doRetrieveById(notifica.getId() + 1);

			Assertions.assertNull(retrieved);
		}

		@DisplayName("Retrieve all notifications")
		@ParameterizedTest
		@ArgumentsSource(ListNotificaProvider.class)
		void doRetrieveAllNotificationsTest(List<Notifica> notifiche) {
			for(Notifica n : notifiche) {
				notificaDao.doCreate(n);
			}

			List<Notifica> retrieved = notificaDao.doRetrieveAll(null);

			Assertions.assertArrayEquals(notifiche.toArray(), retrieved.toArray());
		}

		@DisplayName("Retrieve all notifications by user id")
		@Test
		void doRetrieveAllByUserIdTest() {
			List<Notifica> notifiche = new ArrayList<>();
			List<Notifica> oracle = new ArrayList<>();

			Utente u3 = new Utente();
			u3.setId(3);
			Utente u4 = new Utente();
			u4.setId(4);

			Asta a1 = new Asta();
			a1.setId(1);
			Rivendita noRivendita = new Rivendita();

			Notifica notificaUtente4 = new Notifica(
				u4,
				a1,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			);

			Notifica notificaUtente3 = new Notifica(
				u3,
				a1,
				noRivendita,
				Notifica.Tipo.VITTORIA,
				"Contenuto della notifica.",
				true
			);

			notifiche.add(notificaUtente3);
			notifiche.add(notificaUtente4);

			oracle.add(notificaUtente4);

			for(Notifica n : notifiche) {
				notificaDao.doCreate(n);
			}

			List<Notifica> retrievedUtente4 = notificaDao.doRetrieveAllByUserId(u4.getId());

			Assertions.assertArrayEquals(oracle.toArray(), retrievedUtente4.toArray());
		}

		@DisplayName("Retrieve all notifications by auction id")
		@Test
		void doRetrieveAllByAuctionIdTest() {
			List<Notifica> notifiche = new ArrayList<>();
			List<Notifica> oracle = new ArrayList<>();

			Utente u4 = new Utente();
			u4.setId(4);
			Utente u5 = new Utente();
			u5.setId(5);

			Asta a1 = new Asta();
			a1.setId(1);
			Asta a2 = new Asta();
			a2.setId(2);
			Rivendita noRivendita = new Rivendita();

			Notifica notificaAsta1 = new Notifica(
				u4,
				a1,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			);

			Notifica notificaAsta2 = new Notifica(
				u5,
				a2,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			);

			notifiche.add(notificaAsta1);
			notifiche.add(notificaAsta2);

			oracle.add(notificaAsta1);

			for(Notifica n : notifiche) {
				notificaDao.doCreate(n);
			}

			List<Notifica> retrieved = notificaDao.doRetrieveAllByAuctionId(a1.getId());

			Assertions.assertArrayEquals(oracle.toArray(), retrieved.toArray());
		}

		@DisplayName("Retrieve all notifications by rivendita id")
		@Test
		void doRetrieveAllByRivenditaIdTest() {
			List<Notifica> notifiche = new ArrayList<>();
			List<Notifica> oracle = new ArrayList<>();

			Utente u3 = new Utente();
			u3.setId(3);
			Utente u10 = new Utente();
			u10.setId(10);

			Rivendita r1 = new Rivendita();
			r1.setId(1);
			Rivendita r2 = new Rivendita();
			r2.setId(2);
			Asta noAsta = new Asta();

			Notifica notificaRivendita1 = new Notifica(
							u3,
							noAsta,
							r1,
							Notifica.Tipo.TERMINATA,
							"Contenuto della notifica.",
							true
			);

			Notifica notificaRivendita2 = new Notifica(
							u10,
							noAsta,
							r2,
							Notifica.Tipo.TERMINATA,
							"Contenuto della notifica.",
							false
			);

			notifiche.add(notificaRivendita1);
			notifiche.add(notificaRivendita2);

			oracle.add(notificaRivendita2);

			for(Notifica n : notifiche) {
				notificaDao.doCreate(n);
			}

			List<Notifica> retrieved = notificaDao.doRetrieveAllByRivenditaId(r2.getId());

			Assertions.assertArrayEquals(oracle.toArray(), retrieved.toArray());
		}
	}

	@Nested
	@DisplayName("Update")
	class UpdateTest {

		@DisplayName("Update a notification")
		@Test
		void doUpdateTest() {
			Utente u4 = new Utente();
			u4.setId(4);

			Asta a1 = new Asta();
			a1.setId(1);

			Rivendita noRivendita = new Rivendita();

			Notifica toUpdate = new Notifica(
				u4,
				a1,
				noRivendita,
				Notifica.Tipo.SUPERATO,
				"Contenuto della notifica.",
				false
			);

			notificaDao.doCreate(toUpdate);

			toUpdate.setContenuto("Questo è il nuovo contenuto");

			notificaDao.doUpdate(toUpdate);

			Notifica updated = notificaDao.doRetrieveById(toUpdate.getId());

			assertEquals(toUpdate, updated);
		}
	}

	@Nested
	@DisplayName("Delete")
	class DeleteTest {

		@DisplayName("Delete a notification")
		@Test
		void doDeleteTest() {
			Utente u4 = new Utente();
			u4.setId(4);

			Asta a1 = new Asta();
			a1.setId(1);

			Rivendita noRivendita = new Rivendita();

			Notifica toDelete = new Notifica(
							u4,
							a1,
							noRivendita,
							Notifica.Tipo.SUPERATO,
							"Contenuto della notifica.",
							false
			);

			notificaDao.doCreate(toDelete);

			notificaDao.doDelete(toDelete);

			Notifica deletedNotifica = notificaDao.doRetrieveById(toDelete.getId());

			Assertions.assertNull(deletedNotifica);
		}

	}

}
