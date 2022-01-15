package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletRicercaUtente", value = "/serchUsers")
public class ServletRicercaUtente extends HttpServlet {



  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String name = request.getParameter("name");
    List<Utente> utenti = utenteService.searchUsers(name);
    request.setAttribute("utentiCercati", utenti);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);

  }

  @Inject
  private UtenteService utenteService;
}
