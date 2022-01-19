package it.unisa.c02.moneyart.model.beans;


import java.util.Objects;

/**
 * Questa classe rappresenta una Partecipazione.
 * Una segnalazione è caratterizzata da: id (autogenerato dal DB),
 * idAsta (identificativo dell'asta a cui l'utente ha partecipato),
 * idUtente (identificativo dell'utente che ha partecipato all'asta) e offerta.
 */
public class Partecipazione {

  /**
   * Costruttore della classe Partecipazione.
   *
   * @param asta    identificativo dell'asta a cui l'utente ha partecipato
   * @param utente  identificativo dell'utente che ha partecipato all'asta
   * @param offerta offerta dell'utente
   */
  public Partecipazione(Asta asta, Utente utente, double offerta) {
    this.id = null;
    this.asta = asta;
    this.utente = utente;
    this.offerta = offerta;
  }

  /**
   * Costruttore vuoto della classe Partecipazione.
   **/
  public Partecipazione() {
  }

  /**
   * Restituisce l'identificativo della partecipazione.
   *
   * @return identificativo della partecipazione
   */
  public Integer getId() {
    return id;
  }

  /**
   * Imposta l'identificativo della partecipazione.
   *
   * @param id identificativo della partecipazione
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Restituisce l'identificativo dell'asta a cui l'utente ha partecipato.
   *
   * @return identificativo dell'asta a cui l'utente ha partecipato
   */
  public Asta getAsta() {
    return asta;
  }

  /**
   * Imposta l'identificativo dell'asta a cui l'utente ha partecipato.
   *
   * @param asta identificativo dell'asta a cui l'utente ha partecipato
   */
  public void setAsta(Asta asta) {
    this.asta = asta;
  }

  /**
   * Restituisce l'identificativo dell'utente che ha partecipato all'asta.
   *
   * @return identificativo dell'utente che ha partecipato all'asta
   */
  public Utente getUtente() {
    return utente;
  }

  /**
   * Imposta l'identificativo dell'utente che ha partecipato all'asta.
   *
   * @param utente identificativo dell'utente che ha partecipato all'asta
   */
  public void setUtente(Utente utente) {
    this.utente = utente;
  }

  /**
   * Restituisce il valore dell'offerta dell'utente che partecipa all'asta.
   *
   * @return il valore dell'offerta dell'utente che partecipa all'asta
   */
  public double getOfferta() {
    return offerta;
  }

  /**
   * Imposta l'offerta dell'utente che partecipa all'asta.
   *
   * @param offerta offerta dell'utente che partecipa all'asta
   */
  public void setOfferta(double offerta) {
    this.offerta = offerta;
  }

  /**
   * Restituisce la rappresentazione sotto forma di stringa di una partecipazione.
   *
   * @return la stringa che rappresenta partecipazione
   */
  @Override
  public String toString() {
    return "Partecipazione{"
        +
        "id=" + id
        +
        ", asta=" + asta.getId()
        +
        ", utente=" + utente.getId()
        +
        ", offerta=" + offerta
        +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Partecipazione)) {
      return false;
    }
    Partecipazione that = (Partecipazione) o;
    return Double.compare(that.getOfferta(), getOfferta()) == 0
      && Objects.equals(getId(), that.getId())
      && Objects.equals(getAsta(), that.getAsta())
      && Objects.equals(getUtente(), that.getUtente());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getAsta(), getUtente(), getOfferta());
  }

  private Integer id;
  private Asta asta;
  private Utente utente;
  private double offerta;



}
