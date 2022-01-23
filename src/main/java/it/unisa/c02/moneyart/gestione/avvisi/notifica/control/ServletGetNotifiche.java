package it.unisa.c02.moneyart.gestione.avvisi.notifica.control;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce il recupre delle notifiche.
 *
 */

@WebServlet(name = "ServletGetNotifiche", value = "/notifies")
public class ServletGetNotifiche extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Utente utente = (Utente) request.getSession().getAttribute("utente");

    List<Notifica> notifiche = notificaService.getNotificationsByUser(utente.getId());
    for (Notifica notifica : notifiche) {
      if (notifica.getAsta().getId() != null) {
        Asta asta = astaService.getAuction(notifica.getAsta().getId());
        notifica.setAsta(asta);
      } else {
        Rivendita rivendita = rivenditaService.getResell(notifica.getRivendita().getId());
        notifica.setRivendita(rivendita);
      }
    }
    request.setAttribute("notifiche", notifiche);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  @Inject
  private NotificaService notificaService;

  @Inject
  private AstaService astaService;

  @Inject
  private RivenditaService rivenditaService;

}