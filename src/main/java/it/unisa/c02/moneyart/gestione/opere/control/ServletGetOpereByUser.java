package it.unisa.c02.moneyart.gestione.opere.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Opera;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce il recupero di un'opera.
 *
 */
@WebServlet(name = "ServletGetOpereOwner", value = "/getArtworksByUser")
public class ServletGetOpereByUser extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String action = request.getParameter("action");
    int id = Integer.parseInt(request.getParameter("id"));
    List<Opera> opere;
    if (action.equals("created")) {
      opere = operaService.getArtworkByUser(id);
    } else {
      opere = operaService.getArtworkByOwner(id);
    }
    for (Opera opera : opere) {
      opera.setArtista(utenteService.getUserInformation(opera.getArtista().getId()));
    }
    request.setAttribute("opere", opere);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  @Inject
  private OperaService operaService;

  @Inject
  private UtenteService utenteService;
}
