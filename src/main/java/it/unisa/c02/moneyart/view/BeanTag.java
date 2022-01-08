package it.unisa.c02.moneyart.view;

import java.io.IOException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;

public abstract class BeanTag extends GenericTag {



  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }



  private int id;



}
