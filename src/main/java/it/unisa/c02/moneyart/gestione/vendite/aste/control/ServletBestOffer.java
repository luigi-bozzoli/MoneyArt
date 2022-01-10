package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ServletBestOffer", value = "/getBestOffer")
public class ServletBestOffer extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    astaService = Retriever.getIstance(AstaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    Utente utente = (Utente) request.getSession().getAttribute("utente");
    String action = request.getParameter("action");
    int astaId = astaId = Integer.parseInt(request.getParameter("id"));


    Asta asta = astaService.getAuction(astaId);

    if(asta.getOpera().getArtista().getId() != utente.getId()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "non sei il creatore dell'asta");
    }

    boolean risultato = false;
    switch (action) {
      //proprietario
      case "remove":
        risultato = astaService.annullaAsta(asta);
        break;

      //admin
      case "delete":
        risultato = astaService.removeAsta(asta);
        break;
    }

    if(!risultato) {
      request.setAttribute("error", "Errore durante l'annullamento dell'asta!");
    }

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/home.jsp");
    dispatcher.forward(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    doGet(request, response);
  }

  private AstaService astaService;

}
