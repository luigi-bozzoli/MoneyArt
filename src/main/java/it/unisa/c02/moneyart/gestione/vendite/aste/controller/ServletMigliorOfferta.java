package it.unisa.c02.moneyart.gestione.vendite.aste.controller;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet per risale alla migliore offerta di una determinata asta.
 */
@WebServlet(name = "ServletMigliorOfferta", value = "/bestOffer")
public class ServletMigliorOfferta extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    astaService = Retriever.getIstance(AstaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    Asta asta = new Asta();
    if (request.getParameter("idAsta") != null) {
      int id = Integer.valueOf(request.getParameter("idAsta"));
      asta.setId(id);

      Partecipazione partecipazione = astaService.bestOffer(asta);
      request.setAttribute("migliorOfferta", partecipazione);
    } else {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametro mancante");
    }

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/provaAsta.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

  private AstaService astaService;

}