package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
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
 * Questa servlet mostra il profilo utente.
 *
 */

@WebServlet(name = "ServletUserPage", value = "/userPage")
public class ServletUserPage extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    int id = utente.getId();
    utente = utenteService.getUserInformation(id);
    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/profiloUtente.jsp");
    request.setAttribute("utente", utente);
    dispatcher.forward(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }

  @Inject
  private UtenteService utenteService;
}
