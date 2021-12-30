package it.unisa.c02.moneyart.model.beans;

import java.sql.Blob;

/**
 * Questa classe rappresenta un'Opera.
 * Un'opera è caratterizzata da: id, nome, descrizione, stato, immagine,
 * prezzo, certificato, possessore.
 */
public class Opera {

  /**
   * Costruttore della classe Opera.
   *
   * @param nome nome dell'opera
   * @param descrizione descrizione dell'opera
   * @param stato stato dell'opera (all'asta, in vendita, in possesso)
   * @param immagine immagine dell'opera
   * @param possessore id dell'utente che possiede l'opera
   * @param artista id dell'artista (utente creatore dell'opera)
   */
  public Opera(String nome, String descrizione, Stato stato,
               Blob immagine, Integer possessore, Integer artista) {

    this.id = Opera.NO_ID;
    this.nome = nome;
    this.descrizione = descrizione;
    this.stato = stato;
    this.immagine = immagine;
    this.possessore = possessore;
    this.artista = artista;
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
   * Restituisce l'identificativo dell'utente possessore dell'opera.
   * Questo identificativo coinciderà con l'id dell'artista qualora
   * l'opera non è mai stata messa in asta oppure quando l'opera, se
   * è stata messa all'asta, non riceve nessuna offerta.
   *
   * @return identificativo dell'utente possessore dell'opera
   */
  public Integer getPossessore() {
    return possessore;
  }

  /**
   * Imposta l'identificativo dell'utente possessore dell'opera.
   *
   * @param possessore identificativo dell'utente possessore dell'opera
   */
  public void setPossessore(Integer possessore) {
    this.possessore = possessore;
  }

  /**
   * Restituisce l'identificativo dell'utente creatore dell'opera.
   *
   * @return identificativo dell'utente creatore dell'opera
   */
  public Integer getArtista() {
    return artista;
  }

  /**
   * Imposta l'identificativo dell'utente creatore dell'opera.
   *
   * @param artista identificativo dell'utente creatore dell'opera.
   */
  public void setArtista(Integer artista) {
    this.artista = artista;
  }

  public static final int NO_ID = -1;

  private Integer id;
  private String nome;
  private String descrizione;
  private Stato stato;
  private Blob immagine;
  private Integer possessore;
  private Integer artista;

  /**
   * ENUM che rappresenta i possibili stati dell'opera.
   * ALL_ASTA: l'opera è all'asta
   * IN_VENDITA: l'opera, precedentemente vinta all'asta, è messa in vendita
   * IN_POSSESSO: l'opera è in possesso di un utente (non si trova né all'asta né in vendita)
   */
  public enum Stato {
    ALL_ASTA, IN_VENDITA, IN_POSSESSO
  }

}