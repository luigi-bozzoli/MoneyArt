package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletStoricoOfferte", value = "/offersHistory")
public class ServletStoricoOfferte extends HttpServlet {
  @Override
  public void init() throws ServletException {
    super.init();
    this.astaService = Retriever.getInstance(AstaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    List<Partecipazione> partecipazioni = astaService.getAllOffers();
    request.setAttribute("partecipazioni", partecipazioni);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private AstaService astaService;
}
