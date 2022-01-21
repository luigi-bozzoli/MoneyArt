package it.unisa.c02.moneyart.model.beans;


import java.util.Objects;

/**
 * Questa classe rappresenta una notifica legata ad una vendita.
 * è caratterizzata da:
 * un id,
 * l'id dell'utente che riceve la notifica,
 * l'id dell'asta a cui si riferisce,
 * l'id della vendita diretta a cui si riferisce,
 * un tipo che descrive il motivo della notifica,
 * e il contenuto della notifica
 *
 * @invariant (idAsta = = null and idRivendita ! = null) or (idAsta != null and idRivendita == null)
 */
public class Notifica {

  /**
   * Costruttore della classe Notifica.
   *
   * @param idUtente    il destinatario
   * @param idAsta      l'asta a cui l'opera si riferisce
   * @param idRivendita la rivendita a cui l'opera si riferisce
   * @param tipo        descrive il tipo della notifica,
   *                    ovvero la motivazione della sua creazione
   * @param contenuto   il contenuto della notifica
   *                    (questo dobbiamo toglierlo credo raga)
   * @param letta       inidica se la notifica è stata letta
   */
  public Notifica(Utente idUtente, Asta idAsta, Rivendita idRivendita, Tipo tipo,
                  String contenuto, boolean letta) {
    this.utente = idUtente;
    this.asta = idAsta;
    this.rivendita = idRivendita;
    this.tipo = tipo;
    this.contenuto = contenuto;
    this.letta = letta;
  }

  /**
   * Costruttore vuoto della classe Notifica.
   */
  public Notifica() {
  }

  /**
   * Restituisce l'id della notifica.
   *
   * @return l'id della notifica
   */
  public Integer getId() {
    return id;
  }

  /**
   * Modifica l'id della notifica.
   *
   * @param id il nuovo id della notifica
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Restituisce il destinatario.
   *
   * @return il destinatario della notifica
   */
  public Utente getUtente() {
    return utente;
  }

  /**
   * Modifica il destinatario.
   *
   * @param utente il destinatario a cui si deve riferire l'opera
   */
  public void setUtente(Utente utente) {
    this.utente = utente;
  }

  /**
   * Restituisce l'asta associata alla notifica.
   *
   * @return l'asta a cui si riferisce la notifica
   */
  public Asta getAsta() {
    return asta;
  }

  /**
   * Modifica l'asta associata alla notifica.
   *
   * @param asta l'asta a cui si deve riferire la notifica
   */
  public void setAsta(Asta asta) {
    this.asta = asta;
  }

  /**
   * Restituisce la rivendita associata alla notifica.
   *
   * @return la rivendita a cui si riferisce l'opera
   */
  public Rivendita getRivendita() {
    return rivendita;
  }

  /**
   * Modifica la rivendita associata alla notifica.
   *
   * @param rivendita la rivendita a cui si riferisce la notifica
   */
  public void setRivendita(Rivendita rivendita) {
    this.rivendita = rivendita;
  }

  /**
   * Restituisce il tipo della notifica.
   *
   * @return il tipo della notifica
   */
  public Tipo getTipo() {
    return tipo;
  }

  /**
   * Modifica il tipo di notifica.
   *
   * @param tipo il tipo della notifica
   */
  public void setTipo(Tipo tipo) {
    this.tipo = tipo;
  }

  /**
   * Restituice il contenuto della notifica.
   *
   * @return il contenuto della notifica
   */
  public String getContenuto() {
    return contenuto;
  }

  /**
   * Modifica il contenuto della notifica.
   *
   * @param contenuto il contenuto della notifica
   */
  public void setContenuto(String contenuto) {
    this.contenuto = contenuto;
  }

  /**
   * Verifica se la notifica è stata letta.
   *
   * @return un valore che identifica se la notifica sia stata letta o meno
   */
  public Boolean isLetta() {
    return letta;
  }

  /**
   * Modifica lo stato di lettura della notifica.
   *
   * @param letta il nuovo stato della lettura
   */
  public void setLetta(Boolean letta) {
    this.letta = letta;
  }


  /**
   * Restituisce la rappresentazione sotto forma di stringa di una notifica.
   *
   * @return la stringa che rappresenta notifica
   */
  @Override
  public String toString() {
    return "Notifica{"
        +
        "id=" + id
        +
        ", utente=" + utente
        +
        ", asta=" + asta
        +
        ", rivendita=" + rivendita
        +
        ", tipo=" + tipo
        +
        ", contenuto='" + contenuto + '\''
        +
        ", letta=" + letta
        +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    Notifica notifica = (Notifica) o;
    BooleanComparator booleanComparator =
        new BooleanComparator(Objects.equals(getId(), notifica.getId()));
    return booleanComparator
        .and(Objects.equals(getUtente(), notifica.getUtente()))
        .and(Objects.equals(getAsta(), notifica.getAsta()))
        .and(Objects.equals(getRivendita(), notifica.getRivendita()))
        .and(Objects.equals(getTipo(), notifica.getTipo()))
        .and(Objects.equals(getContenuto(), notifica.getContenuto()))
        .and(Objects.equals(letta, notifica.letta)).getValue();

  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getUtente(), getAsta(), getRivendita(), getTipo(), getContenuto(),
        letta);
  }

  private Integer id;
  private Utente utente;
  private Asta asta;
  private Rivendita rivendita;
  private Tipo tipo;
  private String contenuto;
  private Boolean letta;

  /**
   * ENUM che rappresenta i possibili tipi di una notifica.
   * VITTORIA: la notifica avvisa il destinatario che ha vinto l'asta associata alla notifica
   * ANNULLAMENTO: la notifica avvisa che l'asta o la rivendita associata è stata annullata
   * SUPERATO: la notifica avvisa il destinatario che è stato superato nell'asta associata
   * TERMINATA: la notifica avvisa al destinatario che la vendita da lui creata è terminata
   */
  public enum Tipo {
    VITTORIA, ANNULLAMENTO, SUPERATO, TERMINATA
  }


  /**
   * costante per segnalare la mancanza di identificatore.
   */
  public static final int NO_ID = -1;

}
