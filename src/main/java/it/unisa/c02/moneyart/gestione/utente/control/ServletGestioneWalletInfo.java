package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletGestioneWalletInfo", value = "/walletInfo")
public class ServletGestioneWalletInfo extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    int id = utente.getId();
    utente = utenteService.getUserInformation(id);
    request.setAttribute("utente", utente);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/wallet.jsp");
    dispatcher.forward(request,response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  @Inject
  UtenteService utenteService;
}
