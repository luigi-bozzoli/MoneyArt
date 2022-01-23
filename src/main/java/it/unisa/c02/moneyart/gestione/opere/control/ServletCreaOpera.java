package it.unisa.c02.moneyart.gestione.opere.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;

/**
 * Servlet per la creazione di un opera.
 *
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

    String immagine = request.getParameter("picture");
    String nome = request.getParameter("name");
    String descrizione = request.getParameter("description");

    Utente artista = (Utente) request.getSession().getAttribute("utente");
    immagine = immagine.replace("data:image/png;base64,", "");
    Blob blob;
    try {
      byte[] encodedByte = Base64.getDecoder().decode(immagine);
      blob = new SerialBlob(encodedByte);
    } catch (SQLException e) {
      request.setAttribute("error",
          "errore nel caricamento dell'immagine");
      RequestDispatcher dispatcher = request
          .getRequestDispatcher(
              "/pages/creaOpera.jsp");
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
                "/pages/creaOpera.jsp"); // TODO: aggiungere il link alla pagina di creazione opera
        dispatcher.forward(request, response);
      } else {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/opereUtente#opere-possedute-asta-tab.jsp");
        dispatcher.forward(request, response);
      }
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore certificato");
    }


  }


  @Inject
  private OperaService operaService;
}
