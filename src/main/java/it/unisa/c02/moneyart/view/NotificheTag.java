package it.unisa.c02.moneyart.view;

import it.unisa.c02.moneyart.gestione.avvisi.notifica.service.NotificaService;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.utils.production.Retriever;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.List;

public class NotificheTag extends BeanTag {
  @Override
  public void doTag() throws JspException, IOException {
    NotificaService notificaService = Retriever.getIstance(NotificaService.class);

    List<Notifica> notifiche = notificaService.getNotificationsByUser(getId());
    JspContext jspContext = getJspFragment().getJspContext();
    jspContext.setAttribute("notifiche", notifiche);
    getJspFragment().invoke(null);
  }
}
