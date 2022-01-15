package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;


import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ServletGetUtente", value = "/getUser")
public class ServletGetUtente extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String username = request.getParameter("username");

    Utente utente = utenteService.getUserInformation(username);

    if (utente == null) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Nessun utente trovato!");
    } else {
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/utenteRicercato.jsp");
      request.setAttribute("utenteRicercato", utente);
      dispatcher.forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request,response);
  }

  @Inject
  private UtenteService utenteService;
}
