package it.unisa.c02.moneyart.gestione.utente.control;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce il recupero degli utenti.
 *
 */

@WebServlet(name = "ServletGetUtenti", value = "/getUsers")
public class ServletGetUtenti extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    List<Utente> utenti = utenteService.getAllUsers();

    Set<Integer> adminIds = getAdminIds();

    utenti.removeIf((Utente utente) -> adminIds.contains(utente.getId()));



    boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

    if (ajax) {

      String criteria = request.getParameter("criteria");
      String order = request.getParameter("order");


      List<Utente> utentiFiltrati = null;
      if (criteria != null && order != null) {

        utentiFiltrati = utenteService.getUsersSortedByFollowers(order);

        utentiFiltrati.removeIf((Utente utente) -> adminIds.contains(utente.getId()));

      } else {
        utentiFiltrati =  utenti;
      }

      String json = new Gson().toJson(utentiFiltrati);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(json);
    } else {
      request.setAttribute("utenti", utenti);
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);

  }

  private Set<Integer> getAdminIds() {
    Type tipo = new TypeToken<Set<Integer>>() {
    }.getType();
    Gson gson = new Gson();
    String filename = "/amministratori.json";
    InputStream in = getServletContext().getResourceAsStream(filename);
    InputStreamReader inR = new InputStreamReader(in);
    JsonReader reader = new JsonReader(inR);
    return gson.fromJson(reader, tipo);

  }

  @Inject
  private UtenteService utenteService;
}
