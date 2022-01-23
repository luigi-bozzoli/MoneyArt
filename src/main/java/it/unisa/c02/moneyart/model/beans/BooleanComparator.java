package it.unisa.c02.moneyart.model.beans;

/**
 * BooleanComparator effettua il confronto tra due valori booleani.
 */
public class BooleanComparator {

  public BooleanComparator(boolean bool) {
    this.bool = bool;
  }

  public BooleanComparator and(boolean anotherBoolean) {
    return new BooleanComparator(this.bool && anotherBoolean);
  }

  public boolean getValue() {
    return bool;
  }

  private boolean bool;
}
