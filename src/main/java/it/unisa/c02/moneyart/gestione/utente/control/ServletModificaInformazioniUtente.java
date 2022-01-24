package it.unisa.c02.moneyart.gestione.utente.control;

import com.google.gson.Gson;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.rowset.serial.SerialBlob;
import org.apache.commons.io.IOUtils;

/**
 * Questa servlet gestisce la modifica delle informazioni del profilo utente.
 *
 */

@WebServlet(name = "ServletModificaInformazioniUtente", value = "/changeUserInformation")
@MultipartConfig
public class ServletModificaInformazioniUtente extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();
    Utente utente = (Utente) session.getAttribute("utente");
    utente = utenteService.getUserInformation(utente.getId());

    boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

    if (ajax) {
      String password = request.getParameter("password");

      Utente checkUtente = utenteService.checkUser(utente.getUsername(), password);

      boolean passOk;

      if (checkUtente == null) {
        passOk = false;
      } else {
        passOk = true;
      }

      String json = new Gson().toJson(passOk);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(json);
    } else {

      String name = request.getParameter("name");
      String surname = request.getParameter("surname");
      String username = request.getParameter("username");
      String email = request.getParameter("email");
      Part immagine = request.getPart("picture");
      String password = request.getParameter("new-password");

      Blob nuovaImmagine = null;

      if (immagine.getSize() != 0) {
        try {
          System.out.println("immagine trovata");
          nuovaImmagine = new SerialBlob(IOUtils.toByteArray(immagine.getInputStream()));
        } catch (SQLException e) {
          request.setAttribute("error", "Errore nel caricamento dell'immagine");
        }
      } else {
        System.out.println("immagine non trovata");
        nuovaImmagine = utente.getFotoProfilo();
      }

      byte[] newPassword = null;

      if (password == null || password.equals("")) {
        newPassword = utente.getPassword();
      } else {
        newPassword = utenteService.encryptPassword(password);
      }


      Utente newUtente =
          new Utente(name, surname, nuovaImmagine, email, username, utente.getSeguito(),
              newPassword, utente.getSaldo());
      newUtente.setId(utente.getId());
      try {

        utenteService.updateUser(newUtente);
      } catch (IllegalArgumentException e) {
        request.setAttribute("error", "Esiste già un account con queste informazioni");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/profiloUtente.jsp");
        dispatcher.forward(request, response);
        return;
      }

      session.removeAttribute("utente");
      session.setAttribute("utente", newUtente);

      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/profiloUtente.jsp");
      dispatcher.forward(request, response);
    }


  }

  @Inject
  private UtenteService utenteService;


}
