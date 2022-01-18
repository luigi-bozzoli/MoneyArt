package it.unisa.c02.moneyart.gestione.vendite.aste.service;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;

/**
 * Classe che espone i servizi di logica di buisness di un oggetto Asta.
 *
 */
public interface AstaService {

  Asta getAuction(int id);

  List<Asta> getAllAuctions();

  List<Asta> getAuctionsByState(Asta.Stato s);

  List<Asta> getAuctionsSortedByPrice(String order, Asta.Stato s);

  List<Asta> getAuctionsSortedByArtistFollowers(String order, Asta.Stato s);

  List<Asta> getAuctionsSortedByExpirationTime(String order, Asta.Stato s);

  boolean partecipateAuction(Utente utente, Asta asta, double offerta);

  boolean addAsta(Asta asta);

  boolean removeAsta(Asta asta);

  boolean annullaAsta(Asta asta);

  Partecipazione bestOffer(Asta asta);

  List<Asta> getWonAuctions(Utente utente);

  List<Asta> getLostAuctions(Utente utente);

  List<Asta> getCurrentAuctions(Utente utente);

  List<Partecipazione> getAllOffers();

}
