package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import com.google.gson.Gson;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;

import java.io.IOException;
import java.util.*;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet che si occupa di recuperare le aste da far visualizzare.
 */
@WebServlet(name = "ServletGetAste", value = "/getAuctions")
public class ServletGetAste extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    String action = request.getParameter("action");

    List<Asta> aste = null;

    Asta.Stato stato = null;

    switch (action) {
      case "inCorso":
        stato = Asta.Stato.IN_CORSO;
        aste = astaService.getAuctionsByState(stato);
        break;
      case "terminate":
        stato = Asta.Stato.TERMINATA;
        aste = astaService.getAuctionsByState(stato);
        break;
      default:
        aste = astaService.getAllAuctions();
        break;
    }

    boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

    if(ajax) {

      String criteria = request.getParameter("criteria");
      String order = request.getParameter("order");


      if (criteria != null && order != null) {

        List<Asta> asteFiltrate = null;

        switch (criteria) {
          case "Prezzo":
            asteFiltrate = astaService.getAuctionsSortedByPrice(order, stato);
            break;
          case "Followers":
            asteFiltrate = astaService.getAuctionsSortedByArtistFollowers(order, stato);
            break;
          case "Scadenza":
            asteFiltrate = astaService.getAuctionsSortedByExpirationTime(order, stato);
            break;
          default:
            asteFiltrate = aste;
            break;
        }

        for (Asta asta : asteFiltrate) {

          List<Partecipazione> partecipazioni = asta.getPartecipazioni();
          for(Partecipazione p : partecipazioni) {
            p.setAsta(null);
          }
          asta.setNotifiche(null);
          asta.setSegnalazioni(null);
        }

        String jsonFiltrato = new Gson().toJson(asteFiltrate);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonFiltrato);

      } else {

        for (Asta asta : aste) {
          List<Partecipazione> partecipazioni = asta.getPartecipazioni();
          for(Partecipazione p : partecipazioni) {
            p.setAsta(null);
          }
          asta.setNotifiche(null);
          asta.setSegnalazioni(null);
        }

        String json = new Gson().toJson(aste);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
      }
    } else {
        request.setAttribute("aste", aste);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doGet(request, response);
  }

  @Inject
  private AstaService astaService;

}
