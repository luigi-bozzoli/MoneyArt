package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.control;

import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.model.beans.Segnalazione;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ServletAddReport", value = "/readReport")
public class ServletReadReport extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String action = request.getParameter("action");
    Segnalazione segnalazione =
        segnalazioneService.getReport(Integer.parseInt(request.getParameter("idNotifica")));

    switch (action) {

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
