package it.unisa.c02.moneyart.model.beans;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe rappresenta un Utente.
 * Un utente ha id, nome, cognome, foto profilo, email, username e password.
 * Inoltre ha l'attributo seguito per identificare l'artista che segue e
 * l'attributo wallet per identificare il portafoglio virtuale.
 */
public class Utente {

    /**
     * Costruttore della classe utente.
     *
     * @param id codice identificativo dell'utente
     * @param nome nome dell'utente
     * @param cognome cognome dell'utente
     * @param fotoProfilo foto profilo dell'utente
     * @param email email dell'utente
     * @param username username dell'utente
     * @param password password dell'utente
     * @param seguito id dell'artista seguito
     * @param wallet wallet dell'utente
     */
    public Utente(int id, String nome, String cognome, Blob fotoProfilo,
                  String email, String username, String password, int seguito, Wallet wallet) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.fotoProfilo = fotoProfilo;
        this.email = email;
        this.username = username;
        this.password = password;
        this.seguito = seguito;
        this.wallet = wallet;
    }

    /**
     * Restituisce id dell'utente.
     *
     * @return id dell'utente
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta id dell'utente.
     *
     * @param id identificativo dell'utente
     */
    public void setId(int id) {
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
     * @return id dell'utente seguito
     */
    public int getSeguito() {
        return seguito;
    }

    /**
     * Imposta l'identificativo dell'utente seguito.
     *
     * @param seguito identificativo dell'utente seguito
     */
    public void setSeguito(int seguito) {
        this.seguito = seguito;
    }

    /**
     * Restituisce wallet dell'utente.
     *
     * @return wallet dell'utente
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Imposta wallet dell'utente.
     *
     * @param wallet wallet dell'utente
     */
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    /**
     * Restituisce le opere in possesso dell'utente.
     *
     * @return lista di opere possedute
     */
    public List<Opera> operePossedute() {
        //TODO
        return null;
    }

    private int id;
    private String nome;
    private String cognome;
    private Blob fotoProfilo;
    private String email;
    private String username;
    private String password;
    private int seguito;
    private Wallet wallet;

}