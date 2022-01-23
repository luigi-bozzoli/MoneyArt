package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Asta;
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
 * Questa servlet gestisce il recupero delle informazioni dell'admin.
 *
 */

@WebServlet(name = "ServletAdminInformation", value = "/adminPage")
public class ServletAdminInformation extends HttpServlet  {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    Utente utente = utenteService.getUserInformation("admin");
    int utentiRegistrati = utenteService.getAllUsers().size();
    int asteInCorso = astaService.getAuctionsByState(Asta.Stato.IN_CORSO).size();
    int rivendite = rivenditaService.getResells().size();

    request.setAttribute("admin", utente);
    request.setAttribute("utentiRegistrati", utentiRegistrati);
    request.setAttribute("asteInCorso", asteInCorso);
    request.setAttribute("rivendite", rivendite);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/admin/profiloAdmin.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

  @Inject
  private UtenteService utenteService;
  @Inject
  private AstaService astaService;
  @Inject
  private RivenditaService rivenditaService;

}
