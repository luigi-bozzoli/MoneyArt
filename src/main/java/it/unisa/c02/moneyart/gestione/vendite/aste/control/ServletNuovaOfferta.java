package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Utente;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletNuovaOfferta", value = "/newOffer")
public class ServletNuovaOfferta extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {


    Utente utente = (Utente) request.getSession().getAttribute("utente");

    String json = new Gson().toJson(utente);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int astaId;
    double offerta;
    RequestDispatcher dispatcher;

    Utente utente = (Utente) request.getSession().getAttribute("utente");
    astaId = Integer.parseInt(request.getParameter("asta"));
    offerta = Double.parseDouble(request.getParameter("offerta"));

    Asta asta = astaService.getAuction(astaId);

    boolean offertaOk = astaService.partecipateAuction(utente, asta, offerta);
    if (offertaOk) {
      request.getSession().removeAttribute("utente");
      request.getSession().setAttribute("utente", utente);
    }
    String json = new Gson().toJson(offertaOk);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);

  }

  @Inject
  private AstaService astaService;
}
