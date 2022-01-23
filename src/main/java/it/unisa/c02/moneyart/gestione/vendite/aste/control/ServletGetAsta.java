package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import com.google.gson.Gson;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
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
 * Questa servlet gestisce il recupero di un'asta.
 *
 */
@WebServlet(name = "ServletGetAsta", value = "/getAuction")
public class ServletGetAsta extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    if (request.getSession().getAttribute("admin") != null) {
      request.setAttribute("admin", true);
    }

    String id = request.getParameter("id");

    Asta asta = astaService.getAuction(Integer.parseInt(id));

    boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

    if (ajax) {

      List<Partecipazione> partecipazioni = asta.getPartecipazioni();

      for (Partecipazione p : partecipazioni) {
        p.setAsta(null);
        asta.setNotifiche(null);
        asta.setSegnalazioni(null);
      }

      String json = new Gson().toJson(asta);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(json);
    } else {
      request.setAttribute("message", request.getAttribute("message"));
      if (asta.getStato().equals(Asta.Stato.IN_CORSO)) {
        request.setAttribute("asta", asta);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/asta.jsp");
        dispatcher.forward(request, response);
      } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/esplora.jsp");
        dispatcher.forward(request, response);
      }
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
