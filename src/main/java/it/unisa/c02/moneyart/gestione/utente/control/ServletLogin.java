package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletLogin", value = "/login")
public class ServletLogin extends HttpServlet {

  private UtenteService utenteService;

  @Override
  public void init() throws ServletException {
    super.init();
    this.utenteService = Retriever.getIstance(UtenteService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String contextPath = request.getContextPath();
    contextPath += "/pages";
    HttpSession session = request.getSession();

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    Utente utente = utenteService.checkUser(username, password);
    if (utente != null) {
      session.setAttribute("utente", utente);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/home.jsp");
      dispatcher.forward(request, response);
    } else {
      request.setAttribute("error", "autenticazione fallita, Riprova");
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");
      dispatcher.forward(request, response);
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
