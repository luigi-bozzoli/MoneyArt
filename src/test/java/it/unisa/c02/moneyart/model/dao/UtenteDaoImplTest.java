package it.unisa.c02.moneyart.model.dao;

import static org.junit.jupiter.api.Assertions.*;

import hthurow.tomcatjndi.TomcatJNDI;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import java.io.File;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtenteDaoImplTest {

  private UtenteDao utenteDao;

  @BeforeEach
  void setUp() {
    TomcatJNDI tomcatJNDI = new TomcatJNDI();
    tomcatJNDI.processContextXml(new File("src/main/webapp/META-INF/context.xml"));
    tomcatJNDI.start();
    DataSource ds = null;
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      ds = (DataSource) envCtx.lookup("jdbc/storage");

    } catch (NamingException e) {
      e.printStackTrace();
    }
    utenteDao = new UtenteDaoImpl(ds);
  }

  @AfterEach
  void tearDown() {

  }

  @Test
  void doCreate() {

    Utente admin = new Utente(
        "Franco",
        "Battiato",
        null,
        "admin@moneyart.it",
        "franco",
        new Utente(),
        new byte[10],
        0d
    );
    utenteDao.doCreate(admin);
  }

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
}