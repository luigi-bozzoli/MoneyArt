package it.unisa.c02.moneyart.model.beans;


/**
 * Questa classe rappresenta una Segnalazione.
 * Una segnalazione è caratterizzata da: id (autogenerato dal DB),
 * idAsta (identificativo dell'asta segnalata) e commento.
 */
public class Segnalazione {

  /**
   * Costruttore dell classe Segnalazione.
   *
   * @param idAsta identificativo dell'asta segnalata
   * @param commento eventuale commento della segnalazione
   * @param letta indica se la segnalazione è stata letta
   */
  public Segnalazione(Integer idAsta, String commento, boolean letta) {
    this.id = Segnalazione.NO_ID;
    this.idAsta = idAsta;
    this.commento = commento;
    this.letta = letta;
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
  public Integer getIdAsta() {
    return idAsta;
  }

  /**
   * Imposta l'identificativo dell'asta segnalata.
   *
   * @param idAsta identificativo dell'asta segnalata
   */
  public void setIdAsta(Integer idAsta) {
    this.idAsta = idAsta;
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

  public static final int NO_ID = -1;

  private Integer id;
  private Integer idAsta;
  private String commento;
  private boolean letta;


}
