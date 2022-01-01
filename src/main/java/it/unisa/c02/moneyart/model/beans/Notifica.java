package it.unisa.c02.moneyart.model.beans;


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
   * @param idUtente identificativo del destinatario
   * @param idAsta identificativo dell'asta a cui l'opera si riferisce
   * @param idRivendita identificativo della rivendita a cui l'opera si riferisce
   * @param tipo descrive il tipo della notifica,
   *             ovvero la motivazione della sua creazione
   * @param contenuto il contenuto della notifica
   *                  (questo dobbiamo toglierlo credo raga)
   * @param letta inidica se la notifica è stata letta
   */
  public Notifica(Integer idUtente, Integer idAsta, Integer idRivendita, Tipo tipo,
                  String contenuto, boolean letta) {
    this.id = Notifica.NO_ID;
    this.idUtente = idUtente;
    this.idAsta = idAsta;
    this.idRivendita = idRivendita;
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
   * Restituisce l'id del destinatario.
   *
   * @return l'id del destinatario della notifica
   */
  public Integer getIdUtente() {
    return idUtente;
  }

  /**
   * Modifica il destinatario.
   *
   * @param idUtente l'id del destinatario a cui si deve riferire l'opera
   */
  public void setIdUtente(Integer idUtente) {
    this.idUtente = idUtente;
  }

  /**
   * Restituisce l'id dell'asta associata alla notifica.
   *
   * @return l'id dell'opera a cui si riferisce
   */
  public Integer getIdAsta() {
    return idAsta;
  }

  /**
   * Modifica l'asta associata alla notifica.
   *
   * @param idAsta l'id dell'asta a cui si deve riferire la notifica
   */
  public void setIdAsta(Integer idAsta) {
    this.idAsta = idAsta;
  }

  /**
   * Restituisce l'id della rivendita associata alla notifica.
   *
   * @return l'id della rivendita a cui si riferisce l'opera
   */
  public Integer getIdRivendita() {
    return idRivendita;
  }

  /**
   * Modifica la rivendita associata alla notifica.
   *
   * @param idRivendita l'id della rivendita a cui si riferisce la notifica
   */
  public void setIdRivendita(Integer idRivendita) {
    this.idRivendita = idRivendita;
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
   * Restituisce la rappresentazione sottoforma di stringa di una notifica.
   *
   * @return la stringa che rappresenta notifica
   */
  @Override
  public String toString() {
    return "Notifica{"
        +
        "id=" + id
        +
        ", idUtente=" + idUtente
        +
        ", idAsta=" + idAsta
        +
        ", idRivendita=" + idRivendita
        +
        ", tipo=" + tipo
        +
        ", contenuto='" + contenuto + '\''
        +
        ", letta=" + letta
        +
        '}';
  }

  private Integer id;
  private Integer idUtente;
  private Integer idAsta;
  private Integer idRivendita;
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
