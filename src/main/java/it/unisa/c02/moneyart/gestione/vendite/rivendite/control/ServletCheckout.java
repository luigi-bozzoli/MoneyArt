package it.unisa.c02.moneyart.gestione.vendite.rivendite.control;

import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletCheckout", value = "/servletCheckout")
public class ServletCheckout extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Integer idRivendita = Integer.parseInt(request.getParameter("rivendita"));
    Rivendita rivendita = rivenditaService.getResell(idRivendita);
    Opera opera = rivendita.getOpera();

    request.setAttribute("rivendita", rivendita);
    request.setAttribute("opera", opera);

    RequestDispatcher dispatcher =
            request.getRequestDispatcher("/pages/checkout.jsp");
    dispatcher.forward(request, response);
  }

  @Inject
  RivenditaService rivenditaService;
}
