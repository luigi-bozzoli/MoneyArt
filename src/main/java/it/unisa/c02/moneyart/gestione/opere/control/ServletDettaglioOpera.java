package it.unisa.c02.moneyart.gestione.opere.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.model.beans.Opera;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "ServletDettaglioOpera", value = "/artwork")
public class ServletDettaglioOpera extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int id = Integer.parseInt(request.getParameter("idOpera"));

    Opera opera = operaService.getArtwork(id);
    request.setAttribute("opera", opera);

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/artworkDetails.jsp");
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
