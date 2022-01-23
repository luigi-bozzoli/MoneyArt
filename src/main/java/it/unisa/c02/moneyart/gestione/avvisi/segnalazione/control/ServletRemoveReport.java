package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.control;

import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce l'eliminazione di una segnalazione.
 *
 */
@WebServlet(name = "ServletRemoveReport", value = "/removeReport")
public class ServletRemoveReport extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int id = Integer.parseInt(request.getParameter("idReport"));

    Segnalazione s = new Segnalazione();
    s.setId(id);

    segnalazioneService.removeReport(s);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  @Inject
  private SegnalazioneService segnalazioneService;

}
