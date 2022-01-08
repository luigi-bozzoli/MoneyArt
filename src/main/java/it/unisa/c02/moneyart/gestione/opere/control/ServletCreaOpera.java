package it.unisa.c02.moneyart.gestione.opere.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import org.apache.commons.io.IOUtils;

import java.sql.Blob;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import javax.sql.rowset.serial.SerialBlob;

/**
 * Servlet per la creazione di un opera.
 */
@WebServlet(name = "ServletCreaOpera", value = "/newArtwork")
@MultipartConfig
public class ServletCreaOpera extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    for (String parametro : PARAMETRI_NECESSARI) {
      if (request.getParameter(parametro) == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametri mancanti");
        return;
      }
    }
    Part immagine = request.getPart("immagine");
    String nome = request.getParameter(PARAMETRI_NECESSARI[0]);
    String descrizione = request.getParameter(PARAMETRI_NECESSARI[1]);

    Utente artista = (Utente) request.getSession().getAttribute("utente");
    Blob blob;
    try {
      blob = new SerialBlob(IOUtils.toByteArray(immagine.getInputStream()));
    } catch (SQLException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "errore durante il processamento dell'immagine");
      return;
    }
    Opera nuovaOpera =
        new Opera(nome, descrizione, Opera.Stato.PREVENDITA, blob, artista, artista, null);
    nuovaOpera.setImmagine(blob);
    if (!operaService.addArtwork(nuovaOpera)) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          "l'opera è già presente sulla piattaforma");
    }


  }

  private static final String[] PARAMETRI_NECESSARI = {"nome", "descrizione"};


  private OperaService operaService;
}
