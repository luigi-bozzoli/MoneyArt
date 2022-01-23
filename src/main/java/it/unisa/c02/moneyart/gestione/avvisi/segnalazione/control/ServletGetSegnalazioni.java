package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.control;

import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.model.beans.Utente;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServletGetSegnalazioni", value = "/getReports")
public class ServletGetSegnalazioni extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String filter = request.getParameter("filter");

    if(filter == null) {
      filter = "";
    }

    List<Segnalazione> segnalazioni = segnalazioneService.getReports(filter);

    request.setAttribute("segnalazioni", segnalazioni);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/admin/segnalazioni.jsp");
    dispatcher.forward(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  @Inject
  private SegnalazioneService segnalazioneService;

}
