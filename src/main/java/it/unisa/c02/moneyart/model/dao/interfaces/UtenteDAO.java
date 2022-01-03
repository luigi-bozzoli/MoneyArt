package it.unisa.c02.moneyart.model.dao.interfaces;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;

import java.util.List;

/**
 * Questa classe rappresenta il DAO di un Utente.
 */
public interface UtenteDAO extends GenericDao<Utente> {
//signature metodi aggiuntivi.....

    /**
     * Restituisce l'utente in base all'username
     *
     * @param username l'username dell'utente
     * @return l'utente con quell'username
     */
    public Utente doRetrieveByUsername(String username);

    /**
     * Restituisce tutti gli utenti che seguono utente
     *
     * @param utente l'utente di cui vogliamo conoscere i follower
     * @return i follower di quell'utente
     */
    public List<Utente> getFollowers (Utente utente);

}
