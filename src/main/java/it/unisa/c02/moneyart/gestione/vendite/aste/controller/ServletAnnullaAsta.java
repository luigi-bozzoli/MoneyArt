package it.unisa.c02.moneyart.gestione.vendite.aste.controller;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletAnnullaAsta", value = "/cancelAuction")
public class ServletAnnullaAsta extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    astaService = Retriever.getIstance(AstaService.class);
    operaService = Retriever.getIstance(OperaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Utente utente = (Utente) request.getSession().getAttribute("utente");
    String action = request.getParameter("action");
    int astaId = astaId = Integer.parseInt(request.getParameter("id"));


    Asta asta = astaService.getAuction(astaId);

    boolean risultato;
    switch (action) {
      case "remove":
        risultato = astaService.annullaAsta(asta);
        break;
      case "delete":
        risultato = astaService.removeAsta(asta);
        break;
    }

    //TODO: aggiungere i redirect alle pagine
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private AstaService astaService;

  private OperaService operaService;
}
