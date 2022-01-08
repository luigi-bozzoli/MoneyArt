package it.unisa.c02.moneyart.view;


import it.unisa.c02.moneyart.gestione.vendite.rivendite.service.RivenditaService;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.utils.production.Retriever;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import java.io.IOException;

public class RivenditaTag extends BeanTag {

  @Override
  public void doTag() throws JspException, IOException {
    RivenditaService rivenditaService = Retriever.getIstance(RivenditaService.class);
    Rivendita rivendita = rivenditaService.getResell(getId());
    JspContext jspContext = getJspFragment().getJspContext();
    jspContext.setAttribute("rivendita", rivendita);
    getJspFragment().invoke(null);

  }
}
