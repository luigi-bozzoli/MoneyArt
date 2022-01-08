package it.unisa.c02.moneyart.gestione.utente.control;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
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

@WebServlet(name = "ServletModificaFotoProfilo", value = "/newProfilePicture")
@MultipartConfig
public class ServletModificaFotoProfilo extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Utente utente = (Utente) request.getSession().getAttribute("utente");
    Part immagine = request.getPart("immagine");
    if (immagine == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametri incorreti o mancanti");
      return;
    }
    Blob nuovaImmagine;
    try {
      nuovaImmagine = new SerialBlob(IOUtils.toByteArray(immagine.getInputStream()));
    } catch (SQLException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "errore nella processing dell'immagine");
      return;
    }
    utente.setFotoProfilo(nuovaImmagine);
    utenteService.updateUser(utente);

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  private UtenteService utenteService;
}
