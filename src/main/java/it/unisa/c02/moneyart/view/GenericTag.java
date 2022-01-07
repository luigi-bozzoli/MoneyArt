package it.unisa.c02.moneyart.view;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;

public abstract class GenericTag implements SimpleTag {


  @Override
  public void setParent(JspTag jspTag) {

    this.parent = parent;

  }

  @Override
  public JspTag getParent() {
    return parent;
  }

  @Override
  public void setJspContext(JspContext jspContext) {

    this.jspContext = jspContext;

  }

  @Override
  public void setJspBody(JspFragment jspFragment) {

    this.jspFragment = jspFragment;

  }

  public JspContext getJspContext() {
    return jspContext;
  }

  public JspFragment getJspFragment() {
    return jspFragment;
  }

  public void setJspFragment(JspFragment jspFragment) {
    this.jspFragment = jspFragment;
  }

  private JspContext jspContext;

  private JspFragment jspFragment;

  private JspTag parent;
}
