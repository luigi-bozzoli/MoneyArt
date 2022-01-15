package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Utente;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ServletAnnullaAsta", value = "/cancelAuction")
public class ServletAnnullaAsta extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Utente utente = (Utente) request.getSession().getAttribute("utente");
    int astaId = astaId = Integer.parseInt(request.getParameter("id"));


    Asta asta = astaService.getAuction(astaId);

    if (asta.getOpera().getArtista().getId() != utente.getId()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "non sei il creatore dell'asta");
    }

    boolean risultato = astaService.annullaAsta(asta);

    if (!risultato) {
      request.setAttribute("error", "Errore durante l'annullamento dell'asta!");
      response.sendRedirect("/getAuctionDetails?idAsta=" + astaId);
    }

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/aste.jsp");
    dispatcher.forward(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  @Inject
  private AstaService astaService;

}
