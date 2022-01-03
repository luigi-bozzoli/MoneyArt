package it.unisa.c02.moneyart.model.beans;


/**
 * Questa classe rappresenta una Rivendita legata ad un'opera.
 * Una segnalazione è caratterizzata da: id (autogenerato dal DB),
 * idOpera (identificativo dell'opera a cui si riferisce),
 * stato e prezzo.
 */
public class Rivendita {

  /**
   * Costruttore dell classe Rivendita.
   *
   * @param opera opera a cui la rivendita fa riferimento
   * @param stato stato della rivendita
   * @param prezzo prezzo dell'opera rivenduta
   */
  public Rivendita(Opera opera, Stato stato, double prezzo) {
    this.id = Rivendita.NO_ID;
    this.opera = opera;
    this.stato = stato;
    this.prezzo = prezzo;
  }

  /**
   * Costruttore vuoto della classe Rivendita.
   */
  public Rivendita() {
  }

  /**
   * Restituisce l'identificativo della rivendita.
   *
   * @return identificativo della rivendita
   */
  public int getId() {
    return id;
  }

  /**
   * Imposta l'identificativo della rivendita.
   *
   * @param id identificativo della rivendita
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Restituisce l'opera rivenduta.
   *
   * @return opera rivenduta
   */
  public Opera getOpera() {
    return opera;
  }

  /**
   * Imposta l'opera rivenduta.
   *
   * @param opera opera rivenduta
   */
  public void setOpera(Opera opera) {
    this.opera = opera;
  }

  /**
   * Restituisce lo stato della rivendita.
   *
   * @return stato della rivendita
   */
  public Stato getStato() {
    return stato;
  }

  /**
   * Imposta lo stato della rivendita.
   *
   * @param stato stato della rivendita
   */
  public void setStato(Stato stato) {
    this.stato = stato;
  }

  /**
   * Restituisce il valore del prezzo a cui l'opera è stata rivenduta.
   *
   * @return il valore del prezzo a cui l'opera è stata rivenduta
   */
  public double getPrezzo() {
    return prezzo;
  }

  /**
   * Imposta il prezzo della rivendita.
   *
   * @param prezzo prezzo a cui l'opera viene rivenduta
   */
  public void setPrezzo(double prezzo) {
    this.prezzo = prezzo;
  }

  private int id;
  private Opera opera;
  private Stato stato;
  private double prezzo;

  /**
   * ENUM che rappresenta i possibili stati della rivendita.
   * IN_CORSO: la rivendita è in corso
   * TERMINATA: la rivendita è terminata
   */
  public enum Stato {
    IN_CORSO, TERMINATA
  }


  /**
   * costante per segnalare la mancanza di identificatore.
   */
  public static final int NO_ID = -1;
}
