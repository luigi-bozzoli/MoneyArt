package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletNuovaOfferta", value = "/newOffer")
public class ServletNuovaOfferta extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    astaService = Retriever.getInstance(AstaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int astaId;
    double offerta;
    RequestDispatcher dispatcher;
    Utente utente = (Utente) request.getSession().getAttribute("utente");

    astaId = Integer.parseInt(request.getParameter("asta"));
    offerta = Double.parseDouble(request.getParameter("offerta"));

    Asta asta = astaService.getAuction(astaId);

    if (astaService.partecipateAuction(utente, asta, offerta)) {
      request.setAttribute("message", "offerta registrata correttamente!");
    } else {
      request.setAttribute("errore", "problema con la registrazione dell'offerta");
    }

    dispatcher = request.getRequestDispatcher("pages/??"); // TODO: aggiungere il link alla view
    dispatcher.forward(request, response);


  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private AstaService astaService;
}
