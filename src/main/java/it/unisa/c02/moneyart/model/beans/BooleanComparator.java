package it.unisa.c02.moneyart.model.beans;

public class BooleanComparator {

  private boolean aBoolean;

  public BooleanComparator(boolean bool) {
    this.aBoolean = bool;
  }

  public BooleanComparator and(boolean anotherBoolean) {
    return new BooleanComparator(this.aBoolean && anotherBoolean);
  }

  public boolean getValue() {
    return aBoolean;
  }
}
