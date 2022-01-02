import it.unisa.c02.moneyart.model.dao.AstaDaoImpl;
import it.unisa.c02.moneyart.model.dao.NotificaDaoImpl;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.utils.instantiation.GenericInstantiator;
import it.unisa.c02.moneyart.utils.instantiation.ObjectSource;
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
    Map<String, GenericInstantiator<?>> istantiators = inizializeIstantiators(ds);
    ObjectSource.setIstantiator(istantiators);

    context.setAttribute("DataSource", ds);
    System.out.println("DataSource creation: " + ds.toString());

  }

  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();

    DataSource ds = (DataSource) context.getAttribute("DataSource");
    context.removeAttribute("DataSource");

    System.out.println("DataSource deletion: " + ds.toString());
  }

  private HashMap<String, GenericInstantiator<?>> inizializeIstantiators(DataSource ds) {
    HashMap<String, GenericInstantiator<?>> istantiators = new HashMap<>();

    GenericInstantiator<NotificaDao> notificaIstantiator = () -> {
      return new NotificaDaoImpl(ds);
    };
    istantiators.put(NotificaDao.class.getName(), notificaIstantiator);

    GenericInstantiator<AstaDao> astaInstantiator = () -> {
      return new AstaDaoImpl(ds);
    };
    istantiators.put(AstaDao.class.getName(), astaInstantiator);


    return istantiators;
  }
}