package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Questa servlet gestisce il miglior offerente.
 *
 */
@WebServlet(name = "ServletBestOffer", value = "/getBestOffer")
public class ServletBestOffer extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int astaId = astaId = Integer.parseInt(request.getParameter("id"));

    Asta asta = astaService.getAuction(astaId);

    Partecipazione bestOffer = astaService.bestOffer(asta);

    request.setAttribute("bestOffer", bestOffer);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/??");
    //TODO: aggiungere link all view
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
