package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.utils.payment.PayPalPayment;
import it.unisa.c02.moneyart.utils.payment.PaymentAdapter;
import it.unisa.c02.moneyart.utils.payment.StripePayment;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce il pagamento esterno.
 *
 */

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
      request.getRequestDispatcher("/walletInfo")
          .forward(request, response);

    }


  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
