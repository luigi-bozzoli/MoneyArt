package it.unisa.c02.moneyart.gestione.vendite.rivendite.control;

import com.google.gson.Gson;
import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Rivendita;

import java.io.IOException;
import java.util.List;
import javax.inject.Inject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "ServletGetRivendite", value = "/getResells")
public class ServletGetRivendite extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String action = request.getParameter("action");

    List<Rivendita> rivendite = null;

    Rivendita.Stato stato = null;

    switch (action) {
      case "inCorso":
        stato = Rivendita.Stato.IN_CORSO;
        rivendite = rivenditaService.getResellsByState(stato);
        break;
      case "terminate":
        stato = Rivendita.Stato.TERMINATA;
        rivendite = rivenditaService.getResellsByState(stato);
        break;
      default:
        stato = Rivendita.Stato.IN_CORSO;
        rivendite = rivenditaService.getResells();
        break;
    }

    boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

    if(ajax) {

      String criteria = request.getParameter("criteria");
      String order = request.getParameter("order");


      if (criteria != null && order != null) {

        List<Rivendita> rivenditeFiltrate = null;

        switch (criteria) {
          case "Prezzo":
            rivenditeFiltrate = rivenditaService.getResellsSortedByPrice(order, stato);
            break;
          case "Followers":
            rivenditeFiltrate = rivenditaService.getResellsSortedByArtistFollowers(order, stato);
            break;
          default:
            rivenditeFiltrate = rivendite;
            break;
        }

        for(Rivendita r : rivenditeFiltrate) {
          r.setNotifiche(null);
        }

        String jsonFiltrato = new Gson().toJson(rivenditeFiltrate);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonFiltrato);
      } else {


        for(Rivendita r : rivendite) {
          r.setNotifiche(null);
        }

        String json = new Gson().toJson(rivendite);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
      }
    } else {
      request.setAttribute("rivendite", rivendite);
    }


  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  @Inject
  private RivenditaService rivenditaService;
}
