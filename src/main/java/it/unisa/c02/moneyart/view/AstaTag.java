package it.unisa.c02.moneyart.view;

import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;


public class AstaTag extends BeanTag {


  @Override
  public void doTag() throws JspException, IOException {
    AstaService astaService = Retriever.getIstance(AstaService.class);
    Asta asta = astaService.getAuction(getId());
    JspContext jspContext = getJspFragment().getJspContext();
    jspContext.setAttribute("asta", asta);
    getJspFragment().invoke(null);

  }



}
