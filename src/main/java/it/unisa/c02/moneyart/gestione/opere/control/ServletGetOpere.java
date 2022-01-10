package it.unisa.c02.moneyart.gestione.opere.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.utils.production.Retriever;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "ServletGetOpere", value = "/getArtworks")
public class ServletGetOpere extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    operaService = Retriever.getIstance(OperaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    Utente utente = (Utente) request.getSession().getAttribute("utente");

    List<Opera> opere = operaService.getArtworkByUser(utente.getId());
    request.setAttribute("opera", opere);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/opereUtente.jsp");
    dispatcher.forward(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    doGet(request, response);
  }

  private OperaService operaService;

}
