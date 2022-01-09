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

@WebServlet(name = "ServletAnnullaAsta", value = "/camcelAuction")
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
    int astaId;
    try {
      astaId = Integer.parseInt(request.getParameter("id"));
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametri mancanti o invalidi");
      return;
    }
    Asta asta = astaService.getAuction(astaId);
    Opera opera = operaService.getArtwork(asta.getOpera().getId());
    if (opera.getArtista().getId() != utente.getId()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "non sei il creatore dell'asta");
      return;
    }
    astaService.removeAsta(asta);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private AstaService astaService;

  private OperaService operaService;
}
