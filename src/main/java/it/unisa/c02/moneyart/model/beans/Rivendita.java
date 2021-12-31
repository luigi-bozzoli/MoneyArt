package it.unisa.c02.moneyart.model.beans;


/**
 * Questa classe rappresenta una Rivendita.
 * Una segnalazione è caratterizzata da: id (autogenerato dal DB),
 * idOpera (identificativo dell'opera che è stata rivenduta),
 * stato e prezzo.
 */
public class Rivendita {

    /**
     * Costruttore dell classe Rivendita.
     *
     * @param idOpera identificativo dell'asta a cui l'utente ha partecipato
     * @param stato identificativo dell'asta a cui l'utente ha partecipato
     * @param prezzo identificativo dell'utente che ha partecipato all'asta
     */
    public Rivendita(int idOpera, Stato stato, double prezzo) {
        this.id = Rivendita.NO_ID;
        this.idOpera = idOpera;
        this.stato = stato;
        this.prezzo = prezzo;
    }

    /**
     * Restituisce l'identificativo della rivendita.
     *
     * @return identificativo della rivendita
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'identificativo della rivendita.
     *
     * @param id identificativo della rivendita
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'identificativo dell'asta a cui l'utente ha partecipato.
     *
     * @return identificativo dell'asta a cui l'utente ha partecipato
     */
    public int getIdOpera() {
        return idOpera;
    }

    /**
     * Imposta l'identificativo dell'opera rivenduta.
     *
     * @param idOpera identificativo dell'opera rivenduta
     */
    public void setIdOpera(int idOpera) { this.idOpera = idOpera;}

    /**
     * Restituisce lo stato della rivendita.
     *
     * @return stato della rivendita
     */
    public Stato getStato() {
        return stato;
    }

    /**
     * Imposta lo stato della rivendita
     *
     * @param stato stato della rivendita
     */
    public void setStato(Stato stato) {
        this.stato = stato;
    }

    /**
     * Restituisce il valore del prezzo a cui l'opera è stata rivenduta.
     *
     * @return il valore del prezzo a cui l'opera è stata rivenduta
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta il prezzo della rivendita.
     *
     * @param prezzo prezzo a cui l'opera è stata rivenduta
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public static final int NO_ID = -1;

    private int id;
    private int idOpera;
    private Stato stato;
    private double prezzo;

    /**
     * ENUM che rappresenta i possibili stati della rivendita.
     * IN_CORSO: la rivendita è in corso
     * TERMINATA: la rivendita è terminata
     */
    public enum Stato {
        IN_CORSO, TERMINATA
    }
}
