package it.unisa.c02.moneyart.gestione.utente.control;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import it.unisa.c02.moneyart.utils.payment.PayPalPayer;
import it.unisa.c02.moneyart.utils.payment.PayerAdapter;
import it.unisa.c02.moneyart.utils.payment.StripePayer;
import java.util.HashMap;
import java.util.Map;
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
    PayerAdapter payerAdapter;
    if (paymentMethod.equals("PayPal")) {
      payerAdapter = new PayPalPayer();
    } else {
      payerAdapter = new StripePayer();
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
