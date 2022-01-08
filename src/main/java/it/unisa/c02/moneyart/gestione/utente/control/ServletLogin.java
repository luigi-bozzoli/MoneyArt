package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ServletLogin", value = "/login")
public class ServletLogin extends HttpServlet {

  private UtenteService utenteService;

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
      session.setAttribute("utente", utente);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/home.jsp");
      dispatcher.forward(request, response);
    } else {
      request.setAttribute("error", "Autenticazione fallita, riprova!");
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");
      dispatcher.forward(request, response);
    }
  }
}
