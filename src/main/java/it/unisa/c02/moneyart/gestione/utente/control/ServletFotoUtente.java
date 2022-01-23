package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.sql.Blob;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

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
