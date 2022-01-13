package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import jnr.ffi.annotations.In;

@WebServlet(name = "ServletFollow", value = "/follow")
public class ServletFollow extends HttpServlet {
  @Override
  public void init() throws ServletException {
    super.init();
    this.utenteService = Retriever.getIstance(UtenteService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    String action = request.getParameter("action");
    RequestDispatcher dispatcher = request.getRequestDispatcher("");//Todo: mettere una view valida

    switch (action) {
      case "follow":
        int idFollowed = Integer.parseInt(request.getParameter("followed"));
        Utente followed = utenteService.getUserInformation(idFollowed);
        utenteService.follow(utente, followed);
        break;
      case "unfollow":
        utenteService.unfollow(utente);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + action);
    }
    dispatcher.forward(request, response);


  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private UtenteService utenteService;
}
