package it.unisa.c02.moneyart.gestione.opere.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce il recupero di un'opera.
 *
 */
@WebServlet(name = "ServletGetOpere", value = "/getUserArtworks")
public class ServletGetOpereUtente extends HttpServlet {


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

  @Inject
  private OperaService operaService;

}
