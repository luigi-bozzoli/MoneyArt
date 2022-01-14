package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet per la rimozione di un'asta.
 */
@WebServlet(name = "ServletRimuoviAsta", value = "/removeAuction")
public class ServletRimuoviAsta extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    astaService = Retriever.getInstance(AstaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int id = Integer.parseInt(request.getParameter("idAsta"));
    Asta asta = astaService.getAuction(id);

    boolean risultato = astaService.removeAsta(asta);

    if (!risultato) {
      request.setAttribute("error", "Errore durante l'annullamento dell'asta!");
      response.sendRedirect("/getAuctionDetails?idAsta=" + id);
    }

    RequestDispatcher dispatcher =
        request.getRequestDispatcher("/pages/aste.jsp");//Todo: aggiungere il link alla view
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  private AstaService astaService;

}