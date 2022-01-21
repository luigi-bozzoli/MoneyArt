package unit.gestione.service;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

abstract class AstaCreations {

	private static final long DAY_MILLIS = 1000 * 60 * 60 * 24;

	public static Asta ongoingAstaLastingSevenDaysWithNoArtistFollowersAndNoBids() {
		Utente u1 = new Utente();
		u1.setId(1);

		u1.setnFollowers(0);

		Opera o1 = new Opera();
		o1.setId(1);
		o1.setArtista(u1);

		Asta a1 = new Asta(o1, new Date(), new Date(System.currentTimeMillis() + 7 * DAY_MILLIS), Asta.Stato.IN_CORSO);
		a1.setId(1);

		List<Partecipazione> p1 = new ArrayList<>();
		a1.setPartecipazioni(p1);

		return a1;
	}

	public static Asta ongoingAstaLastingSevenDaysWith3ArtistFollowersAnd3Bids() {
		// Utente u1 con 3 followers
		Utente u1 = new Utente();
		u1.setId(1);

		Utente u2 = new Utente();
		u2.setId(2);
		u2.setSeguito(u1);

		Utente u3 = new Utente();
		u3.setId(3);
		u3.setSeguito(u1);

		Utente u4 = new Utente();
		u4.setId(4);
		u4.setSeguito(u1);

		u1.setnFollowers(3);

		// Utente u1 autore dell'opera
		Opera o1 = new Opera();
		o1.setId(1);
		o1.setArtista(u1);

		Asta a1 = new Asta(o1, new Date(), new Date(System.currentTimeMillis() + 7 * DAY_MILLIS), Asta.Stato.IN_CORSO);
		a1.setId(1);

		// Asta a1 con 3 partecipazioni
		List<Partecipazione> p1 = new ArrayList<>();
		p1.add(new Partecipazione(a1, u2, 99.99d));
		p1.add(new Partecipazione(a1, u3, 199.99d));
		p1.add(new Partecipazione(a1, u4, 299.99d));
		a1.setPartecipazioni(p1);

		return a1;
	}

	/*
	public static List<Asta> emptyList() {
		return new ArrayList<Asta>();
	}
	*/

	public static List<Asta> astaThreeElementsList() {
		List<Asta> aste = new ArrayList<>();

		// Utente u1 con 3 followers
		Utente u1 = new Utente();
		u1.setId(1);

		Utente u2 = new Utente();
		u2.setId(2);
		u2.setSeguito(u1);

		Utente u3 = new Utente();
		u3.setId(3);
		u3.setSeguito(u1);

		Utente u4 = new Utente();
		u4.setId(4);
		u4.setSeguito(u1);

		u1.setnFollowers(3);
		u2.setnFollowers(0);
		u3.setnFollowers(0);
		u4.setnFollowers(0);

		/********** Asta 1 **********/

		// Utente u1 autore dell'opera
		Opera o1 = new Opera();
		o1.setId(1);
		o1.setArtista(u1);

		Asta a1 = new Asta(o1, new Date(System.currentTimeMillis() - 7 * DAY_MILLIS), new Date(System.currentTimeMillis()), Asta.Stato.TERMINATA);
		a1.setId(1);

		// Asta a1 con 3 partecipazioni
		List<Partecipazione> p1 = new ArrayList<>();
		p1.add(new Partecipazione(a1, u2, 99.99d));
		p1.add(new Partecipazione(a1, u3, 199.99d));
		p1.add(new Partecipazione(a1, u4, 299.99d));
		a1.setPartecipazioni(p1);

		aste.add(a1);

		/********** Asta 2 **********/

		Opera o2 = new Opera();
		o2.setId(2);
		o2.setArtista(u2);

		Asta a2 = new Asta(o2, new Date(), new Date(System.currentTimeMillis() + 5 * DAY_MILLIS), Asta.Stato.IN_CORSO);
		a2.setId(2);

		List<Partecipazione> p2 = new ArrayList<>();
		a2.setPartecipazioni(p2);

		aste.add(a2);

		/********** Asta 3 **********/

		Opera o3 = new Opera();
		o3.setId(3);
		o3.setArtista(u3);

		Asta a3 = new Asta(o3, new Date(), new Date(System.currentTimeMillis() + 5 * DAY_MILLIS), Asta.Stato.ELIMINATA);
		a3.setId(3);

		List<Partecipazione> p3 = new ArrayList<>();
		a3.setPartecipazioni(p3);

		aste.add(a3);

		return aste;
	}

