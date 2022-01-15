package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletBestOffer", value = "/getBestOffer")
public class ServletBestOffer extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int astaId = astaId = Integer.parseInt(request.getParameter("id"));

    Asta asta = astaService.getAuction(astaId);

    Partecipazione bestOffer = astaService.bestOffer(asta);

    request.setAttribute("bestOffer", bestOffer);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/??"); //TODO: aggiungere link all view
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
