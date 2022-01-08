package it.unisa.c02.moneyart.gestione.avvisi.notifica.control;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ServletAddNotifica", value = "/addNotify")
public class ServletAddNotifica extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    for(String paramentro: PARAMETRI_NECESSARI){
      if(request.getParameter(paramentro) == null){
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "parametri mancanti");
        return;
      }
    }

    int idDestinatario = Integer.valueOf(request.getParameter("idDestinatario"));
    Utente utente = new Utente();
    utente.setId(idDestinatario);

    //sarà uguale a -1 se notifica riferita a rivendita
    int idAsta = Integer.valueOf(request.getParameter("idAsta"));
    Asta asta = new Asta();
    asta.setId(idAsta);

    //sarà uguale a -1 se notifica riferita a asta
    int idRivendita = Integer.valueOf(request.getParameter("idRivendita"));
    Rivendita rivendita = new Rivendita();
    rivendita.setId(idRivendita);

    Notifica.Tipo tipo = Notifica.Tipo.valueOf(request.getParameter("tipo"));

    String contenuto = request.getParameter("contenuto");

    Boolean letta = false;

    Notifica notifica = new Notifica(utente, asta, rivendita, tipo, contenuto, letta);

    notificaService.addNotification(notifica);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  private NotificaService notificaService;
  private UtenteService utenteService;

  private static final String[] PARAMETRI_NECESSARI = {"idDestinatario", "idAsta", "idRivendita", "tipo", "contenuto"};
}