	public static List<Asta> astaSixElementsList() {
		List<Asta> aste = new ArrayList<>();

		// Utente u1 con 3 followers
		Utente u1 = new Utente();
		u1.setId(1);

		Utente u2 = new Utente();
		u2.setId(2);
		u2.setSeguito(u1);

		Utente u3 = new Utente();
		u3.setId(3);
		u3.setSeguito(u1);

		Utente u4 = new Utente();
		u4.setId(4);
		u4.setSeguito(u1);

		Utente u5 = new Utente();
		u5.setId(5);

		Utente u6 = new Utente();
		u6.setId(6);

		u1.setnFollowers(3);
		u2.setnFollowers(0);
		u3.setnFollowers(0);
		u4.setnFollowers(0);
		u5.setnFollowers(0);
		u6.setnFollowers(0);

		/********** Asta 1 **********/

		// Utente u1 autore dell'opera
		Opera o1 = new Opera();
		o1.setId(1);
		o1.setArtista(u1);
		o1.setPossessore(u4);

		Asta a1 = new Asta(o1, new Date(System.currentTimeMillis() - 7 * DAY_MILLIS), new Date(System.currentTimeMillis()), Asta.Stato.TERMINATA);
		a1.setId(1);

		// Asta a1 con 3 partecipazioni
		List<Partecipazione> p1 = new ArrayList<>();
		p1.add(new Partecipazione(a1, u2, 99.99d));
		p1.add(new Partecipazione(a1, u3, 199.99d));
		p1.add(new Partecipazione(a1, u4, 299.99d));
		a1.setPartecipazioni(p1);

		aste.add(a1);

		/********** Asta 2 **********/

		Opera o2 = new Opera();
		o2.setId(2);
		o2.setArtista(u2);
		o2.setPossessore(u5);

		Asta a2 = new Asta(o2, new Date(System.currentTimeMillis() - 7 * DAY_MILLIS), new Date(System.currentTimeMillis()), Asta.Stato.TERMINATA);
		a2.setId(2);

		List<Partecipazione> p2 = new ArrayList<>();
		p2.add(new Partecipazione(a2, u5, 999.99d));
		a2.setPartecipazioni(p2);

		aste.add(a2);

		/********** Asta 3 **********/

		Opera o3 = new Opera();
		o3.setId(3);
		o3.setArtista(u3);
		o3.setPossessore(u3);

		Asta a3 = new Asta(o3, new Date(), new Date(System.currentTimeMillis() + 5 * DAY_MILLIS), Asta.Stato.IN_CORSO);
		a3.setId(3);

		List<Partecipazione> p3 = new ArrayList<>();
		p3.add(new Partecipazione(a3, u6, 800.00d));
		a3.setPartecipazioni(p3);

		aste.add(a3);

		/********** Asta 4 **********/

		Opera o4 = new Opera();
		o4.setId(4);
		o4.setArtista(u4);
		o4.setPossessore(u4);

		Asta a4 = new Asta(o4, new Date(), new Date(System.currentTimeMillis() + 5 * DAY_MILLIS), Asta.Stato.IN_CORSO);
		a4.setId(4);

		List<Partecipazione> p4 = new ArrayList<>();
		p4.add(new Partecipazione(a4, u3, 789.98d));
		p4.add(new Partecipazione(a4, u2, 790.00d));
		a4.setPartecipazioni(p4);

		aste.add(a4);

		/********** Asta 5 **********/

		Opera o5 = new Opera();
		o5.setId(5);
		o5.setArtista(u5);
		o5.setPossessore(u5);

		Asta a5 = new Asta(o5, new Date(), new Date(System.currentTimeMillis() + 5 * DAY_MILLIS), Asta.Stato.ELIMINATA);
		a5.setId(5);

		List<Partecipazione> p5 = new ArrayList<>();
		a5.setPartecipazioni(p5);

		aste.add(a5);

		/********** Asta 6 **********/

		Opera o6 = new Opera();
		o6.setId(6);
		o6.setArtista(u6);
		o6.setPossessore(u6);

		Asta a6 = new Asta(o6, new Date(), new Date(System.currentTimeMillis() + 5 * DAY_MILLIS), Asta.Stato.ELIMINATA);
		a6.setId(6);

		List<Partecipazione> p6 = new ArrayList<>();
		p6.add(new Partecipazione(a6, u1, 1999.99d));
		a6.setPartecipazioni(p6);

		aste.add(a6);

		return aste;
	}

	public static List<Asta> ongoingAstaTwoElementsList() {
		List<Asta> aste = astaSixElementsList();
		aste.remove(0);
		aste.remove(0);
		aste.remove(3);
		aste.remove(2);
		return aste;
	}

	public static List<Asta> endedAstaTwoElementsList() {
		List<Asta> aste = astaSixElementsList();
		aste.remove(5);
		aste.remove(4);
		aste.remove(3);
		aste.remove(2);
		return aste;
	}

	public static List<Asta> deletedAstaTwoElementsList() {
		List<Asta> aste = astaSixElementsList();
		aste.remove(0);
		aste.remove(0);
		aste.remove(0);
		aste.remove(0);
		return aste;
	}

	public static List<Asta> ongoingAstaTwoElementsListSortedByPriceDesc() {
		List<Asta> aste = astaSixElementsList();
		aste.remove(0);
		aste.remove(0);
		aste.remove(3);
		aste.remove(2);
		return aste;
	}

	public static List<Asta> ongoingAstaTwoElementsListSortedByPriceAsc() {
		List<Asta> aste = astaSixElementsList();
		aste.remove(0);
		aste.remove(0);
		aste.remove(3);
		aste.remove(2);
		Collections.swap(aste, 0, 1);
		return aste;
	}

	public static List<Asta> endedAstaTwoElementsListSortedByPriceDesc() {
		List<Asta> aste = astaSixElementsList();
		aste.remove(5);
		aste.remove(4);
		aste.remove(3);
		aste.remove(2);
		Collections.swap(aste,0,1);
		return aste;
	}

	public static List<Asta> endedAstaTwoElementsListSortedByPriceAsc() {
		List<Asta> aste = astaSixElementsList();
		aste.remove(5);
		aste.remove(4);
		aste.remove(3);
		aste.remove(2);
		return aste;
	}

	public static List<Asta> deletedAstaTwoElementsListSortedByPriceDesc() {
		List<Asta> aste = astaSixElementsList();
		aste.remove(0);
		aste.remove(0);
		aste.remove(0);
		aste.remove(0);
		Collections.swap(aste, 0, 1);
		return aste;
	}

	public static List<Asta> deletedAstaTwoElementsListSortedByPriceAsc() {
		List<Asta> aste = astaSixElementsList();
		aste.remove(0);
		aste.remove(0);
		aste.remove(0);
		aste.remove(0);
		return aste;
	}
}
