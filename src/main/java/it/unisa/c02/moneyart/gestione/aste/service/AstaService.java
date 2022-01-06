package it.unisa.c02.moneyart.gestione.aste.service;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Utente;

import java.sql.Date;

public interface AstaService {

//_______________________________Creazione ed eliminazione asta________________________________

    //vanno aggiunti i vincoli come documentazione qui.
    /**
     * Verifica la correttezza delle date inserite durante la fase di creazione dell'asta
     * @param startDate data di inizio per l'asta
     * @param endDate data di fine per l'asta
     * @return vero o falso
     */
     boolean checkDate(Date startDate, Date endDate);

    /**
     * Aggiunge una nuova asta
     * @param asta l'asta da aggiungere
     * @return vero se l'aggiunta è andata a buon fine, falso altrimenti
     */
    boolean addAsta (Asta asta);

    /**
     * Rimuove un'asta
     * @param asta l'asta da rimuovere
     * @return vero se la rimozione è andata a buon fine, falso altrimenti
     */
    boolean removeAsta (Asta asta);



//_______________________________Gestione e terminazione asta________________________________

    /**
     * Verifica se l'asta è terminata per lo scadere del tempo
     * @param asta l'asta da verificare
     * @return vero o falso
     */
    boolean isTerminated (Asta asta);  //  <<-- l'asta termina quando o finisce il tempo assegnatogli oppure viene vinta

    /**
     * Verifica se l'asta è terminata perchè vinta da qualcuno
     * @param asta l'asta da verificare
     * @return l'utente che ha vinto quell'asta
     */
    Utente winAsta (Asta asta);


}
