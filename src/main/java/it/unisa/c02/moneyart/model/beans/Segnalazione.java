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
   */
  public Segnalazione(int idAsta, String commento) {
    this.id = Segnalazione.NO_ID;
    this.idAsta = idAsta;
    this.commento = commento;
  }

  /**
   * Restituisce l'identificativo della segnalazione.
   *
   * @return identificativo della segnalazione
   */
  public int getId() {
    return id;
  }

  /**
   * Imposta l'identificativo della segnalazione.
   *
   * @param id identificativo della segnalazione
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Restituisce l'identificativo dell'asta segnalata.
   *
   * @return identificativo dell'asta segnalata.
   */
  public int getIdAsta() {
    return idAsta;
  }

  /**
   * Imposta l'identificativo dell'asta segnalata.
   *
   * @param idAsta identificativo dell'asta segnalata
   */
  public void setIdAsta(int idAsta) {
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


  public static final int NO_ID = -1;

  private int id;
  private int idAsta;
  private String commento;


}
