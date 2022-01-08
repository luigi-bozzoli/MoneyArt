package it.unisa.c02.moneyart.gestione.avvisi.notifica.control;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.model.beans.Notifica;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ServletUnreadNotifica", value = "/unreadNotification")
public class ServletUnreadNotifica extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if(request.getParameter("idNotifica") == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametri mancanti");
      return;
    }
    Notifica notifica = notificaService.getNotification(Integer.valueOf(request.getParameter("idNotifica")));
    notificaService.unreadNotification(notifica);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  private NotificaService notificaService;

}
