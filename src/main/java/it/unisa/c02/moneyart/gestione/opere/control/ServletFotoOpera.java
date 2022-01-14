package it.unisa.c02.moneyart.gestione.opere.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ServletFotoOpera", value = "/artworkPicture")
public class ServletFotoOpera extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    operaService = Retriever.getInstance(OperaService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int idOpera;
    response.setContentType("image/*");

    idOpera = Integer.parseInt(request.getParameter("id"));

    Opera opera = operaService.getArtwork(idOpera);
    if (opera == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "opera non trovato");
      return;
    }
    Blob imgBlob = opera.getImmagine();
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

    doGet(request,response);
  }

  private OperaService operaService;
}
