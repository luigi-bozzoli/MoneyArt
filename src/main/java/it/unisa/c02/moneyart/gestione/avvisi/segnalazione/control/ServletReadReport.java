package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.control;

import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce la lettura delle ì segnalazioni.
 *
 */
@WebServlet(name = "ServletReadReport", value = "/readReport")
public class ServletReadReport extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String action = request.getParameter("action");
    Segnalazione segnalazione =
        segnalazioneService.getReport(Integer.parseInt(request.getParameter("idReport")));

    switch (action) {

      default:
        break;

      case "read":
        segnalazioneService.readReport(segnalazione);
        break;

      case "unread":
        segnalazioneService.unreadReport(segnalazione);
        break;
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  @Inject
  private SegnalazioneService segnalazioneService;

}
