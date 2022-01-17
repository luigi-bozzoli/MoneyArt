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
   * Restituisce le aste sulla piattaforma che si trovano in un determinato stato.
   *
   * @param s stato delle aste da restituire
   * @return la lista di tutte le aste presenti sulla piattaforma in un determinato stato.
   */
  List<Asta> getAuctionsByState(Asta.Stato s);

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

  /**
   * Restituisce tutte le aste vinte da un utente.
   *
   * @param utente l'utente per cui bisogna cercare le aste vinte
   * @return le aste vinte dall'utente
   */
  List<Asta> getWonAuctions(Utente utente);

  /**
   * Restituisce tutte le aste perse da un utente.
   *
   * @param utente l'utente per cui bisogna cercare le aste perse
   * @return le aste perse dall'utente
   */
  List<Asta> getLostAuctions(Utente utente);

  /**
   * Restituisce tutte le aste in corso di un utente.
   *
   * @param utente l'utente per cui bisogna cercare le aste in corso
   * @return le aste in corso dell'utente
   */
  List<Asta> getCurrentAuctions(Utente utente);


  /**
   * Restituisce lo storico di tutte le offerte.
   *
   * @return lo storico di tutte le offerte
   */
  List<Partecipazione> getAllOffers();

  /**
   * Restituisce tutte le aste ordinate in base al prezzo.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @return la lista ordinata
   */
  List<Asta> getAuctionsSortedByPrice(String order);

  /**
   * Restituisce tutte le aste ordinate in base ai follower dell'artista.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @return la lista ordinata
   */
  List<Asta> getAuctionsSortedByArtistFollowers(String order);

  /**
   * Restituisce tutte le aste ordinate in base alla scadenza.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @return la lista ordinata
   */
  List<Asta> getAuctionsSortedByExpirationTime(String order);
}
