package it.unisa.c02.moneyart.gestione.vendite.aste.controller;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.sql.Blob;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletFotoUtente", value = "/userPicture")
public class ServletFotoUtente extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int idUtente;
    response.setContentType("image/*");
    try {

      idUtente = Integer.parseInt(request.getParameter("id"));
    } catch (NumberFormatException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametri mancanti");
      return;

    }
    Utente utente = utenteService.getUserInformation(idUtente);
    if (utente == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "utente non trovato");
      return;
    }
    Blob imgBlob = utente.getFotoProfilo();
    byte[] imageBytes;
    try {
      imageBytes = imgBlob.getBytes(1, (int) imgBlob.length());
    } catch (SQLException e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "errore durante il processamento dell'immagine");
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

  private UtenteService utenteService;
}
