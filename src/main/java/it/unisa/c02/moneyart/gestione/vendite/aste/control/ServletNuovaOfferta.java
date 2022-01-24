package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import com.google.gson.Gson;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce una nuova offerta.
 *
 */

@WebServlet(name = "ServletNuovaOfferta", value = "/newOffer")
public class ServletNuovaOfferta extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {


    Utente utente1 = (Utente) request.getSession().getAttribute("utente");
    Utente utente = utenteService.getUserInformation(utente1.getId());
    utente.setFotoProfilo(null);
    utente.setPartecipazioni(null);
    utente.setNotifiche(null);
    utente.setOpereInPossesso(null);
    utente.setOpereCreate(null);

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

  @Inject
  private UtenteService utenteService;
}
