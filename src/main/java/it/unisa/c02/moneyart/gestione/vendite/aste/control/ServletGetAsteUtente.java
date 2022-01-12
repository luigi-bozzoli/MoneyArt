package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletGetAsteUtente", value = "/userAuctions")
public class ServletGetAsteUtente extends HttpServlet {
  @Override
  public void init() throws ServletException {
    super.init();
    this.astaService = Retriever.getIstance(AstaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    String action = request.getParameter("action");
    List<Asta> aste;
    switch (action) {
      case "won":
        aste = astaService.getWonAuctions(utente);
        request.setAttribute("asteVinte", aste);
        break;
      case "lost":
        aste = astaService.getLostAuctions(utente);
        request.setAttribute("astePerse", aste);
        break;
      case "current":
        aste = astaService.getCurrentAuctions(utente);
        request.setAttribute("asteInCorso", aste);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + action);
    }


  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private AstaService astaService;
}
