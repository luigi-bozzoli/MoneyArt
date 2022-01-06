package it.unisa.c02.moneyart.gestione.aste.service;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;

import java.sql.Date;

public class AstaServiceImpl implements AstaService{
    /**
     * Verifica la correttezza delle date inserite durante la fase di creazione dell'asta
     * @param startDate data di inizio per l'asta
     * @param endDate   data di fine per l'asta
     * @return vero o falso
     */
    @Override
    public boolean checkDate(Date startDate, Date endDate) {
        long  millis=System.currentTimeMillis();
        Date today = new Date(millis);

        if ( (!startDate.equals(today)) || startDate.before(today) )
            return false;
        if ( (endDate.before(today)) || endDate.before(startDate) )
            return false;

        return true;
    }


    //non mi convice fatto così
    /**
     * Aggiunge una nuova asta
     * @param asta l'asta da aggiungere
     * @return vero se l'aggiunta è andata a buon fine, falso altrimenti
     */
    @Override
    public boolean addAsta(Asta asta) {
        astaDao.doCreate(asta);
        return true;
    }

    /**
     * Rimuove un'asta
     *
     * @param asta l'asta da rimuovere
     * @return vero se la rimozione è andata a buon fine, falso altrimenti
     */
    @Override
    public boolean removeAsta(Asta asta) {
        astaDao.doDelete(asta);
        return true;
    }

    /**
     * Verifica se l'asta è terminata per lo scadere del tempo
     *
     * @param asta l'asta da verificare
     * @return vero o falso
     */
    @Override
    public boolean isTerminated(Asta asta) {
        return false;
    }

    /**
     * Verifica se l'asta è terminata perchè vinta da qualcuno
     *
     * @param asta l'asta da verificare
     * @return l'utente che ha vinto quell'asta
     */

    @Override
    public Utente winAsta(Asta asta) {
        return null;
    }

    private AstaDao astaDao;
}
