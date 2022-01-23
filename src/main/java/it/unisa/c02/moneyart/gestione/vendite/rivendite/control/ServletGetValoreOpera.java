package it.unisa.c02.moneyart.gestione.vendite.rivendite.control;

import com.google.gson.Gson;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletGetValoreOpera", value = "/getArtworkValue")
public class ServletGetValoreOpera extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int id = Integer.parseInt(request.getParameter("id"));
    Opera opera = new Opera();
    opera.setId(id);
    double prezzo = rivenditaService.getResellPrice(opera);
    String json = new Gson().toJson(prezzo);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request,response);
  }

  @Inject
  private RivenditaService rivenditaService;
}


