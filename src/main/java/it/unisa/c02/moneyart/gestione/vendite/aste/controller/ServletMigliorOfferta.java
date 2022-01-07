package it.unisa.c02.moneyart.gestione.vendite.aste.controller;

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
@WebServlet(name = "ServletMigliorOfferta", value = "/migliorOfferta")
public class ServletMigliorOfferta extends HttpServlet {

  private AstaService astaService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.astaService = Retriever.getIstance(AstaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    Asta asta = (Asta) request.getSession().getAttribute("asta");
    Partecipazione partecipazione = astaService.bestOffer(asta);
    request.setAttribute("offerta", partecipazione);
    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/asta.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

}