import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
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

    GenericProducer<NotificaDao> notificaIstantiator = () -> {
      return new NotificaDaoImpl();
    };
    istantiators.put(new Retriever.RetriverKey(NotificaDao.class.getName()), notificaIstantiator);

    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");


      DataSource ds = (DataSource) envCtx.lookup("jdbc/storage");
      GenericProducer<DataSource> dataSourceInstantiator = () -> {
        return ds;
      };
      istantiators.put(new Retriever.RetriverKey(DataSource.class.getName()),dataSourceInstantiator);


    } catch (NamingException e) {
      e.printStackTrace();
    }


    return istantiators;
  }
}