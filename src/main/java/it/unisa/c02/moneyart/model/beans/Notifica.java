package it.unisa.c02.moneyart.model.beans;



/**
 * Questa classe rappresenta una notifica legata ad una vendita
 * è caratterizzata da:
 * un id,
 * l'id dell'utente che riceve la notifica,
 * l'id dell'asta a cui si riferisce,
 * l'id della vendita diretta a cui si riferisce,
 * un tipo che descrive il motivo della notifica,
 * e il contenuto della notifica
 */
public class Notifica {

  /**
   * Costruttore della classe Notifica.
   *
   * @param idUtente id del destinatario
   * @param idAsta id dell'asta a cui l'opera si riferisce
   * @param idRivendita id della rivendita a cui l'opera si riferisce
   * @param tipo desctive il tipo della notifica, ovvero la motivazione della sua creazione
   * @param contenuto il contenuto della notifica(questo dobbiamo toglierlo credo raga)
   */
  public Notifica(int idUtente, int idAsta, int idRivendita, Tipo tipo, String contenuto) {
    this.id = Notifica.NO_ID;
    this.idUtente = idUtente;
    this.idAsta = idAsta;
    this.idRivendita = idRivendita;
    this.tipo = tipo;
    this.contenuto = contenuto;
  }

  /**
   * Costruttore vuoto della classe Notifica.
   */
  public Notifica() {
  }

  /**
   *
   * @return l'id della notifica
   */
  public int getId() {
    return id;
  }

  /**
   *
   * @param id il nuovo id della notifica
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   *
   * @return l'id del destinatario della notifica
   */
  public int getIdUtente() {
    return idUtente;
  }

  /**
   *
   * @param idUtente l'id del destinatario a cui si deve riferire l'opera
   */
  public void setIdUtente(int idUtente) {
    this.idUtente = idUtente;
  }

  /**
   *
   * @return l'id dell'opera a cui si riferisce
   */
  public int getIdAsta() {
    return idAsta;
  }

  /**
   *
   * @param idAsta l'id dell'asta a cui si deve riferire la notifica
   */
  public void setIdAsta(int idAsta) {
    this.idAsta = idAsta;
  }

  /**
   *
   * @return l'id della rivendita a cui si riferisce l'opera
   */
  public int getIdRivendita() {
    return idRivendita;
  }

  /**
   *
   * @param idRivendita l'id della rivendita a cui si riferisce la notifica
   */
  public void setIdRivendita(int idRivendita) {
    this.idRivendita = idRivendita;
  }

  /**
   *
   * @return il tipo della notifica
   */
  public Tipo getTipo() {
    return tipo;
  }

  /**
   *
   * @param tipo il tipo della notifica
   */
  public void setTipo(Tipo tipo) {
    this.tipo = tipo;
  }

  /**
   *
   * @return il contenuto dell'opera
   */
  public String getContenuto() {
    return contenuto;
  }

  /**
   *
   * @param contenuto il contenuto della notifica
   */
  public void setContenuto(String contenuto) {
    this.contenuto = contenuto;
  }

  private int id;
  private int idUtente;
  private int idAsta;
  private int idRivendita;
  private Tipo tipo;
  private String contenuto;

  /**
   * ENUM che rappresenta i possibili tipi di una notifica:
   * VITTORIA: la notifica avvisa il destinatario che ha vinto l'asta associata alla notifica
   * ANNULLAMENTO: la notifica avvisa che l'asta o la rivendita associata è stata annullata
   * SUPERATO: la notifica avvisa il destinatario che è stato superato nell'asta associata
   * TERMINATA: la notifica avvisa al destinatario che la vendita da lui creata è terminata
   */
  public enum Tipo{
    VITTORIA,ANNULLAMENTO,SUPERATO,TERMINATA
  }


  /**
   * costante per segnalare la mancanza di identificatore
   */
  public final static int NO_ID = -1;

}
