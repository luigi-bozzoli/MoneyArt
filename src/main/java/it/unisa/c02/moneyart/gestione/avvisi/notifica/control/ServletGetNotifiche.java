package it.unisa.c02.moneyart.gestione.avvisi.notifica.control;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.model.beans.Utente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="ServletGetNotifiche", value="/notifies")
public class ServletGetNotifiche extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    request.setAttribute("notifiche", notificaService.getNotificationsByUser(utente));
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  private NotificaService notificaService;

}
