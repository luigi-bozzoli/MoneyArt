package it.unisa.c02.moneyart.gestione.avvisi.notifica.control;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.model.beans.Notifica;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce la lettura delle notifiche.
 *
 */
@WebServlet(name = "ServletReadNotifica", value = "/readNotification")
public class ServletReadNotifica extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String action = request.getParameter("action");
    Notifica notifica =
        notificaService.getNotification(Integer.parseInt(request.getParameter("idNotifica")));

    switch (action) {

      default:
        break;

      case "read":
        notificaService.readNotification(notifica);
        break;

      case "unread":
        notificaService.unreadNotification(notifica);
        break;
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  @Inject
  private NotificaService notificaService;

}
