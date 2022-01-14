package it.unisa.c02.moneyart.gestione.opere.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletRicercaOpera", value = "/serchArtworks")
public class ServletRicercaOpera extends HttpServlet {
  @Override
  public void init() throws ServletException {
    super.init();
    this.operaService = Retriever.getInstance(OperaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String name = request.getParameter("name");
    List<Opera> opere = operaService.searchOpera(name);
    request.setAttribute("opereCercate", opere);


  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private OperaService operaService;
}
