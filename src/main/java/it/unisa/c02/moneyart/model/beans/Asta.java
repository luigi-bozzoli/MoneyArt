package it.unisa.c02.moneyart.model.beans;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Questa classe rappresenta un'asta.
 * Un'asta è caratterizzata da: id, dataInizio, dataFine, stato.
 */
public class Asta {
  /**
   * Costruttore della classe Asta.
   *
   * @param opera      riferimento all'identificativo di opera
   * @param dataInizio data di inizio dell'asta
   * @param dataFine   data di fine dell'asta
   * @param stato      stato corrente dell'asta ('in corso' , 'terminata' , 'eliminata', 'creata')
   */
  public Asta(Opera opera, Date dataInizio, Date dataFine, Stato stato) {
    this.opera = opera;
    this.dataInizio = dataInizio;
    this.dataFine = dataFine;
    this.stato = stato;
  }

  /**
   * Costruttore vuoto.
   */
  public Asta() {
  }

  /**
   * Restituisce l'identificativo dell'asta.
   *
   * @return id
   */
  public Integer getId() {
    return id;
  }

  /**
   * Assegna identificativo all'asta.
   *
   * @param id nuovo identificativo di asta
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Restituisce l'opera messa all'asta.
   *
   * @return l'opera messa all'asta
   */
  public Opera getOpera() {
    return opera;
  }

  /**
   * Assegna l'opera da mettere all'asta.
   *
   * @param opera l'opera da mettere all'asta
   */
  public void setOpera(Opera opera) {
    this.opera = opera;
  }

  /**
   * Restituisce data inizio dell'asta.
   *
   * @return dataInizio
   */
  public Date getDataInizio() {
    return dataInizio;
  }

  /**
   * Assegna data inizio all'asta.
   *
   * @param dataInizio nuova data inizio
   */
  public void setDataInizio(Date dataInizio) {
    this.dataInizio = dataInizio;
  }

  /**
   * Restituisce data fine dell'asta.
   *
   * @return dataFine
   */
  public Date getDataFine() {
    return dataFine;
  }

  /**
   * Assegna data fine all'asta.
   *
   * @param dataFine nuova data di fine
   */
  public void setDataFine(Date dataFine) {
    this.dataFine = dataFine;
  }

  /**
   * Restituisce data fine dell'asta.
   *
   * @return dataFine
   */
  public Stato getStato() {
    return stato;
  }

  /**
   * Assegna data fine all'asta.
   *
   * @param stato nuovo stato
   */
  public void setStato(Stato stato) {
    this.stato = stato;
  }

  /**
   * Restituisce una collezione di segnalazioni relative all'asta.
   *
   * @return segnalazioni
   */
  public List<Segnalazione> getSegnalazioni() {
    return segnalazioni;
  }

  /**
   * Assegna una lista di segnalazioni all'asta.
   *
   * @param segnalazioni segnalazioni associate all'asta
   */
  public void setSegnalazioni(List<Segnalazione> segnalazioni) {
    this.segnalazioni = segnalazioni;
  }

  /**
   * Restituisce una collezione di notifiche relative all'asta.
   *
   * @return notifiche
   */
  public List<Notifica> getNotifiche() {
    return notifiche;
  }

  /**
   * Assegna una lista di notifiche all'asta.
   *
   * @param notifiche notifiche associate all'asta
   */
  public void setNotifiche(List<Notifica> notifiche) {
    this.notifiche = notifiche;
  }

  /**
   * Restituisce una collezione di partecipazioni.
   *
   * @return partecipazioni dell'asta
   */
  public List<Partecipazione> getPartecipazioni() {
    return partecipazioni;
  }

  /**
   * Assegna una lista di partecipazioni all'asta.
   *
   * @param partecipazioni partecipazioni associate all'asta
   */
  public void setPartecipazioni(List<Partecipazione> partecipazioni) {
    this.partecipazioni = partecipazioni;
  }

  /**
   * Restituisce la rappresentazione sotto forma di stringa di un'asta.
   *
   * @return la stringa che rappresenta l'asta
   */
  public String toString() {
    return "id: " + this.id + " opera: " + this.opera.toString() + " Data inizio: " +
        this.dataInizio
        + " Data fine: " + this.dataFine + " Stato: " + this.stato;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    Asta asta = (Asta) o;
    BooleanComparator booleanComparator =
        new BooleanComparator(Objects.equals(getId(), asta.getId()));
    return booleanComparator
        .and(Objects.equals(getOpera(), asta.getOpera()))
        .and(Objects.equals(getDataInizio(), asta.getDataInizio()))
        .and(Objects.equals(getDataFine(), asta.getDataFine()))
        .and(Objects.equals(getStato(), asta.getStato())).getValue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getOpera(), getDataInizio(), getDataFine(), getStato(),
        getSegnalazioni(), getNotifiche(), getPartecipazioni());
  }

  /**
   * Variabili d'istanza.
   */
  private Integer id;
  private Opera opera;
  private Date dataInizio;
  private Date dataFine;
  private Stato stato;
  private List<Segnalazione> segnalazioni;
  private List<Notifica> notifiche;
  private List<Partecipazione> partecipazioni;

  /**
   * L'enumerazione seguente determina lo stato corrente di un'asta.
   * IN CORSO: L'asta è attualmente in svolgimento
   * TERMINATA: L'asta ha raggiunto il tempo di fine
   * ELIMINATA: L'asta è stata eliminata
   */
  public enum Stato {
    IN_CORSO, TERMINATA, ELIMINATA, CREATA
  }
}


