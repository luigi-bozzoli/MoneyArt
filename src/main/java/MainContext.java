import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
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

      try {
        Connection con = ds.getConnection();

        DatabaseMetaData metaData = con.getMetaData();
        System.out.println("JDBC version: " + metaData.getJDBCMajorVersion() + "." + metaData.getJDBCMinorVersion());
        System.out.println("Product name: " + metaData.getDatabaseProductName());
        System.out.println("Product version: " + metaData.getDatabaseProductVersion());

      } catch (SQLException e) {
        e.printStackTrace();
      }

    } catch (NamingException e) {
      e.printStackTrace();
    }

    context.setAttribute("DataSource", ds);
    System.out.println("DataSource creation: " + ds.toString());

  }

  public void contextDestroyed(ServletContextEvent sce) {
    ServletContext context = sce.getServletContext();

    DataSource ds = (DataSource) context.getAttribute("DataSource");
    context.removeAttribute("DataSource");

    System.out.println("DataSource deletion: " + ds.toString());
  }
}