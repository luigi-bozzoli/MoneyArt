package it.unisa.c02.moneyart.model.beans;

import java.sql.Blob;
import java.util.List;

/**
 * Questa classe rappresenta un Utente.
 * Un utente è caratterizzato da: id, nome, cognome, foto profilo, email, username,
 * password, seguito (identificativo dell'utente seguito) e saldo.
 */
public class Utente {

  /**
   * Costruttore della classe Utente.
   *
   * @param nome nome dell'utente
   * @param cognome cognome dell'utente
   * @param fotoProfilo foto profilo dell'utente
   * @param email email dell'utente
   * @param username username dell'utente
   * @param password password dell'utente
   * @param seguito id dell'artista seguito
   * @param saldo saldo dell'utente
   */
  public Utente(String nome, String cognome, Blob fotoProfilo,
                String email, String username, Utente seguito, String password, Float saldo) {
    this.id = Utente.NO_ID;
    this.nome = nome;
    this.cognome = cognome;
    this.fotoProfilo = fotoProfilo;
    this.email = email;
    this.username = username;
    this.password = password;
    this.seguito = seguito;
    this.saldo = saldo;
  }

  /**
   * Costruttore vuoto della classe Utente.
   **/
  public Utente(){
  }

  /**
   * Restituisce l'identificativo dell'utente.
   *
   * @return identificativo dell'utente
   */
  public Integer getId() {
    return id;
  }

  /**
   * Imposta l'identificativo dell'utente.
   *
   * @param id identificativo dell'utente
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Restituisce nome dell'utente.
   *
   * @return nome dell'utente
   */
  public String getNome() {
    return nome;
  }

  /**
   * Imposta il nome dell'utente.
   *
   * @param nome nome dell'utente
   */
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Restituisce il cognome dell'utente.
   *
   * @return cognome dell'utente
   */
  public String getCognome() {
    return cognome;
  }

  /**
   * Imposta il cognome dell'utente.
   *
   * @param cognome cognome dell'utente
   */
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   * Restituisce la foto profilo dell'utente.
   *
   * @return foto profilo dell'utente
   */
  public Blob getFotoProfilo() {
    return fotoProfilo;
  }

  /**
   * Imposta la foto profilo dell'utente.
   *
   * @param fotoProfilo foto profilo dell'utente
   */
  public void setFotoProfilo(Blob fotoProfilo) {
    this.fotoProfilo = fotoProfilo;
  }

  /**
   * Restituisce l'email dell'utente.
   *
   * @return email dell'utente
   */
  public String getEmail() {
    return email;
  }

  /**
   * Imposta l'email dell'utente.
   *
   * @param email email dell'utente
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Restituisce l'username dell'utente.
   *
   * @return username dell'utente
   */
  public String getUsername() {
    return username;
  }

  /**
   * Imposta l'username dell'utente.
   *
   * @param username username dell'utente
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Restituisce la password dell'utente cifrata in SHA256.
   *
   * @return password cifrata dell'utente
   */
  public String getPassword() {
    return password;
  }

  /**
   * Imposta la password dell'utente.
   *
   * @param password password cifrata in SHA256
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Restituisce l'identificativo dell'utente seguito.
   *
   * @return identificativo dell'utente seguito
   */
  public Utente getSeguito() {
    return seguito;
  }

  /**
   * Imposta l'identificativo dell'utente seguito.
   *
   * @param seguito identificativo dell'utente seguito
   */
  public void setSeguito(Utente seguito) {
    this.seguito = seguito;
  }

  /**
   * Restituisce saldo dell'utente.
   *
   * @return saldo dell'utente
   */
  public Float getSaldo() {
    return saldo;
  }

  /**
   * Imposta saldo dell'utente.
   *
   * @param saldo wallet dell'utente
   */
  public void setSaldo(Float saldo) {
    this.saldo = saldo;
  }

  public static final int NO_ID = -1;

  private Integer id;
  private String nome;
  private String cognome;
  private Blob fotoProfilo;
  private String email;
  private String username;
  private String password;
  private Utente seguito;
  private Float saldo;

  /* Liste utili */
  private List<Opera> opereCreate;
  private List<Opera> opereInPossesso;
  private List<Notifica> notifiche;
  private List<Partecipazione> partecipazioni;
}