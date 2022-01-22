package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletGestioneWallet", value = "/wallet")
public class ServletGestioneWallet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    Utente utente = (Utente) request.getSession().getAttribute("utente");
    String action = request.getParameter("action");
    String amount = request.getParameter("amount");

    switch (action) {
      case "withdraw":
        if (!utenteService.withdraw(utente, Double.parseDouble(amount))) {
          request.setAttribute("error", "Errore durante il prelievo del saldo!");
        } else {
          request.setAttribute("message", "Saldo prelevato con successo");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/wallet.jsp");
        dispatcher.forward(request,response);
        break;

      case "deposit":
        ServletContext context= getServletContext();
        RequestDispatcher rd= context.getRequestDispatcher("/pay");
        rd.forward(request, response);
        break;
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  @Inject
  private UtenteService utenteService;

}
