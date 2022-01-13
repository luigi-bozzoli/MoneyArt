package it.unisa.c02.moneyart.model.beans;

import java.sql.Blob;
import java.util.List;
import java.util.Objects;

/**
 * Questa classe rappresenta un'Opera.
 * Un'opera è caratterizzata da: id, nome, descrizione, stato, immagine,
 * prezzo, certificato, possessore.
 */
public class Opera {

  /**
   * Costruttore della classe Opera.
   *
   * @param nome        nome dell'opera
   * @param descrizione descrizione dell'opera
   * @param stato       stato dell'opera (all'asta, in vendita, in possesso)
   * @param immagine    immagine dell'opera
   * @param possessore  l'utente che possiede l'opera
   * @param artista     l'artista (utente creatore dell'opera)
   * @param certificato identificativo del certificato dell'opera
   */
  public Opera(String nome, String descrizione, Stato stato,
               Blob immagine, Utente possessore, Utente artista,
               String certificato) {

    this.nome = nome;
    this.descrizione = descrizione;
    this.stato = stato;
    this.immagine = immagine;
    this.possessore = possessore;
    this.artista = artista;
    this.certificato = certificato;
  }

  /**
   * Costruttore vuoto della classe Notifica.
   */
  public Opera() {
  }

  /**
   * Restituisce l'id dell'opera.
   *
   * @return identificativo dell'opera
   */
  public Integer getId() {
    return id;
  }

  /**
   * Imposta l'id dell'opera.
   *
   * @param id identificativo dell'opera
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Restituisce nome dell'opera.
   *
   * @return nome dell'opera
   */
  public String getNome() {
    return nome;
  }

  /**
   * Imposta nome dell'opera.
   *
   * @param nome nome dell'opera
   */
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Restituisce la descrizione dell'opera.
   *
   * @return descrizione dell'opera
   */
  public String getDescrizione() {
    return descrizione;
  }

  /**
   * Imposta la descrizione dell'opera.
   *
   * @param descrizione descrizione dell'opera
   */
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   * Restituisce lo stato dell'opera (all'asta, in vendita, in possesso).
   *
   * @return stato dell'opera
   */
  public Stato getStato() {
    return stato;
  }

  /**
   * Imposta lo stato dell'opera.
   *
   * @param stato stato dell'opera
   */
  public void setStato(Stato stato) {
    this.stato = stato;
  }

  /**
   * Restituisce l'immagine dell'opera.
   *
   * @return immagine dell'opera
   */
  public Blob getImmagine() {
    return immagine;
  }

  /**
   * Imposta l'immagine dell'opera.
   *
   * @param immagine immagine dell'opera
   */
  public void setImmagine(Blob immagine) {
    this.immagine = immagine;
  }

  /**
   * Restituisce l'utente possessore dell'opera.
   *
   * @return l'utente possessore dell'opera
   */
  public Utente getPossessore() {
    return possessore;
  }

  /**
   * Imposta l'utente possessore dell'opera.
   *
   * @param possessore l'utente possessore dell'opera
   */
  public void setPossessore(Utente possessore) {
    this.possessore = possessore;
  }

  /**
   * Restituisce l'utente creatore dell'opera.
   *
   * @return l'utente creatore dell'opera
   */
  public Utente getArtista() {
    return artista;
  }

  /**
   * Imposta l'utente creatore dell'opera.
   *
   * @param artista l'utente creatore dell'opera.
   */
  public void setArtista(Utente artista) {
    this.artista = artista;
  }

  /**
   * Restituisce l'identificativo del certificato dell'opera.
   *
   * @return identificativo del certificato dell'opera
   */
  public String getCertificato() {
    return certificato;
  }

  /**
   * Imposta l'identificativo del certificato dell'opera.
   *
   * @param certificato identificativo del certificato dell'opera
   */
  public void setCertificato(String certificato) {
    this.certificato = certificato;
  }

  /**
   * Restituisce una lista di tutte le aste relative all'opera.
   *
   * @return una lista di aste relativa all'opera
   */
  public List<Asta> getAste() {
    return aste;
  }

  /**
   * setta la lista di tutte le aste relative all'opera.
   *
   * @param aste la lista di tutte le aste relative all'opera
   */
  public void setAste(List<Asta> aste) {
    this.aste = aste;
  }

  /**
   * Restituisce una lista di tutte le rivendite relative all'opera.
   *
   * @return una lista di rivendite relativa all'opera
   */
  public List<Rivendita> getRivendite() {
    return rivendite;
  }

  /**
   * setta la lista di tutte le rivendite relative all'opera.
   *
   * @param rivendite la lista di tutte le rivendite relative all'opera
   */
  public void setRivendite(List<Rivendita> rivendite) {
    this.rivendite = rivendite;
  }

  /**
   * Restituisce la rappresentazione sotto forma di stringa di un'asta.
   *
   * @return la stringa che rappresenta l'asta
   */
  public String toString() {
    return " id: " + this.id + " id utente: " + possessore.getId() + " Artista: " + artista.getId()
        + " Nome: " + this.nome + " Descrizione: " + this.descrizione + " Stato: " + this.stato;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Opera)) {
      return false;
    }
    Opera opera = (Opera) o;
    return Objects.equals(getId(), opera.getId()) &&
        Objects.equals(getNome(), opera.getNome()) &&
        Objects.equals(getDescrizione(), opera.getDescrizione()) &&
        getStato() == opera.getStato() &&
        Objects.equals(getImmagine(), opera.getImmagine()) &&
        Objects.equals(getPossessore(), opera.getPossessore()) &&
        Objects.equals(getArtista(), opera.getArtista()) &&
        Objects.equals(getCertificato(), opera.getCertificato()) &&
        Objects.equals(getAste(), opera.getAste()) &&
        Objects.equals(getRivendite(), opera.getRivendite());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getNome(), getDescrizione(), getStato(), getImmagine(),
        getPossessore(), getArtista(), getCertificato(), getAste(), getRivendite());
  }

  private Integer id;
  private String nome;
  private String descrizione;
  private Stato stato;
  private Blob immagine;
  private Utente possessore;
  private Utente artista;
  private String certificato;

  private List<Asta> aste;
  private List<Rivendita> rivendite;

  /**
   * ENUM che rappresenta i possibili stati dell'opera.
   * ALL_ASTA: l'opera è all'asta
   * IN_VENDITA: l'opera, precedentemente vinta all'asta, è messa in vendita
   * IN_POSSESSO: l'opera è in possesso di un utente (non si trova né all'asta né in vendita)
   * PREVENDITA: l'opera non è in vendita o all'asta e non è ancora mai stata venduta
   */
  public enum Stato {
    ALL_ASTA, IN_VENDITA, IN_POSSESSO, PREVENDITA
  }

}