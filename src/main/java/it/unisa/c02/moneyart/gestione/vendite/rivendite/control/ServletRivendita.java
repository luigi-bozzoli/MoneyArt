package it.unisa.c02.moneyart.gestione.vendite.rivendite.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "ServletRivendita", value = "/resell")
public class ServletRivendita extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Integer idOpera = Integer.parseInt(request.getParameter("idOpera"));
    Opera opera = operaService.getArtwork(idOpera);
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    if (!opera.getPossessore().getId().equals(utente.getId())) {
      request.setAttribute("errore", "non possiedi quest opera");

    } else {

      rivenditaService.resell(idOpera);
    }

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/marketplace.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  @Inject
  private RivenditaService rivenditaService;
  @Inject
  private OperaService operaService;
}
