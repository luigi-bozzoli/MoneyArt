package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.utils.payment.PayPalPayment;
import it.unisa.c02.moneyart.utils.payment.PaymentAdapter;
import it.unisa.c02.moneyart.utils.payment.StripePayment;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletExternalPayment", value = "/pay")
public class ServletExternalPayment extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    double amount = Double.parseDouble(request.getParameter("amount"));
    String paymentMethod = request.getParameter("method");
    PaymentAdapter payerAdapter;
    if (paymentMethod.equals("PayPal")) {
      payerAdapter = new PayPalPayment();
    } else {
      payerAdapter = new StripePayment();
    }

    try {

      response.sendRedirect(payerAdapter.makePayment(amount));
    } catch (Exception e) {
      e.printStackTrace();
      request.getRequestDispatcher("/payment.jsp")
          .forward(request, response);//Todo: aggiungere vera view;

    }


  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
