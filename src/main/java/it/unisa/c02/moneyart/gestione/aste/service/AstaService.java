package it.unisa.c02.moneyart.gestione.aste.service;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;

import java.util.Date;

public interface AstaService {

//_______________________________Creazione ed eliminazione asta________________________________

  //vanno aggiunti i vincoli come documentazione qui.

  /**
   * Aggiunge una nuova asta.
   *
   * @param asta l'asta da aggiungere
   * @return vero se l'aggiunta è andata a buon fine, falso altrimenti
   */
  boolean addAsta(Asta asta);

  /**
   * Rimuove un'asta.
   *
   * @param asta l'asta da rimuovere
   * @return vero se la rimozione è andata a buon fine, falso altrimenti
   */
  boolean removeAsta(Asta asta);


//_______________________________Gestione e terminazione asta________________________________

  /**
   * Restituisce la migliore offerta fatta all'asta.
   *
   * @param asta l'asta da verificare
   * @return la migliore offerta per l'asta o null se non è presente alcuna offerta
   */
   Partecipazione bestOffer(Asta asta);


}
