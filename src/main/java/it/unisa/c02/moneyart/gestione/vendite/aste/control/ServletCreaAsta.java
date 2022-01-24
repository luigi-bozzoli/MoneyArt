package it.unisa.c02.moneyart.gestione.vendite.aste.control;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet per la creazione e aggiunta di un'asta.
 *
 */
@WebServlet(name = "ServletCreaAsta", value = "/newAuction")
public class ServletCreaAsta extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String idOpera = request.getParameter("id");
    Opera opera = operaService.getArtwork(Integer.parseInt(idOpera));
    Utente utente = (Utente) request.getSession().getAttribute("utente");

    if (opera == null || !utente.getId().equals(opera.getArtista().getId())) {
      request.setAttribute("error",
          "Non sei il creatore di quest'opera!");
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/opereUtente.jsp");
      dispatcher.forward(request, response);
    } else {
      request.setAttribute("opera", opera);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/creaAsta.jsp");
      dispatcher.forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String idOperaS = request.getParameter("id");
    String dataInizioS = request.getParameter("inizio");
    String dataFineS = request.getParameter("fine");
    int idOpera;
    Date dataInizio;
    Date dataFine;
    idOpera = Integer.parseInt(idOperaS);
    Opera opera = operaService.getArtwork(idOpera);

    try {

      dataInizio = new SimpleDateFormat("yyyy-MM-dd").parse(dataInizioS);
      dataFine = new SimpleDateFormat("yyyy-MM-dd").parse(dataFineS);

    } catch (Exception e) {
      request.setAttribute("error",
          "Formato date non corrette!");
      request.setAttribute("opera", opera);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/creaAsta.jsp");
      dispatcher.forward(request, response);
      e.printStackTrace();
      return;

    }

    Utente utente = (Utente) request.getSession().getAttribute("utente");

    if (opera == null || !utente.getId().equals(opera.getArtista().getId())) {
      request.setAttribute("error",
          "Non sei il creatore di quest'opera!");
      request.setAttribute("opera", opera);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/creaAsta.jsp");
      dispatcher.forward(request, response);
      return;
    }
    Asta asta = new Asta(opera, dataInizio, dataFine, Asta.Stato.CREATA);
    if (!astaService.addAsta(asta)) {
      request.setAttribute("error",
          "Errore creazione asta!");
      request.setAttribute("opera", opera);
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/creaAsta.jsp");
      dispatcher.forward(request, response);
      return;
    }


    Date currentDate = new Date();

    if(currentDate.after(dataInizio) || currentDate.equals(dataInizio)) {
      response.sendRedirect(request.getContextPath() + "/pages/asteCreateUtente.jsp#aste-in-corso");
    } else {
      response.sendRedirect(request.getContextPath() + "/pages/asteCreateUtente.jsp");
    }



  }

  @Inject
  private OperaService operaService;
  @Inject
  private AstaService astaService;


}
