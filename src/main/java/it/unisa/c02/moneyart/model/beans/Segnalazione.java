package it.unisa.c02.moneyart.model.beans;


import java.util.Objects;

/**
 * Questa classe rappresenta una Segnalazione.
 * Una segnalazione è caratterizzata da: id (autogenerato dal DB),
 * idAsta (identificativo dell'asta segnalata) e commento.
 */
public class Segnalazione {


  /**
   * Costruttore dell classe Segnalazione.
   *
   * @param asta     l'asta segnalata
   * @param commento eventuale commento della segnalazione
   * @param letta    indica se la segnalazione è stata letta
   */
  public Segnalazione(Asta asta, String commento, boolean letta) {
    this.id = null;
    this.asta = asta;
    this.commento = commento;
    this.letta = letta;
  }

  /**
   * Costruttore vuoto della classa Segnalazione.
   */
  public Segnalazione() {

  }

  /**
   * Restituisce l'identificativo della segnalazione.
   *
   * @return identificativo della segnalazione
   */
  public Integer getId() {
    return id;
  }

  /**
   * Imposta l'identificativo della segnalazione.
   *
   * @param id identificativo della segnalazione
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Restituisce l'identificativo dell'asta segnalata.
   *
   * @return identificativo dell'asta segnalata.
   */
  public Asta getAsta() {
    return asta;
  }

  /**
   * Imposta l'identificativo dell'asta segnalata.
   *
   * @param asta identificativo dell'asta segnalata
   */
  public void setAsta(Asta asta) {
    this.asta = asta;
  }

  /**
   * Restituisce il commento della segnalazione.
   *
   * @return il commento della segnalazione
   */
  public String getCommento() {
    return commento;
  }

  /**
   * Imposta il commento della segnalazione.
   *
   * @param commento commento della segnalazione
   */
  public void setCommento(String commento) {
    this.commento = commento;
  }

  /**
   * Verifica se la segnalazione è stata letta.
   *
   * @return un valore che identifica se la segnalazione sia stata letta o meno
   */
  public boolean isLetta() {
    return letta;
  }

  /**
   * Modifica lo stato di lettura della segnalazione.
   *
   * @param letta il nuovo stato della lettura
   */
  public void setLetta(boolean letta) {
    this.letta = letta;
  }

  /**
   * Restituisce la rappresentazione sotto forma di stringa di una segnalazione.
   *
   * @return la stringa che rappresenta segnalazione
   */
  @Override
  public String toString() {
    return "Segnalazione{"
        +
        "id=" + id
        +
        ", asta=" + asta
        +
        ", commento='" + commento + '\''
        +
        ", letta=" + letta
        +
        '}';
  }

  /**
   * Verifica l'uguaglianza tra due oggetti.
   *
   * @param o oggetto da confrontare con un'istanza della classe corrente
   * @return true se l'uguaglianza tra i due oggetti è verificato,
   *         false altrimenti
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }


    Segnalazione that = (Segnalazione) o;
    BooleanComparator booleanComparator =
        new BooleanComparator(Objects.equals(isLetta(), that.isLetta()));
    return booleanComparator
        .and(Objects.equals(getId(), that.getId()))
        .and(Objects.equals(getAsta(), that.getAsta()))
        .and(Objects.equals(getCommento(), that.getCommento()))
        .getValue();
  }

  /**
   * Genera un hash.
   *
   * @return un intero che rappresenta l'hash della classe
   */
  @Override
  public int hashCode() {
    return Objects.hash(getId(), getAsta(), getCommento(), isLetta());
  }

  /**
   * Variabili di istanza.
   *
   */
  private Integer id;
  private Asta asta;
  private String commento;
  private boolean letta;


}
