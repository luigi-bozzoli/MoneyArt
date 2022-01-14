package it.unisa.c02.moneyart.gestione.utente.control;

import com.google.gson.Gson;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.utils.production.Retriever;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ServletModificaInformazioniUtente", value = "/changeUserInformation")
public class ServletModificaInformazioniUtente extends HttpServlet {

  @Override
  public void init() throws ServletException {
    super.init();
    utenteService = Retriever.getIstance(UtenteService.class);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Utente utente = (Utente) request.getSession().getAttribute("utente");

    String password = request.getParameter("confirmPassword");


    Utente checkUtente = utenteService.checkUser(utente.getUsername(), password);
    boolean passOk;
    if(checkUtente == null) {
      passOk = false;
    } else {
      passOk = true;
    }

    String json = new Gson().toJson(passOk);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(json);

  }
  private UtenteService utenteService;


}
