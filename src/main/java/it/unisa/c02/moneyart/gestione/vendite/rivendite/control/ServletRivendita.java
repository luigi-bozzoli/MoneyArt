package it.unisa.c02.moneyart.gestione.vendite.rivendite.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "ServletRivendita", value = "/rivendita")
public class ServletRivendita extends HttpServlet {

  private RivenditaService rivenditaService;

  private OperaService operaService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.rivenditaService = Retriever.getIstance(RivenditaService.class);
    this.operaService = Retriever.getIstance(OperaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Integer idOpera = Integer.parseInt(request.getParameter("idOpera"));
    Opera opera = operaService.getArtwork(idOpera);
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    if (!opera.getPossessore().getId().equals(utente.getId())) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "non possiedi quest opera");
      return;
    }

    rivenditaService.resell(idOpera);
    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/opere.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }
}
