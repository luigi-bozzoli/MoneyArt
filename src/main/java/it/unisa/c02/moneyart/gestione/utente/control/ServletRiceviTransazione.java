package it.unisa.c02.moneyart.gestione.utente.control;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.payment.PayPalReciver;
import it.unisa.c02.moneyart.utils.payment.PaymentReciverAdapter;
import it.unisa.c02.moneyart.utils.payment.StripeReciver;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletRiceviTransazione", value = "/recivePayment")
public class ServletRiceviTransazione extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String stripeId = request.getParameter("session_id");
    String payPalId = request.getParameter("paymentId");
    String paymentId;
    PaymentReciverAdapter paymentReciverAdapter;
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    if (stripeId != null) {
      paymentReciverAdapter = new StripeReciver();
      paymentId = stripeId;

    } else {
      paymentReciverAdapter = new PayPalReciver();
      paymentId = payPalId;
    }
    try {
      double amount = paymentReciverAdapter.recivePayment(paymentId);
      utenteService.deposit(utente, amount);
      request.getRequestDispatcher("/")
          .forward(request, response);//Todo: aggiungere vera view;

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
