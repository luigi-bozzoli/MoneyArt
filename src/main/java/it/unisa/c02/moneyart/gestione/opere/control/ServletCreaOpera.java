package it.unisa.c02.moneyart.gestione.opere.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import javax.inject.Inject;
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

    Part immagine = request.getPart("immagine");
    String nome = request.getParameter("nome");
    String descrizione = request.getParameter("descrizione");

    Utente artista = (Utente) request.getSession().getAttribute("utente");
    Blob blob;
    try {
      blob = new SerialBlob(IOUtils.toByteArray(immagine.getInputStream()));
    } catch (SQLException e) {
      request.setAttribute("error",
          "errore nel caricamento dell'immagine");
      RequestDispatcher dispatcher = request
          .getRequestDispatcher(
              "/pages/aggiungi-opera.jsp"); // TODO: aggiungere il link alla pagina di creazione opera
      dispatcher.forward(request, response);
      return;
    }
    Opera nuovaOpera =
        new Opera(nome, descrizione, Opera.Stato.PREVENDITA, blob, artista, artista, null);
    nuovaOpera.setImmagine(blob);
    try {
      if (!operaService.addArtwork(nuovaOpera)) {
        request.setAttribute("error",
            "L'opera è già presente nella piattaforma!");
        RequestDispatcher dispatcher = request
            .getRequestDispatcher(
                "/pages/aggiungi-opera.jsp"); // TODO: aggiungere il link alla pagina di creazione opera
        dispatcher.forward(request, response);
      } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/dettaglio-opera?id="
            + nuovaOpera.getId() + ".jsp"); // TODO: aggiungere il link alla pagina del wallet
        dispatcher.forward(request, response);
      }
    } catch (Exception e) {

    }


  }


  @Inject
  private OperaService operaService;
}
