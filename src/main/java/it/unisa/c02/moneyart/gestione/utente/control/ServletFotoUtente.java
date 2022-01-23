package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/**
 * Questa servlet gestisce il caricamento della foto utente.
 *
 */

@WebServlet(name = "ServletFotoUtente", value = "/userPicture")
public class ServletFotoUtente extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int idUtente;
    response.setContentType("image/*");

    idUtente = Integer.parseInt(request.getParameter("id"));

    Utente utente = utenteService.getUserInformation(idUtente);

    Blob imgBlob = utente.getFotoProfilo();
    byte[] imageBytes;
    try {
      if (imgBlob == null) {
        imageBytes = IOUtils.toByteArray(
            getServletContext().getResourceAsStream("/static/image/user-placeholder.png"));

      } else {
        imageBytes = imgBlob.getBytes(1, (int) imgBlob.length());
      }
    } catch (SQLException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "errore durante l'elaborazione dell'immagine");
      return;
    }
    response.setContentLength(imageBytes.length);
    response.getOutputStream().write(imageBytes);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  @Inject
  private UtenteService utenteService;
}
