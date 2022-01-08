package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletRicaricaSaldo", value = "/deposit")
public class ServletRicaricaSaldo extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    double amount;
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    try {
      amount = Double.parseDouble(request.getParameter("amount"));
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametri incorreti o mancanti");
      return;
    }
    utenteService.deposit(utente, amount);
    //rimanda a qualche pagina


  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private UtenteService utenteService;
}
