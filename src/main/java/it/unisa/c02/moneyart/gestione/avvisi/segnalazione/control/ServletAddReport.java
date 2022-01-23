package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.control;

import com.google.gson.Gson;
import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Questa servlet gestisce l'aggiunta di una segnalazione.
 *
 */

@WebServlet(name = "ServletAddReport", value = "/addReport")
public class ServletAddReport extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int idAsta = Integer.parseInt(request.getParameter("asta"));
    String commento = request.getParameter("commento");

    Asta asta = new Asta();
    asta.setId(idAsta);

    Boolean letta = false;

    Segnalazione segnalazione = new Segnalazione(asta, commento, letta);

    boolean segnalazioneOk = segnalazioneService.addReport(segnalazione);

    String json = new Gson().toJson(segnalazioneOk);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);


  }

  @Inject
  private SegnalazioneService segnalazioneService;


}
