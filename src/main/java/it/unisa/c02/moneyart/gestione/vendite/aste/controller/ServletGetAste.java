package it.unisa.c02.moneyart.gestione.vendite.aste.controller;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet che si occupa di recuperare le aste da far visualizzare.
 */
@WebServlet(name = "ServletGetAste", value = "/getAuctions")
public class ServletGetAste extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    astaService = Retriever.getIstance(AstaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String action = request.getParameter("action");

    List<Asta> aste = null;
    switch(action){
      case "inCorso":
        Asta.Stato inCorso = Asta.Stato.IN_CORSO;
        aste = astaService.getAuctionsByState(inCorso);
        break;
      case "terminate":
        Asta.Stato terminata = Asta.Stato.TERMINATA;
        aste = astaService.getAuctionsByState(terminata);
        break;
    }

    request.setAttribute("aste", aste);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

  private AstaService astaService;

}
