package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletPrelievoSaldo", value = "/withdraw")
public class ServletPrelievoSaldo extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    utenteService = Retriever.getIstance(UtenteService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    double amount;
    try {
      amount = Double.parseDouble(request.getParameter("amount"));
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametri incorreti o mancanti");
      return;
    }
    if (!utenteService.withdraw(utente, amount)) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "saldo disponibile insufficente");
      return;

    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private UtenteService utenteService;

}
