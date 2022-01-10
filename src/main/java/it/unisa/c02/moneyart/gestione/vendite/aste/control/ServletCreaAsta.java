package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.RequestDispatcher;
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
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Utente utente = (Utente) request.getSession().getAttribute("utente");
    String idOperaS = request.getParameter("id");
    String dataInizioS = request.getParameter("inizio");
    String dataFineS = request.getParameter("fine");

    int idOpera;
    Date dataInizio;
    Date dataFine;

    try {
      idOpera = Integer.parseInt(idOperaS);
      dataInizio = new SimpleDateFormat("dd/MM/yyyy").parse(dataInizioS);
      dataFine = new SimpleDateFormat("dd/MM/yyyy").parse(dataFineS);

    } catch (Exception e) {
      request.setAttribute("error",
        "Formato date non corrette!");
      response.sendRedirect("/pages/crea-asta.jsp"); // TODO: Aggiungere link alla pagina creazione asta
      return;

    }
    Opera opera = operaService.getArtwork(idOpera);
    if (opera == null || !utente.getId().equals(opera.getArtista().getId())) {
      request.setAttribute("error",
        "Non sei il creatore di quest'opera!");
      response.sendRedirect("/pages/crea-asta.jsp"); // TODO: Aggiungere link alla pagina creazione asta
      return;
    }
    Asta asta = new Asta(opera, dataInizio, dataFine, Asta.Stato.CREATA);
    if (!astaService.addAsta(asta)) {
      request.setAttribute("error",
        "Errore creazione asta!");
      response.sendRedirect("/pages/crea-asta.jsp"); // TODO: Aggiungere link alla pagina creazione asta
      return;
    }

    RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/dettaglio-asta?id="
      + asta.getId() + ".jsp"); // TODO: aggiungere il link alla pagina dell'asta
    dispatcher.forward(request, response);
  }

  private OperaService operaService;
  private AstaService astaService;

  private static final String[] PARAMETRI_NECESSARI = {"id", "inizio", "fine"};

}
