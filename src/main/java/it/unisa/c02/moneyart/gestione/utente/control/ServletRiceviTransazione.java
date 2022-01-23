package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.payment.PayPalPayment;
import it.unisa.c02.moneyart.utils.payment.PaymentAdapter;
import it.unisa.c02.moneyart.utils.payment.StripePayment;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa classe gestisce le transazioni.
 *
 */

@WebServlet(name = "ServletRiceviTransazione", value = "/recivePayment")
public class ServletRiceviTransazione extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String stripeId = request.getParameter("session_id");
    String payPalId = request.getParameter("paymentId");
    String paymentId;
    PaymentAdapter paymentReciverAdapter;
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    if (stripeId != null) {
      paymentReciverAdapter = new StripePayment();
      paymentId = stripeId;

    } else {
      paymentReciverAdapter = new PayPalPayment();
      paymentId = payPalId;
    }
    try {
      double amount = paymentReciverAdapter.recievePayment(paymentId);
      utenteService.deposit(utente, amount);
      request.getRequestDispatcher("/walletInfo")
          .forward(request, response);

    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "qualcosa è andato storto");
      e.printStackTrace();
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
