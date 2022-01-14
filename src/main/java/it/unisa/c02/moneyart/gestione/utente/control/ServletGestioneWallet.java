package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletGestioneWallet", value = "/wallet")
public class ServletGestioneWallet extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    utenteService = Retriever.getInstance(UtenteService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String action = request.getParameter("action");
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    double amount = Double.parseDouble(request.getParameter("amount"));


    switch (action) {

      case "withdraw":
        if (!utenteService.withdraw(utente, amount)) {
          request.setAttribute("error", "Errore durante il prelievo del saldo!");
        } else {
          request.setAttribute("message", "Saldo prelevato con successo");
        }
        break;

      case "deposit":
        if(!utenteService.deposit(utente, amount)) {
          request.setAttribute("error", "Errore durante il deposito!");
        } else {
          request.setAttribute("message", "Deposito avvenuto con successo");
        }
        break;
    }

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/wallet.jsp"); //// TODO: aggiungere il link alla pagina del wallet
    dispatcher.forward(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private UtenteService utenteService;

}
