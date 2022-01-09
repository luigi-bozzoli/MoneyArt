package it.unisa.c02.moneyart.view;

import it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service.SegnalazioneService;
import it.unisa.c02.moneyart.model.beans.Segnalazione;
import it.unisa.c02.moneyart.utils.production.Retriever;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.List;

public class SegnalazioniTag extends BeanTag {

  @Override
  public void doTag() throws JspException, IOException {
    SegnalazioneService segnalazioneService = Retriever.getIstance(SegnalazioneService.class);

    List<Segnalazione> segnalazioni = segnalazioneService.getReports("");
    JspContext jspContext = getJspFragment().getJspContext();
    jspContext.setAttribute("notifiche", segnalazioni);
    getJspFragment().invoke(null);
  }
}
