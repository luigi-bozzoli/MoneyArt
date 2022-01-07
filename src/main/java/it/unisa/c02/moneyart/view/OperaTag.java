package it.unisa.c02.moneyart.view;

import it.unisa.c02.moneyart.gestione.opere.service.OperaService;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;

public class OperaTag extends BeanTag {
  @Override
  public void doTag() throws JspException, IOException {
    OperaService operaService = Retriever.getIstance(OperaService.class);
    Opera opera = operaService.getArtwork(getId());
    JspContext jspContext = getJspFragment().getJspContext();
    jspContext.setAttribute("opera", opera);
    getJspFragment().invoke(null);
  }
}
