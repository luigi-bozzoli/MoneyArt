package unit.gestione.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaServiceImpl;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.*;
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import net.bytebuddy.asm.Advice;
import org.apache.ibatis.annotations.Param;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AstaServiceImplUnitTest {

	@Mock
	private AstaDao astaDao;
	@Mock
	private OperaDao operaDao;
	@Mock
	private UtenteDao utenteDao;
	@Mock
	private PartecipazioneDao partecipazioneDao;
	@Mock
	private NotificaDao notificaDao;
	@Mock
	private TimerScheduler timerScheduler;
	@Mock
	private AstaLockingSingleton astaLockingSingleton;

	private AstaService astaService;

	@BeforeEach
	void generalSetUp() {
		astaService = new AstaServiceImpl(astaDao, operaDao, utenteDao, partecipazioneDao, timerScheduler, astaLockingSingleton, notificaDao);
	}

	static class AstaProvider implements ArgumentsProvider {

		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
			Asta a0 = AstaCreations.ongoingAstaLastingSevenDaysWithNoArtistFollowersAndNoBids();
			Asta a1 = AstaCreations.ongoingAstaLastingSevenDaysWith3ArtistFollowersAnd3Bids();
			return Stream.of(Arguments.of(a0), Arguments.of(a1));
		}
	}

	static class ListAstaProvider implements ArgumentsProvider {

		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
			List<Asta> aste1 = AstaCreations.astaThreeElementsList();

			return Stream.of(Arguments.of(null), Arguments.of(aste1));
		}
	}

	@Nested
	@DisplayName("Get auction")
	class getAuctionTest {

		@DisplayName("Get existing auction")
		@ParameterizedTest
		@ArgumentsSource(AstaProvider.class)
		void getExistingAuctionTest(Asta asta) {
			when(astaDao.doRetrieveById(anyInt())).thenReturn(asta);
			when(operaDao.doRetrieveById(anyInt())).thenReturn(asta.getOpera());
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(asta.getPartecipazioni());
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(asta.getOpera().getArtista());

			// metodo privato getNumberOfFollowers
			List<Utente> followers = new ArrayList<>();
			followers.add(new Utente());
			followers.add(new Utente());
			followers.add(new Utente());
			when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(followers);

			Asta requested = astaService.getAuction(asta.getId());

			Assertions.assertEquals(asta, requested);
			Assertions.assertEquals(asta.getOpera(), requested.getOpera());
			Assertions.assertEquals(asta.getPartecipazioni(), requested.getPartecipazioni());
			Assertions.assertEquals(asta.getOpera().getArtista(), requested.getOpera().getArtista());
			Assertions.assertEquals(asta.getOpera().getArtista().getnFollowers(), requested.getOpera().getArtista().getnFollowers());
		}

		@DisplayName("Get non existing auction")
		@Test
		void getNonExistingAuctionTest() {
			when(astaDao.doRetrieveById(anyInt())).thenReturn(null);

			Asta requested = astaService.getAuction(anyInt());

			Assertions.assertNull(requested);
		}
	}

	@Nested
	@DisplayName("Get all auctions")
	class getAllAuctionsTest {

		@DisplayName("Get all auctions")
		@Test
		void getAllAuctionsTest() {
			List<Asta> aste = AstaCreations.astaThreeElementsList();

			when(astaDao.doRetrieveAll(null)).thenReturn(aste);
			when(operaDao.doRetrieveById(anyInt())).thenReturn(
				aste.get(0).getOpera(),
				aste.get(1).getOpera(),
				aste.get(2).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
				aste.get(0).getPartecipazioni(),
				aste.get(1).getPartecipazioni(),
				aste.get(2).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
				aste.get(0).getOpera().getArtista(),
				aste.get(1).getOpera().getArtista(),
				aste.get(2).getOpera().getArtista()
			);

			// metodo privato getNumberOfFollowers
			// In questo test, non tutti gli utenti hanno 1 follower,
			// ma non è un aspetto rilevante
			List<Utente> followers = new ArrayList<>();    // lista di size 1
			followers.add(new Utente());
			when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(followers);

			List<Asta> requested = astaService.getAllAuctions();

			Assertions.assertArrayEquals(aste.toArray(), requested.toArray());
		}

		@DisplayName("Get no auctions")
		@Test
		void getNoAuctionsTest() {
			when(astaDao.doRetrieveAll(null)).thenReturn(null);

			List<Asta> requested = astaService.getAllAuctions();

			Assertions.assertNull(requested);
		}
	}

	@Nested
	@DisplayName("Get auctions sorted by price")
	class getAuctionsSortedByPriceTest {

		@DisplayName("Get ongoing auctions sorted by Price DESC")
		@Test
		void getOngoingAuctionsSortedByPriceDescTest() {
			List<Asta> oracle = AstaCreations.ongoingAstaTwoElementsListSortedByPriceDesc();

			List<Asta> ongoingAuctions = AstaCreations.ongoingAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.IN_CORSO)).thenReturn(ongoingAuctions);

			// for di getAuctionsByState(Stato s)
			when(operaDao.doRetrieveById(anyInt())).thenReturn(
				ongoingAuctions.get(0).getOpera(),
				ongoingAuctions.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
				ongoingAuctions.get(0).getPartecipazioni(),
				ongoingAuctions.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
				ongoingAuctions.get(0).getOpera().getArtista(),
				ongoingAuctions.get(1).getOpera().getArtista()
			);

			int nFol0 = ongoingAuctions.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = ongoingAuctions.get(1).getOpera().getArtista().getnFollowers();
			List<Utente> followers0 = new ArrayList<>();
			List<Utente> followers1 = new ArrayList<>();
			for(int i = 0; i < nFol0; i++){
				followers0.add(new Utente());
			}
			for(int i = 0; i < nFol1; i++){
				followers1.add(new Utente());
			}

			when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(
					followers0,
					followers1
			);
			// fine for

			List<Asta> requested = astaService.getAuctionsSortedByPrice("DESC", Asta.Stato.IN_CORSO);

			Assertions.assertEquals(oracle.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(oracle.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(oracle.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(oracle.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(oracle.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(oracle.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(oracle.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(oracle.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(oracle.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(oracle.get(1).getOpera(), requested.get(1).getOpera());

			// Id aste presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getAsta().getId(), requested.get(0).getPartecipazioni().get(0).getAsta().getId());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(1).getAsta().getId(), requested.get(1).getPartecipazioni().get(1).getAsta().getId());

			// Utenti presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getUtente(), requested.get(0).getPartecipazioni().get(0).getUtente());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(1).getUtente(), requested.get(1).getPartecipazioni().get(1).getUtente());

			// Valori delle offerte
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getOfferta(), requested.get(0).getPartecipazioni().get(0).getOfferta());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(1).getOfferta(), requested.get(1).getPartecipazioni().get(1).getOfferta());
		}

		@DisplayName("Get ongoing auctions sorted by Price ASC")
		@Test
		void getOngoingAuctionsSortedByPriceAscTest() {
			List<Asta> oracle = AstaCreations.ongoingAstaTwoElementsListSortedByPriceAsc();

			List<Asta> ongoingAuctions = AstaCreations.ongoingAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.IN_CORSO)).thenReturn(ongoingAuctions);

			// for di getAuctionsByState(Stato s)
			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							ongoingAuctions.get(0).getOpera(),
							ongoingAuctions.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							ongoingAuctions.get(0).getPartecipazioni(),
							ongoingAuctions.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							ongoingAuctions.get(0).getOpera().getArtista(),
							ongoingAuctions.get(1).getOpera().getArtista()
			);

			int nFol0 = ongoingAuctions.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = ongoingAuctions.get(1).getOpera().getArtista().getnFollowers();
			List<Utente> followers0 = new ArrayList<>();
			List<Utente> followers1 = new ArrayList<>();
			for(int i = 0; i < nFol0; i++){
				followers0.add(new Utente());
			}
			for(int i = 0; i < nFol1; i++){
				followers1.add(new Utente());
			}

			when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(
							followers0,
							followers1
			);
			// fine for

			List<Asta> requested = astaService.getAuctionsSortedByPrice("ASC", Asta.Stato.IN_CORSO);

			Assertions.assertEquals(oracle.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(oracle.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(oracle.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(oracle.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(oracle.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(oracle.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(oracle.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(oracle.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(oracle.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(oracle.get(1).getOpera(), requested.get(1).getOpera());

			// Id aste presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getAsta().getId(), requested.get(0).getPartecipazioni().get(0).getAsta().getId());
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(1).getAsta().getId(), requested.get(0).getPartecipazioni().get(1).getAsta().getId());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getUtente(), requested.get(0).getPartecipazioni().get(0).getUtente());
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(1).getUtente(), requested.get(0).getPartecipazioni().get(1).getUtente());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getOfferta(), requested.get(0).getPartecipazioni().get(0).getOfferta());
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(1).getOfferta(), requested.get(0).getPartecipazioni().get(1).getOfferta());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
		}

		@DisplayName("Get ended auctions sorted by Price DESC")
		@Test
		void getEndedAuctionsSortedByPriceDescTest() {
			List<Asta> oracle = AstaCreations.endedAstaTwoElementsListSortedByPriceDesc();

			List<Asta> endedAuctions = AstaCreations.endedAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.TERMINATA)).thenReturn(endedAuctions);

			// for di getAuctionsByState(Stato s)
			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							endedAuctions.get(0).getOpera(),
							endedAuctions.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							endedAuctions.get(0).getPartecipazioni(),
							endedAuctions.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							endedAuctions.get(0).getOpera().getArtista(),
							endedAuctions.get(1).getOpera().getArtista()
			);

			int nFol0 = endedAuctions.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = endedAuctions.get(1).getOpera().getArtista().getnFollowers();
			List<Utente> followers0 = new ArrayList<>();
			List<Utente> followers1 = new ArrayList<>();
			for(int i = 0; i < nFol0; i++){
				followers0.add(new Utente());
			}
			for(int i = 0; i < nFol1; i++){
				followers1.add(new Utente());
			}

			when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(
							followers0,
							followers1
			);
			// fine for

			List<Asta> requested = astaService.getAuctionsSortedByPrice("DESC", Asta.Stato.TERMINATA);

			Assertions.assertEquals(oracle.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(oracle.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(oracle.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(oracle.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(oracle.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(oracle.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(oracle.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(oracle.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(oracle.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(oracle.get(1).getOpera(), requested.get(1).getOpera());

			// Id aste presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getAsta().getId(), requested.get(0).getPartecipazioni().get(0).getAsta().getId());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(1).getAsta().getId(), requested.get(1).getPartecipazioni().get(1).getAsta().getId());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(2).getAsta().getId(), requested.get(1).getPartecipazioni().get(2).getAsta().getId());

			// Utenti presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getUtente(), requested.get(0).getPartecipazioni().get(0).getUtente());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(1).getUtente(), requested.get(1).getPartecipazioni().get(1).getUtente());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(2).getUtente(), requested.get(1).getPartecipazioni().get(2).getUtente());

			// Valori delle offerte
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getOfferta(), requested.get(0).getPartecipazioni().get(0).getOfferta());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(1).getOfferta(), requested.get(1).getPartecipazioni().get(1).getOfferta());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(2).getOfferta(), requested.get(1).getPartecipazioni().get(2).getOfferta());
		}

		@DisplayName("Get ended auctions sorted by Price ASC")
		@Test
		void getEndedAuctionsSortedByPriceAscTest() {
			List<Asta> oracle = AstaCreations.endedAstaTwoElementsListSortedByPriceAsc();

			List<Asta> endedAuctions = AstaCreations.endedAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.TERMINATA)).thenReturn(endedAuctions);

			// for di getAuctionsByState(Stato s)
			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							endedAuctions.get(0).getOpera(),
							endedAuctions.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							endedAuctions.get(0).getPartecipazioni(),
							endedAuctions.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							endedAuctions.get(0).getOpera().getArtista(),
							endedAuctions.get(1).getOpera().getArtista()
			);

			int nFol0 = endedAuctions.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = endedAuctions.get(1).getOpera().getArtista().getnFollowers();
			List<Utente> followers0 = new ArrayList<>();
			List<Utente> followers1 = new ArrayList<>();
			for(int i = 0; i < nFol0; i++){
				followers0.add(new Utente());
			}
			for(int i = 0; i < nFol1; i++){
				followers1.add(new Utente());
			}

			when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(
							followers0,
							followers1
			);
			// fine for

			List<Asta> requested = astaService.getAuctionsSortedByPrice("ASC", Asta.Stato.TERMINATA);

			Assertions.assertEquals(oracle.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(oracle.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(oracle.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(oracle.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(oracle.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(oracle.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(oracle.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(oracle.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(oracle.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(oracle.get(1).getOpera(), requested.get(1).getOpera());

			// Id aste presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getAsta().getId(), requested.get(0).getPartecipazioni().get(0).getAsta().getId());
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(1).getAsta().getId(), requested.get(0).getPartecipazioni().get(1).getAsta().getId());
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(2).getAsta().getId(), requested.get(0).getPartecipazioni().get(2).getAsta().getId());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getUtente(), requested.get(0).getPartecipazioni().get(0).getUtente());
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(1).getUtente(), requested.get(0).getPartecipazioni().get(1).getUtente());
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(2).getUtente(), requested.get(0).getPartecipazioni().get(2).getUtente());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getOfferta(), requested.get(0).getPartecipazioni().get(0).getOfferta());
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(1).getOfferta(), requested.get(0).getPartecipazioni().get(1).getOfferta());
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(2).getOfferta(), requested.get(0).getPartecipazioni().get(2).getOfferta());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
		}

		@DisplayName("Get deleted auctions sorted by Price DESC")
		@Test
		void getDeletedAuctionsSortedByPriceDescTest() {
			List<Asta> oracle = AstaCreations.deletedAstaTwoElementsListSortedByPriceDesc();

			List<Asta> deletedAuctions = AstaCreations.deletedAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.ELIMINATA)).thenReturn(deletedAuctions);

			// for di getAuctionsByState(Stato s)
			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							deletedAuctions.get(0).getOpera(),
							deletedAuctions.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							deletedAuctions.get(0).getPartecipazioni(),
							deletedAuctions.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							deletedAuctions.get(0).getOpera().getArtista(),
							deletedAuctions.get(1).getOpera().getArtista()
			);

			int nFol0 = deletedAuctions.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = deletedAuctions.get(1).getOpera().getArtista().getnFollowers();
			List<Utente> followers0 = new ArrayList<>();
			List<Utente> followers1 = new ArrayList<>();
			for(int i = 0; i < nFol0; i++){
				followers0.add(new Utente());
			}
			for(int i = 0; i < nFol1; i++){
				followers1.add(new Utente());
			}

			when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(
							followers0,
							followers1
			);
			// fine for

			List<Asta> requested = astaService.getAuctionsSortedByPrice("DESC", Asta.Stato.ELIMINATA);

			Assertions.assertEquals(oracle.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(oracle.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(oracle.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(oracle.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(oracle.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(oracle.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(oracle.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(oracle.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(oracle.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(oracle.get(1).getOpera(), requested.get(1).getOpera());

			// Id aste presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getAsta().getId(), requested.get(0).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getUtente(), requested.get(0).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getOfferta(), requested.get(0).getPartecipazioni().get(0).getOfferta());

			// Nessuna partecipazione per la seconda asta
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().size(), requested.get(1).getPartecipazioni().size());
		}

		@DisplayName("Get deleted auctions sorted by Price ASC")
		@Test
		void getDeletedAuctionsSortedByPriceAscTest() {
			List<Asta> oracle = AstaCreations.deletedAstaTwoElementsListSortedByPriceAsc();

			List<Asta> deletedAuctions = AstaCreations.deletedAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.ELIMINATA)).thenReturn(deletedAuctions);

			// for di getAuctionsByState(Stato s)
			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							deletedAuctions.get(0).getOpera(),
							deletedAuctions.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							deletedAuctions.get(0).getPartecipazioni(),
							deletedAuctions.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							deletedAuctions.get(0).getOpera().getArtista(),
							deletedAuctions.get(1).getOpera().getArtista()
			);

			int nFol0 = deletedAuctions.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = deletedAuctions.get(1).getOpera().getArtista().getnFollowers();
			List<Utente> followers0 = new ArrayList<>();
			List<Utente> followers1 = new ArrayList<>();
			for(int i = 0; i < nFol0; i++){
				followers0.add(new Utente());
			}
			for(int i = 0; i < nFol1; i++){
				followers1.add(new Utente());
			}

			when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(
							followers0,
							followers1
			);
			// fine for

			List<Asta> requested = astaService.getAuctionsSortedByPrice("ASC", Asta.Stato.ELIMINATA);

			Assertions.assertEquals(oracle.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(oracle.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(oracle.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(oracle.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(oracle.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(oracle.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(oracle.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(oracle.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(oracle.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(oracle.get(1).getOpera(), requested.get(1).getOpera());

			// Nessuna partecipazione per la prima asta
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().size(), requested.get(0).getPartecipazioni().size());

			// Id aste presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
		}

		// TODO: Test per le aste con stato "CREATA"? Hanno tutte come offerta max 0, quindi sarebbe inutile ordinarle
	}

	@Nested
	@DisplayName("Get auctions sorted by artist's number of followers")
	class getAuctionsSortedByArtistFollowersTest {

	}

	@Nested
	@DisplayName("Get auctions sorted by expiration time")
	class getAuctionsSortedByExpirationTimeTest {

	}

	@Nested
	@DisplayName("Get auctions by state")
	class getAuctionsByStateTest {

	}

	@Nested
	@DisplayName("Participate auction")
	class participateAuctionTest {

	}

	@Nested
	@DisplayName("Check date")
	class checkDateTest {

	}

	@Nested
	@DisplayName("Add asta")
	class addAstaTest {

	}

	@Nested
	@DisplayName("Remove asta")
	class removeAstaTest {

	}

	@Nested
	@DisplayName("Annulla asta")
	class annullaAstaTest {

	}

	@Nested
	@DisplayName("Best offer")
	class bestOfferTest {

	}

	@Nested
	@DisplayName("Get won auctions")
	class getWonAuctionsTest {

	}

	@Nested
	@DisplayName("Get lost auctions")
	class getLostAuctionsTest {

	}

	@Nested
	@DisplayName("Get current auctions")
	class getCurrentAuctionsTest {

	}

	@Nested
	@DisplayName("Get current auctions")
	class getAllOffersTest {

	}

	@Nested
	@DisplayName("Execute timed tasks")
	class executeTimedTasksTest {

	}
}
