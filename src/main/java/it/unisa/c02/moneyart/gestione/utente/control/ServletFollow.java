package it.unisa.c02.moneyart.gestione.utente.control;

import com.google.gson.Gson;
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
 * Questa servlet gestisce il meccanismo di following.
 *
 */

@WebServlet(name = "ServletFollow", value = "/follow")
public class ServletFollow extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();

    Utente utente = (Utente) session.getAttribute("utente");
    String action = request.getParameter("action");

    boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    boolean flag;

    switch (action) {
      case "follow":
        int idFollowed = Integer.parseInt(request.getParameter("followed"));
        Utente followed = utenteService.getUserInformation(idFollowed);

        if (!utenteService.follow(utente, followed)) {
          utenteService.unfollow(utente);
          utenteService.follow(utente, followed);
        }
        session.removeAttribute("utente");
        session.setAttribute("utente", utente);
        flag = true;
        break;
      case "unfollow":
        utenteService.unfollow(utente);
        session.removeAttribute("utente");
        session.setAttribute("utente", utente);
        flag = true;
        break;
      default:
        flag = false;
        throw new IllegalStateException("Unexpected value: " + action);
    }

    if (ajax) {
      String json = new Gson().toJson(flag);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(json);
    } else {
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/profiloUtente.jsp");
      dispatcher.forward(request, response);
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  @Inject
  private UtenteService utenteService;
}
