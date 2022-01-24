package it.unisa.c02.moneyart.gestione.vendite.rivendite.control;

import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa serlvet gestisce l'acquisto diretto di un'opera.
 *
 */

@WebServlet(name = "ServletAcquistoDiretto", value = "/buyArtwork")
public class ServletAcquistoDiretto extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Integer idRivendita = Integer.parseInt(request.getParameter("idRivendita"));
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    Integer idUtente = utente.getId();
    rivenditaService.buy(idRivendita, idUtente);
    RequestDispatcher dispatcher =
        request.getRequestDispatcher("/pages/opereUtente.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  @Inject
  private RivenditaService rivenditaService;

}
