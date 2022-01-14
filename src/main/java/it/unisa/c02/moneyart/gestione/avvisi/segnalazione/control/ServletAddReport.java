package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.control;

import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.*;
import it.unisa.c02.moneyart.utils.production.Retriever;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ServletAddReport", value = "/addReport")
public class ServletAddReport extends HttpServlet {
  
  @Override
  public void init() throws ServletException {
    super.init();
    segnalazioneService = Retriever.getInstance(SegnalazioneService.class);
    astaService = Retriever.getInstance(AstaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int idAsta = Integer.parseInt(request.getParameter("asta"));
    String commento = request.getParameter("commento");

    Asta asta = new Asta();
    asta.setId(idAsta);

    Boolean letta = false;

    Segnalazione segnalazione = new Segnalazione(asta, commento, letta);

    segnalazioneService.addReport(segnalazione);

    request.setAttribute("reportMessage", "Segnalazione inviata con successo!");
    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/"); //TODO: link alla pagina dell'asta segnalata
    dispatcher.forward(request, response);
  }

  private SegnalazioneService segnalazioneService;
  private AstaService astaService;

}
