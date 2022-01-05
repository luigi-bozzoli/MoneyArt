package it.unisa.c02.moneyart.gestione.vendite.rivendite.control;

import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "ServletSelectRivendita", value = "/selectRivendita")
public class ServletSelectRivendita extends HttpServlet {

  private RivenditaService rivenditaService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.rivenditaService = Retriever.getIstance(RivenditaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Rivendita rivendita = (Rivendita) request.getSession().getAttribute("rivendita");
    rivenditaService.getResells(rivendita.getStato().toString());
    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/rivendite.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }
}
