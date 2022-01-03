import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.dao.AstaDaoImpl;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.OperaDaoImpl;
import it.unisa.c02.moneyart.model.dao.PartecipazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.RivenditaDaoImpl;
import it.unisa.c02.moneyart.model.dao.SegnalazioneDaoImpl;
import it.unisa.c02.moneyart.model.dao.UtenteDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.SegnalazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDAO;
import it.unisa.c02.moneyart.utils.production.GenericProducer;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.util.HashMap;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;


@WebListener
public class MainContext implements ServletContextListener {

  public void contextInitialized(ServletContextEvent sce) {
    System.out.println("Startup web application");

    ServletContext context = sce.getServletContext();

    DataSource ds = null;
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      ds = (DataSource) envCtx.lookup("jdbc/storage");

    } catch (NamingException e) {
      e.printStackTrace();
    }
    Map<Retriever.RetriverKey, GenericProducer<?>> istantiators = inizializeProducers();
    Retriever.setProducers(istantiators);

    context.setAttribute("DataSource", ds);
    System.out.println("DataSource creation: " + ds.toString());

  }

  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();

    DataSource ds = (DataSource) context.getAttribute("DataSource");
    context.removeAttribute("DataSource");

    System.out.println("DataSource deletion: " + ds.toString());
  }

  private HashMap<Retriever.RetriverKey, GenericProducer<?>> inizializeProducers() {
    HashMap<Retriever.RetriverKey, GenericProducer<?>> istantiators = new HashMap<>();

    //creazione DataSource
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");


      DataSource ds = (DataSource) envCtx.lookup("jdbc/storage");
      GenericProducer<DataSource> dataSourceInstantiator = () -> ds;
      istantiators.put(new Retriever.RetriverKey(DataSource.class.getName()),
          dataSourceInstantiator);


    } catch (NamingException e) {
      e.printStackTrace();
    }

    /*
    GenericProducer<NotificaDao> notificaIstantiator = () -> new NotificaDaoImpl();
    istantiators.put(new Retriever.RetriverKey(NotificaDao.class.getName()), notificaIstantiator);
    GenericProducer<AstaDao> astaIstantiator = () -> new AstaDaoImpl();
    istantiators.put(new Retriever.RetriverKey(AstaDao.class.getName()), astaIstantiator);
    GenericProducer<UtenteDAO> utenteIstantiator = () -> new UtenteDaoImpl();
    istantiators.put(new Retriever.RetriverKey(UtenteDAO.class.getName()), utenteIstantiator);
    GenericProducer<OperaDao> operaIstantiator = () -> new OperaDaoImpl();
    istantiators.put(new Retriever.RetriverKey(OperaDao.class.getName()), operaIstantiator);
    GenericProducer<RivenditaDao> rivenditaIstantiator = () -> new RivenditaDaoImpl();
    istantiators.put(new Retriever.RetriverKey(RivenditaDao.class.getName()), rivenditaIstantiator);
    GenericProducer<SegnalazioneDao> segnalazioneIstantiator = () -> new SegnalazioneDaoImpl();
    istantiators.put(new Retriever.RetriverKey(SegnalazioneDao.class.getName()),
        segnalazioneIstantiator);
    GenericProducer<PartecipazioneDao> partecipazioneIstantiator =
        () -> new PartecipazioneDaoImpl();
    istantiators.put(new Retriever.RetriverKey(PartecipazioneDao.class.getName()),
        partecipazioneIstantiator);

     */


    return istantiators;
  }
}