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
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Questa classe gestisce il login.
 *
 */

@WebServlet(name = "ServletLogin", value = "/login")
public class ServletLogin extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    Utente utente = utenteService.checkUser(username, password);
    if (utente != null) {

      Set<Integer> idAmministratori = getAdminIds();

      if (idAmministratori.contains(utente.getId())) {
        session.setAttribute("admin", true);
        session.removeAttribute("utente");
      } else {
        session.setAttribute("utente", utente);
        session.removeAttribute("admin");
      }
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/home.jsp");

      dispatcher.forward(request, response);
    } else {
      request.setAttribute("error", "Autenticazione fallita, riprova!");
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");
      dispatcher.forward(request, response);
    }
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
