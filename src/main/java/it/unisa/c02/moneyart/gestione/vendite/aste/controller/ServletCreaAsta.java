package it.unisa.c02.moneyart.gestione.vendite.aste.controller;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet per la creazione e aggiunta di un'asta.
 */
@WebServlet(name = "ServletCreaAsta", value = "/newAuction")
public class ServletCreaAsta extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    astaService = Retriever.getIstance(AstaService.class);
    operaService = Retriever.getIstance(OperaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    for (String parametro : PARAMETRI_NECESSARI) {
      if (request.getParameter(parametro) == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametri mancanti");
        return;
      }
    }
    String idOperaS = request.getParameter(PARAMETRI_NECESSARI[0]);
    String dataInizioS = request.getParameter(PARAMETRI_NECESSARI[1]);
    String dataFineS = request.getParameter(PARAMETRI_NECESSARI[2]);
    int idOpera;
    Date dataInizio;
    Date dataFine;
    try {

      idOpera = Integer.parseInt(idOperaS);
      dataInizio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInizioS);
      dataFine = new SimpleDateFormat("dd/MM/yyyy").parse(dataFineS);
      //
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          "i parametri non sono nel giusto formato");
      return;
    }
    Opera opera = operaService.getArtwork(idOpera);
    if (opera == null || !utente.getId().equals(opera.getArtista().getId())) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          "non sei il creatore di quest opera");
      return;
    }
    Asta asta = new Asta(opera, dataInizio, dataFine, Asta.Stato.CREATA);
    if (!astaService.addAsta(asta)) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          "errore nella creazione dell'asta");
      return;
    }


  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private OperaService operaService;
  private AstaService astaService;

  private static final String[] PARAMETRI_NECESSARI = {"id", "inizio", "fine"};

}
