package it.unisa.c02.moneyart.model.beans;


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
     * @param idAsta identificativo dell'asta a cui l'utente ha partecipato
     * @param idUtente identificativo dell'utente che ha partecipato all'asta
     * @param offerta offerta dell'utente
     */
    public Partecipazione(Asta idAsta, Utente idUtente, double offerta) {
        this.id = Partecipazione.NO_ID;
        this.idAsta = idAsta;
        this.idUtente = idUtente;
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
    public Asta getIdAsta() {
        return idAsta;
    }

    /**
     * Imposta l'identificativo dell'asta a cui l'utente ha partecipato.
     *
     * @param idAsta identificativo dell'asta a cui l'utente ha partecipato
     */
    public void setIdAsta(Asta idAsta) {
        this.idAsta = idAsta;
    }

    /**
     * Restituisce l'identificativo dell'utente che ha partecipato all'asta.
     *
     * @return identificativo dell'utente che ha partecipato all'asta
     */
    public Utente getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta l'identificativo dell'utente che ha partecipato all'asta.
     *
     * @param idUtente identificativo dell'utente che ha partecipato all'asta
     */
    public void setIdUtente(Utente idUtente) {
        this.idUtente = idUtente;
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

    public static final int NO_ID = -1;

    private Integer id;
    private Asta idAsta;
    private Utente idUtente;
    private double offerta;

}
