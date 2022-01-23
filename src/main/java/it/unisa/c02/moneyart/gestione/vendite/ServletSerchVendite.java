package it.unisa.c02.moneyart.gestione.vendite;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletSerchVendite", value = "/searchVendite")
public class ServletSerchVendite extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String searchParams = request.getParameter("action");
    List<Rivendita> rivenditeInCorso = rivenditaService.getResellsByState(Rivendita.Stato.IN_CORSO);
    List<Asta> asteInCorso = astaService.getAuctionsByState(Asta.Stato.IN_CORSO);
    List<Rivendita> rivendite = new ArrayList<>();
    List<Asta> aste = new ArrayList<>();
    for (Rivendita rivendita : rivenditeInCorso) {
      if (rivendita.getOpera().getNome().contains(searchParams)) {
        rivendite.add(rivendita);
      }
    }
    for (Asta asta : asteInCorso) {
      if (asta.getOpera().getNome().contains(searchParams)) {
        aste.add(asta);
      }
    }
    request.setAttribute("aste", aste);
    request.setAttribute("rivendite", rivendite);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);

  }

  @Inject
  private AstaService astaService;

  @Inject
  private RivenditaService rivenditaService;
}
