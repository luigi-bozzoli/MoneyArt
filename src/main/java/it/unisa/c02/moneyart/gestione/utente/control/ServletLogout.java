package it.unisa.c02.moneyart.gestione.utente.control;
import it.unisa.c02.moneyart.model.beans.Utente;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ServletLogout", value = "/logout")
public class ServletLogout extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

      HttpSession session = request.getSession();

      session.invalidate();

      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/home.jsp");
      dispatcher.forward(request, response);
    }


  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    doGet(request, response);
  }

}
