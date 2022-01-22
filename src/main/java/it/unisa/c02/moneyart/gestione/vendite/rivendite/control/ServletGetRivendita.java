package it.unisa.c02.moneyart.gestione.vendite.rivendite.control;

import com.google.gson.Gson;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Rivendita;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "ServletGetRivendita", value = "/getResell")
public class ServletGetRivendita extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    String id = request.getParameter("id");

    Rivendita rivendita = rivenditaService.getResell(Integer.parseInt(id));

    request.setAttribute("message", request.getAttribute("message"));
    if(rivendita.getStato().equals(Rivendita.Stato.IN_CORSO)) {
        request.setAttribute("rivendita", rivendita);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/rivendita.jsp");
        dispatcher.forward(request, response);
      } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/marketplace.jsp");
        dispatcher.forward(request, response);
      }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    doGet(request, response);
  }

  @Inject
  private RivenditaService rivenditaService;
}
