package it.unisa.c02.moneyart.gestione.avvisi.notifica.control;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.model.beans.Notifica;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletDeleteNotifica", value = "/deleteNotification")
public class ServletDeleteNotifica extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int idNotifica = Integer.parseInt(request.getParameter("idNotifica"));
    Notifica notifica = new Notifica();
    notifica.setId(idNotifica);
    notificaService.deleteNotification(notifica);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request,response);

  }

  @Inject
  private NotificaService notificaService;
}
