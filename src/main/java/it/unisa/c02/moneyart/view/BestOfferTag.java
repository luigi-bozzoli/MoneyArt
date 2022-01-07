package it.unisa.c02.moneyart.view;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;

public class BestOfferTag extends GenericTag {


  @Override
  public void doTag() throws JspException, IOException {

    AstaService astaService = Retriever.getIstance(AstaService.class);
    Partecipazione bestOffer = astaService.bestOffer(asta);
    JspContext jspContext = getJspFragment().getJspContext();
    jspContext.setAttribute("bestOffer", bestOffer);
    getJspFragment().invoke(null);
  }

  public void setAsta(Asta asta) {
    this.asta = asta;
  }

  private Asta asta;
}
