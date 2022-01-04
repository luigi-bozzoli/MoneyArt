package it.unisa.c02.moneyart.gestione_avvisi;

import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Servlet", value = "/Servlet")
public class Servlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    NotificaDao dao = (NotificaDao) Retriever.getIstance(NotificaDao.class);
    List<Notifica> notifiche = dao.doRetrieveAll(null);
    for(Notifica n : notifiche){
      System.out.println(n);
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
