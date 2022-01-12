package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

    switch (action) {
      case "inCorso":
        Asta.Stato inCorso = Asta.Stato.IN_CORSO;
        aste = astaService.getAuctionsByState(inCorso);
        break;
      case "terminate":
        Asta.Stato terminata = Asta.Stato.TERMINATA;
        aste = astaService.getAuctionsByState(terminata);
        break;
      default:
        aste = astaService.getAllAuctions();
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
