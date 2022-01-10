package it.unisa.c02.moneyart.gestione.vendite.rivendite.control;

import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.utils.production.Retriever;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ServletDettaglioRivendita", value = "/resellDetails")
public class ServletDettaglioRivendita extends HttpServlet {

  private RivenditaService rivenditaService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.rivenditaService = Retriever.getIstance(RivenditaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    rivenditaService.getResell(Integer.parseInt(request.getParameter("id")));
    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/rivendita.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    doGet(request, response);
  }
}