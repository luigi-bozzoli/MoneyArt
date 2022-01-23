package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Questa servlet gestisce la registrazione.
 *
 */

@WebServlet(name = "ServletSignup", value = "/signup")
public class ServletSignup extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();

    String name = request.getParameter("name");
    String surname = request.getParameter("surname");
    String email = request.getParameter("email");
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    Utente utente = new Utente(name, surname, null, email, username, new Utente(),
        utenteService.encryptPassword(password), 0D);

    boolean notFound = utenteService.signUpUser(utente);


    if (notFound) {
      session.setAttribute("utente", utente);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/home.jsp");
      dispatcher.forward(request, response);
    } else {
      request.setAttribute("error", "Esiste già un account con questi dati, riprova!");
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/signup.jsp");
      dispatcher.forward(request, response);
    }
  }

  @Inject
  private UtenteService utenteService;
}
