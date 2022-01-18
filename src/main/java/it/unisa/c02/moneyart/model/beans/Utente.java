package it.unisa.c02.moneyart.model.beans;

import java.sql.Blob;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Questa classe rappresenta un Utente.
 * Un utente è caratterizzato da: id, nome, cognome, foto profilo, email, username,
 * password, seguito (identificativo dell'utente seguito) e saldo.
 */
public class Utente {

  /**
   * Costruttore della classe Utente.
   *
   * @param nome        nome dell'utente
   * @param cognome     cognome dell'utente
   * @param fotoProfilo foto profilo dell'utente
   * @param email       email dell'utente
   * @param username    username dell'utente
   * @param seguito     id dell'artista seguito
   * @param password    password dell'utente
   * @param saldo       saldo dell'utente
   */
  public Utente(String nome, String cognome, Blob fotoProfilo,
                String email, String username, Utente seguito, byte[] password, Double saldo) {
    this.id = null;
    this.nome = nome;
    this.cognome = cognome;
    this.fotoProfilo = fotoProfilo;
    this.email = email;
    this.username = username;
    this.password = password;
    this.seguito = seguito;
    this.saldo = saldo;
    this.numFollowers = 0;
  }

  /**
   * Costruttore vuoto della classe Utente.
   **/
  public Utente() {
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
  public byte[] getPassword() {
    return password;
  }

  /**
   * Imposta la password dell'utente.
   *
   * @param password password cifrata in SHA256
   */
  public void setPassword(byte[] password) {
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
  public Double getSaldo() {
    return saldo;
  }

  /**
   * Imposta saldo dell'utente.
   *
   * @param saldo wallet dell'utente
   */
  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

  /**
   * Restituisce le opere create dall'utente (artista).
   */
  public List<Opera> getOpereCreate() {
    return opereCreate;
  }

  /**
   * Restituisce le opere in possesso dell'utente (vinte/acquistate).
   */
  public List<Opera> getOpereInPossesso() {
    return opereInPossesso;
  }

  /**
   * Restituisce le notifiche dell'utente.
   */
  public List<Notifica> getNotifiche() {
    return notifiche;
  }

  /**
   * Restituisce le partecipazioni dell'utente.
   */
  public List<Partecipazione> getPartecipazioni() {
    return partecipazioni;
  }

  /**
   * Imposta le opere create dall'utente (artista).
   */
  public void setOpereCreate(List<Opera> opereCreate) {
    this.opereCreate = opereCreate;
  }

  /**
   * Imposta le opere in possesso dell'utente (vinte/acquistate).
   */
  public void setOpereInPossesso(List<Opera> opereInPossesso) {
    this.opereInPossesso = opereInPossesso;
  }

  /**
   * Imposta le notifche dell'utente.
   */
  public void setNotifiche(List<Notifica> notifiche) {
    this.notifiche = notifiche;
  }

  /**
   * Imposta le partecipazioni dell'utente.
   */
  public void setPartecipazioni(List<Partecipazione> partecipazioni) {
    this.partecipazioni = partecipazioni;
  }

  /**
   * Restituisce il numero di followers dell'utente.
   *
   * @return il numero di followers dell'utente
   */
  public int getnFollowers() {
    return numFollowers;
  }

  /**
   * Modifica il numero di followers dell'utente.
   *
   * @param numFollowers il numero di followers dell'utente
   */
  public void setnFollowers(int numFollowers) {
    this.numFollowers = numFollowers;
  }

  /**
   * Restituisce la rappresentazione sotto forma di stringa di un utente.
   *
   * @return la stringa che rappresenta utente
   */
  @Override
  public String toString() {
    return "Utente{"
        +
        "id=" + id
        +
        ", nome='" + nome + '\''
        +
        ", cognome='" + cognome + '\''
        +
        ", fotoProfilo=" + fotoProfilo
        +
        ", email='" + email + '\''
        +
        ", username='" + username + '\''
        +
        ", password='" + password + '\''
        +
        ", seguito=" + seguito
        +
        ", saldo=" + saldo
        +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Utente)) {
      return false;
    }
    Utente utente = (Utente) o;
    return getnFollowers() == utente.getnFollowers()
      && Objects.equals(getId(), utente.getId())
      && Objects.equals(getNome(), utente.getNome())
      && Objects.equals(getCognome(), utente.getCognome())
      && Objects.equals(getFotoProfilo(), utente.getFotoProfilo())
      && Objects.equals(getEmail(), utente.getEmail())
      && Objects.equals(getUsername(), utente.getUsername())
      && Arrays.equals(getPassword(), utente.getPassword())
      && Objects.equals(getSeguito(), utente.getSeguito())
      && Objects.equals(getSaldo(), utente.getSaldo())
      && Objects.equals(getOpereCreate(), utente.getOpereCreate())
      && Objects.equals(getOpereInPossesso(), utente.getOpereInPossesso())
      && Objects.equals(getNotifiche(), utente.getNotifiche())
      && Objects.equals(getPartecipazioni(), utente.getPartecipazioni());
  }

  @Override
  public int hashCode() {
    int result =
        Objects.hash(getId(), getNome(), getCognome(), getFotoProfilo(), getEmail(), getUsername(),
            getSeguito(), getSaldo(), getnFollowers(), getOpereCreate(),
            getOpereInPossesso(), getNotifiche(), getPartecipazioni());
    result = 31 * result + Arrays.hashCode(getPassword());
    return result;
  }

  private Integer id;
  private String nome;
  private String cognome;
  private Blob fotoProfilo;
  private String email;
  private String username;
  private byte[] password;
  private Utente seguito;
  private Double saldo;
  private int numFollowers;

  /* Liste utili */
  private List<Opera> opereCreate;
  private List<Opera> opereInPossesso;
  private List<Notifica> notifiche;
  private List<Partecipazione> partecipazioni;
}