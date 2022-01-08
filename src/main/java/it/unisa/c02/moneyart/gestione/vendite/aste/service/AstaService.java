package it.unisa.c02.moneyart.gestione.vendite.aste.service;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;

import java.util.Date;
import java.util.List;

public interface AstaService {

//_______________________________Creazione ed eliminazione asta________________________________

  //vanno aggiunti i vincoli come documentazione qui.

  /**
   * Restituisce tutte le informazioni relative ad un asta.
   *
   * @param id l'identificativo dell'asta
   * @return l'asta identificata dall'id
   */
  Asta getAuction(int id);

  /**
   * Restituisce tutte le aste esistenti sulla piattaforma.
   *
   * @return la lista di tutte le aste presenti sulla piattaforma.
   */
  List<Asta> getAllAuctions();

  /**
   * Permette ad un utente di partecipare ad un asta.
   * Precondizine: L'Asta non deve essere terminata,
   * l'utente deve avere un saldo sufficente per effettuare l'offerta e
   * L'offerta è superiore a quella attuale dell'asta
   * Postcondizione: l'offerta viene registrata
   *
   * @param utente  l'utente che vuole effettuare l'offerta
   * @param asta    l'asta per cui si vuole effetuare l'offerta
   * @param offerta l'offerta fatta dall'utente
   * @return vero se l'offerta va a buon fine, falso altrimenti
   */
  boolean partecipateAuction(Utente utente, Asta asta, double offerta);

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

  /**
   * annulla un asta.
   *
   * @param asta l'asta da annullare
   * @return vero se l'annullamento è andata a buon fine, falso altrimenti
   */
  boolean annullaAsta(Asta asta);


//_______________________________Gestione e terminazione asta________________________________

  /**
   * Restituisce la migliore offerta fatta all'asta.
   *
   * @param asta l'asta da verificare
   * @return la migliore offerta per l'asta o null se non è presente alcuna offerta
   */
  Partecipazione bestOffer(Asta asta);


}
