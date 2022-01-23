package unit.gestione.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaService;
import it.unisa.c02.moneyart.gestione.vendite.aste.service.AstaServiceImpl;
import it.unisa.c02.moneyart.model.beans.*;
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
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.mockito.Mockito.doNothing;
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

			// Id aste presenti nelle partecipazioni della prima asta
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getAsta().getId(), requested.get(0).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni della prima asta
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getUtente(), requested.get(0).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte della prima asta
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

			// Id aste presenti nelle partecipazioni della seconda asta
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni della seconda asta
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte della seconda asta
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
		}

		// TODO: Test per le aste con stato "CREATA"? Hanno tutte come offerta max 0, quindi sarebbe inutile ordinarle
	}

	@Nested
	@DisplayName("Get auctions sorted by artist's number of followers")
	class getAuctionsSortedByArtistFollowersTest {

		@DisplayName("Get ongoing auctions sorted by artist's followers DESC")
		@Test
		void getOngoingAuctionsSortedByArtistFollowerDescTest() {
			List<Asta> oracle = AstaCreations.ongoingAstaTwoElementsListSortedByArtistFollowersDesc();

			List<Asta> ongoingAuctions = AstaCreations.ongoingAstaTwoElementsWithArtistFollowersList();
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

			List<Asta> requested = astaService.getAuctionsSortedByArtistFollowers("DESC", Asta.Stato.IN_CORSO);

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

			// Non serve questo controllo, lo fanno già le due righe sopra
			// Assertions.assertEquals(oracle.get(0).getOpera().getArtista().getnFollowers(), requested.get(0).getOpera().getArtista().getnFollowers());
			// Assertions.assertEquals(oracle.get(1).getOpera().getArtista().getnFollowers(), requested.get(1).getOpera().getArtista().getnFollowers());

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

		@DisplayName("Get ongoing auctions sorted by artist's followers ASC")
		@Test
		void getOngoingAuctionsSortedByArtistFollowerAscTest() {
			List<Asta> oracle = AstaCreations.ongoingAstaTwoElementsListSortedByArtistFollowersAsc();

			List<Asta> ongoingAuctions = AstaCreations.ongoingAstaTwoElementsWithArtistFollowersList();
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

			List<Asta> requested = astaService.getAuctionsSortedByArtistFollowers("ASC", Asta.Stato.IN_CORSO);

			Assertions.assertEquals(oracle.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(oracle.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(oracle.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(oracle.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(oracle.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(oracle.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(oracle.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(oracle.get(1).getStato(), requested.get(1).getStato());

			// Controlla anche se il numero di presentazioni è corretto
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

		@DisplayName("Get ended auctions sorted by artist's followers DESC")
		@Test
		void getEndedAuctionsSortedByArtistFollowerDescTest() {
			List<Asta> oracle = AstaCreations.endedAstaTwoElementsListSortedByArtistFollowersDesc();

			List<Asta> endedAuctions = AstaCreations.endedAstaTwoElementsWithArtistFollowersList();
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

			List<Asta> requested = astaService.getAuctionsSortedByArtistFollowers("DESC", Asta.Stato.TERMINATA);

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

		@DisplayName("Get ended auctions sorted by artist's followers ASC")
		@Test
		void getEndedAuctionsSortedByArtistFollowerAscTest() {
			List<Asta> oracle = AstaCreations.endedAstaTwoElementsListSortedByArtistFollowersAsc();

			List<Asta> endedAuctions = AstaCreations.endedAstaTwoElementsWithArtistFollowersList();
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

			List<Asta> requested = astaService.getAuctionsSortedByArtistFollowers("ASC", Asta.Stato.TERMINATA);

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

		@DisplayName("Get deleted auctions sorted by artist's followers DESC")
		@Test
		void getDeletedAuctionsSortedByArtistFollowerDescTest() {
			List<Asta> oracle = AstaCreations.deletedAstaTwoElementsListSortedByArtistFollowersDesc();

			List<Asta> deletedAuctions = AstaCreations.deletedAstaTwoElementsWithArtistFollowersList();
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

			List<Asta> requested = astaService.getAuctionsSortedByArtistFollowers("DESC", Asta.Stato.ELIMINATA);

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

			// Id aste presenti nelle partecipazioni della seconda asta
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni della seconda asta
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte della seconda asta
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
		}

		@DisplayName("Get deleted auctions sorted by artist's followers ASC")
		@Test
		void getDeletedAuctionsSortedByArtistFollowerAscTest() {
			List<Asta> oracle = AstaCreations.deletedAstaTwoElementsListSortedByArtistFollowersAsc();

			List<Asta> deletedAuctions = AstaCreations.deletedAstaTwoElementsWithArtistFollowersList();
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

			List<Asta> requested = astaService.getAuctionsSortedByArtistFollowers("ASC", Asta.Stato.ELIMINATA);

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

			// Id aste presenti nelle partecipazioni della prima asta
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getAsta().getId(), requested.get(0).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni della prima asta
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getUtente(), requested.get(0).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte della prima asta
			Assertions.assertEquals(oracle.get(0).getPartecipazioni().get(0).getOfferta(), requested.get(0).getPartecipazioni().get(0).getOfferta());

			// Nessuna partecipazione per la seconda asta
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().size(), requested.get(1).getPartecipazioni().size());
		}

		@DisplayName("Get created auctions sorted by artist's followers DESC")
		@Test
		void getCreatedAuctionsSortedByArtistFollowerDescTest() {
			List<Asta> oracle = AstaCreations.createdAstaTwoElementsListSortedByArtistFollowersDesc();

			List<Asta> createdAuctions = AstaCreations.createdAstaTwoElementsWithArtistFollowersList();
			when(astaDao.doRetrieveByStato(Asta.Stato.CREATA)).thenReturn(createdAuctions);

			// for di getAuctionsByState(Stato s)
			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							createdAuctions.get(0).getOpera(),
							createdAuctions.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							createdAuctions.get(0).getPartecipazioni(),
							createdAuctions.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							createdAuctions.get(0).getOpera().getArtista(),
							createdAuctions.get(1).getOpera().getArtista()
			);

			int nFol0 = createdAuctions.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = createdAuctions.get(1).getOpera().getArtista().getnFollowers();
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

			List<Asta> requested = astaService.getAuctionsSortedByArtistFollowers("DESC", Asta.Stato.CREATA);

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

			Assertions.assertEquals(oracle.get(0).getPartecipazioni().size(), requested.get(0).getPartecipazioni().size());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().size(), requested.get(1).getPartecipazioni().size());
		}

		@DisplayName("Get created auctions sorted by artist's followers ASC")
		@Test
		void getCreatedAuctionsSortedByArtistFollowerAscTest() {
			List<Asta> oracle = AstaCreations.createdAstaTwoElementsListSortedByArtistFollowersAsc();

			List<Asta> createdAuctions = AstaCreations.createdAstaTwoElementsWithArtistFollowersList();
			when(astaDao.doRetrieveByStato(Asta.Stato.CREATA)).thenReturn(createdAuctions);

			// for di getAuctionsByState(Stato s)
			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							createdAuctions.get(0).getOpera(),
							createdAuctions.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							createdAuctions.get(0).getPartecipazioni(),
							createdAuctions.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							createdAuctions.get(0).getOpera().getArtista(),
							createdAuctions.get(1).getOpera().getArtista()
			);

			int nFol0 = createdAuctions.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = createdAuctions.get(1).getOpera().getArtista().getnFollowers();
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

			List<Asta> requested = astaService.getAuctionsSortedByArtistFollowers("ASC", Asta.Stato.CREATA);

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

			Assertions.assertEquals(oracle.get(0).getPartecipazioni().size(), requested.get(0).getPartecipazioni().size());
			Assertions.assertEquals(oracle.get(1).getPartecipazioni().size(), requested.get(1).getPartecipazioni().size());
		}
	}

	@Nested
	@DisplayName("Get auctions sorted by expiration time")
	class getAuctionsSortedByExpirationTimeTest {
		// TODO: impl
	}

	@Nested
	@DisplayName("Get auctions by state")
	class getAuctionsByStateTest {

		@DisplayName("Get no auctions ")
		@ParameterizedTest
		@ValueSource(strings = {"CREATA","TERMINATA","IN_CORSO","ELIMINATA"})
		void getNoAuctionsTest(String stato) {
			when(astaDao.doRetrieveByStato(Asta.Stato.valueOf(stato))).thenReturn(null);
			List<Asta> result = astaService.getAuctionsByState(Asta.Stato.valueOf(stato));
			Assertions.assertNull(result);
		}

		@DisplayName("Get ongoing auctions ")
		@Test
		void getOngoingAuctionsTest() {
			List<Asta> ongoing = AstaCreations.ongoingAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.IN_CORSO)).thenReturn(ongoing);

			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							ongoing.get(0).getOpera(),
							ongoing.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							ongoing.get(0).getPartecipazioni(),
							ongoing.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							ongoing.get(0).getOpera().getArtista(),
							ongoing.get(1).getOpera().getArtista()
			);

			int nFol0 = ongoing.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = ongoing.get(1).getOpera().getArtista().getnFollowers();
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

			List<Asta> requested = astaService.getAuctionsByState(Asta.Stato.IN_CORSO);

			Assertions.assertEquals(ongoing.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(ongoing.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(ongoing.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(ongoing.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(ongoing.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(ongoing.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(ongoing.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(ongoing.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(ongoing.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(ongoing.get(1).getOpera(), requested.get(1).getOpera());

			// Id aste presenti nelle partecipazioni
			Assertions.assertEquals(ongoing.get(0).getPartecipazioni().get(0).getAsta().getId(), requested.get(0).getPartecipazioni().get(0).getAsta().getId());
			Assertions.assertEquals(ongoing.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());
			Assertions.assertEquals(ongoing.get(1).getPartecipazioni().get(1).getAsta().getId(), requested.get(1).getPartecipazioni().get(1).getAsta().getId());

			// Utenti presenti nelle partecipazioni
			Assertions.assertEquals(ongoing.get(0).getPartecipazioni().get(0).getUtente(), requested.get(0).getPartecipazioni().get(0).getUtente());
			Assertions.assertEquals(ongoing.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());
			Assertions.assertEquals(ongoing.get(1).getPartecipazioni().get(1).getUtente(), requested.get(1).getPartecipazioni().get(1).getUtente());

			// Valori delle offerte
			Assertions.assertEquals(ongoing.get(0).getPartecipazioni().get(0).getOfferta(), requested.get(0).getPartecipazioni().get(0).getOfferta());
			Assertions.assertEquals(ongoing.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
			Assertions.assertEquals(ongoing.get(1).getPartecipazioni().get(1).getOfferta(), requested.get(1).getPartecipazioni().get(1).getOfferta());
		}

		@DisplayName("Get ended auctions ")
		@Test
		void getEndedAuctionsTest() {
			List<Asta> ended = AstaCreations.endedAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.TERMINATA)).thenReturn(ended);

			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							ended.get(0).getOpera(),
							ended.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							ended.get(0).getPartecipazioni(),
							ended.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							ended.get(0).getOpera().getArtista(),
							ended.get(1).getOpera().getArtista()
			);

			int nFol0 = ended.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = ended.get(1).getOpera().getArtista().getnFollowers();
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

			List<Asta> requested = astaService.getAuctionsByState(Asta.Stato.TERMINATA);

			Assertions.assertEquals(ended.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(ended.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(ended.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(ended.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(ended.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(ended.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(ended.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(ended.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(ended.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(ended.get(1).getOpera(), requested.get(1).getOpera());

			// Id aste presenti nelle partecipazioni
			Assertions.assertEquals(ended.get(0).getPartecipazioni().get(0).getAsta().getId(), requested.get(0).getPartecipazioni().get(0).getAsta().getId());
			Assertions.assertEquals(ended.get(0).getPartecipazioni().get(1).getAsta().getId(), requested.get(0).getPartecipazioni().get(1).getAsta().getId());
			Assertions.assertEquals(ended.get(0).getPartecipazioni().get(2).getAsta().getId(), requested.get(0).getPartecipazioni().get(2).getAsta().getId());
			Assertions.assertEquals(ended.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni
			Assertions.assertEquals(ended.get(0).getPartecipazioni().get(0).getUtente(), requested.get(0).getPartecipazioni().get(0).getUtente());
			Assertions.assertEquals(ended.get(0).getPartecipazioni().get(1).getUtente(), requested.get(0).getPartecipazioni().get(1).getUtente());
			Assertions.assertEquals(ended.get(0).getPartecipazioni().get(2).getUtente(), requested.get(0).getPartecipazioni().get(2).getUtente());
			Assertions.assertEquals(ended.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte
			Assertions.assertEquals(ended.get(0).getPartecipazioni().get(0).getOfferta(), requested.get(0).getPartecipazioni().get(0).getOfferta());
			Assertions.assertEquals(ended.get(0).getPartecipazioni().get(1).getOfferta(), requested.get(0).getPartecipazioni().get(1).getOfferta());
			Assertions.assertEquals(ended.get(0).getPartecipazioni().get(2).getOfferta(), requested.get(0).getPartecipazioni().get(2).getOfferta());
			Assertions.assertEquals(ended.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
		}

		@DisplayName("Get deleted auctions ")
		@Test
		void getDeletedAuctionsTest() {
			List<Asta> deleted = AstaCreations.deletedAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.ELIMINATA)).thenReturn(deleted);

			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							deleted.get(0).getOpera(),
							deleted.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							deleted.get(0).getPartecipazioni(),
							deleted.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							deleted.get(0).getOpera().getArtista(),
							deleted.get(1).getOpera().getArtista()
			);

			int nFol0 = deleted.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = deleted.get(1).getOpera().getArtista().getnFollowers();
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

			List<Asta> requested = astaService.getAuctionsByState(Asta.Stato.ELIMINATA);

			Assertions.assertEquals(deleted.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(deleted.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(deleted.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(deleted.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(deleted.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(deleted.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(deleted.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(deleted.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(deleted.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(deleted.get(1).getOpera(), requested.get(1).getOpera());

			// Nessuna partecipazione per la prima asta
			Assertions.assertEquals(deleted.get(0).getPartecipazioni().size(), requested.get(0).getPartecipazioni().size());

			// Id aste presenti nelle partecipazioni della seconda asta
			Assertions.assertEquals(deleted.get(1).getPartecipazioni().get(0).getAsta().getId(), requested.get(1).getPartecipazioni().get(0).getAsta().getId());

			// Utenti presenti nelle partecipazioni della seconda asta
			Assertions.assertEquals(deleted.get(1).getPartecipazioni().get(0).getUtente(), requested.get(1).getPartecipazioni().get(0).getUtente());

			// Valori delle offerte della seconda asta
			Assertions.assertEquals(deleted.get(1).getPartecipazioni().get(0).getOfferta(), requested.get(1).getPartecipazioni().get(0).getOfferta());
		}

		@DisplayName("Get created auctions ")
		@Test
		void getCreatedAuctionsTest() {
			List<Asta> created = AstaCreations.createdAstaTwoElementsList();
			when(astaDao.doRetrieveByStato(Asta.Stato.CREATA)).thenReturn(created);

			when(operaDao.doRetrieveById(anyInt())).thenReturn(
							created.get(0).getOpera(),
							created.get(1).getOpera()
			);
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(
							created.get(0).getPartecipazioni(),
							created.get(1).getPartecipazioni()
			);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(
							created.get(0).getOpera().getArtista(),
							created.get(1).getOpera().getArtista()
			);

			int nFol0 = created.get(0).getOpera().getArtista().getnFollowers();
			int nFol1 = created.get(1).getOpera().getArtista().getnFollowers();
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

			List<Asta> requested = astaService.getAuctionsByState(Asta.Stato.CREATA);

			Assertions.assertEquals(created.get(0).getId(), requested.get(0).getId());
			Assertions.assertEquals(created.get(1).getId(), requested.get(1).getId());

			Assertions.assertEquals(created.get(0).getDataInizio(), requested.get(0).getDataInizio());
			Assertions.assertEquals(created.get(1).getDataInizio(), requested.get(1).getDataInizio());

			Assertions.assertEquals(created.get(0).getDataFine(), requested.get(0).getDataFine());
			Assertions.assertEquals(created.get(1).getDataFine(), requested.get(1).getDataFine());

			Assertions.assertEquals(created.get(0).getStato(), requested.get(0).getStato());
			Assertions.assertEquals(created.get(1).getStato(), requested.get(1).getStato());

			Assertions.assertEquals(created.get(0).getOpera(), requested.get(0).getOpera());
			Assertions.assertEquals(created.get(1).getOpera(), requested.get(1).getOpera());

			// Nessuna partecipazione (= 0), l'asta non è ancora in corso
			Assertions.assertEquals(created.get(0).getPartecipazioni().size(), requested.get(0).getPartecipazioni().size());
			Assertions.assertEquals(created.get(1).getPartecipazioni().size(), requested.get(1).getPartecipazioni().size());
		}
	}

	@Nested
	@DisplayName("Participate auction")
	class participateAuctionTest {

		@DisplayName("Participate in an ongoing auction with no bids and a single user and a valid offer")
		@Test
		void participateOngoingAuctionWithNoBidsAndASingleUserAndAValidOfferTest() {
			Asta asta = AstaCreations.ongoingAstaLastingSevenDaysWithNoArtistFollowersAndNoBids();

			Utente u9 = new Utente();
			u9.setId(9);
			u9.setSaldo(500d);

			double offerta = 150d;

			// lockAsta()
			// TODO: impl
			// end lockAsta()

			// recupero versione aggiornata dei dati
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(u9);
			when(astaDao.doRetrieveById(anyInt())).thenReturn(asta);

			// recupero lista partecipazioni per ricavare la migliore offerta
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(asta.getPartecipazioni());

			Partecipazione nuovaOfferta = new Partecipazione(asta, u9, offerta);

			// aggiornamento dati persistenti
			when(partecipazioneDao.doCreate(nuovaOfferta)).thenReturn(true);
			doNothing().when(utenteDao).doUpdate(u9);

			boolean result = astaService.partecipateAuction(u9, asta, offerta);

			Assertions.assertEquals(350d, u9.getSaldo());
			Assertions.assertEquals(true, result);
		}

		@DisplayName("Participate in an ongoing auction with no bids and a single user and not enough funds")
		@Test
		void participateOngoingAuctionWithNoBidsAndASingleUserAndNotEnoughFundsTest() {
			Asta asta = AstaCreations.ongoingAstaLastingSevenDaysWithNoArtistFollowersAndNoBids();

			Utente u9 = new Utente();
			u9.setId(9);
			u9.setSaldo(100d);

			double offerta = 150d;

			// lockAsta()
			// TODO: impl
			// end lockAsta()

			// recupero versione aggiornata dei dati
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(u9);
			when(astaDao.doRetrieveById(anyInt())).thenReturn(asta);

			// recupero lista partecipazioni per ricavare la migliore offerta
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(asta.getPartecipazioni());

			boolean result = astaService.partecipateAuction(u9, asta, offerta);

			Assertions.assertEquals(100d, u9.getSaldo());
			Assertions.assertEquals(false, result);
		}

		@DisplayName("Participate in an ongoing auction with a single user and one bid and a valid offer")
		@Test
		void participateOngoingAuctionWithASingleUserAndOneBidAndAValidOfferTest() {
			Asta asta = AstaCreations.ongoingAstaLastingSevenDaysWithNoArtistFollowersAndOneBid();
			Utente oldBestBidder = asta.getPartecipazioni().get(0).getUtente();

			Utente u9 = new Utente();
			u9.setId(9);
			u9.setSaldo(500d);

			double offerta = 450d;

			// lockAsta()
			// TODO: impl
			// end lockAsta()

			// recupero versione aggiornata dei dati
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(u9, oldBestBidder);
			when(astaDao.doRetrieveById(anyInt())).thenReturn(asta);

			// recupero lista partecipazioni per ricavare la migliore offerta
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(asta.getPartecipazioni());

			Partecipazione nuovaOfferta = new Partecipazione(asta, u9, offerta);

			// ripristino saldo del vecchio miglior offerente
			doNothing().when(utenteDao).doUpdate(oldBestBidder);

			Notifica notifica = new Notifica(
					oldBestBidder,
					asta,
					new Rivendita(),
					Notifica.Tipo.SUPERATO,
					"La tua offerta è stata superata.",
					false
			);

			when(notificaDao.doCreate(notifica)).thenReturn(true);

			// aggiornamento dati persistenti
			when(partecipazioneDao.doCreate(nuovaOfferta)).thenReturn(true);
			doNothing().when(utenteDao).doUpdate(u9);

			boolean result = astaService.partecipateAuction(u9, asta, offerta);

			Assertions.assertEquals(50d, u9.getSaldo());
			Assertions.assertEquals(1000d, oldBestBidder.getSaldo());
			Assertions.assertEquals(true, result);
		}

		@DisplayName("Participate in an ongoing auction with a single user and one bid and an invalid offer")
		@Test
		void participateOngoingAuctionWithASingleUserAndOneBidAndAnInvalidOfferTest() {
			Asta asta = AstaCreations.ongoingAstaLastingSevenDaysWithNoArtistFollowersAndOneBid();

			Utente u9 = new Utente();
			u9.setId(9);
			u9.setSaldo(500d);

			double offerta = 150d;

			// lockAsta()
			// TODO: impl
			// end lockAsta()

			// recupero versione aggiornata dei dati
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(u9);
			when(astaDao.doRetrieveById(anyInt())).thenReturn(asta);

			// recupero lista partecipazioni per ricavare la migliore offerta
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(asta.getPartecipazioni());

			boolean result = astaService.partecipateAuction(u9, asta, offerta);

			Assertions.assertEquals(500d, u9.getSaldo());
			Assertions.assertEquals(false, result);
		}

		@DisplayName("Participate in an ended auction")
		@Test
		void participateInAnEndedAuctionTest() {
			Asta asta = AstaCreations.endedAstaLastingSevenDaysWithNoArtistFollowersAndNoBids();

			Utente u9 = new Utente();
			u9.setId(9);
			u9.setSaldo(500d);

			double offerta = 150d;

			// lockAsta()
			// TODO: impl
			// end lockAsta()

			// recupero versione aggiornata dei dati
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(u9);
			when(astaDao.doRetrieveById(anyInt())).thenReturn(asta);

			// recupero lista partecipazioni per ricavare la migliore offerta
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(asta.getPartecipazioni());

			boolean result = astaService.partecipateAuction(u9, asta, offerta);

			Assertions.assertEquals(500d, u9.getSaldo());
			Assertions.assertEquals(false, result);
		}

		@DisplayName("Participate in a deleted auction")
		@Test
		void participateInADeletedAuctionTest() {
			Asta asta = AstaCreations.deletedAstaLastingSevenDaysWithNoArtistFollowersAndNoBids();

			Utente u9 = new Utente();
			u9.setId(9);
			u9.setSaldo(500d);

			double offerta = 150d;

			// lockAsta()
			// TODO: impl
			// end lockAsta()

			// recupero versione aggiornata dei dati
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(u9);
			when(astaDao.doRetrieveById(anyInt())).thenReturn(asta);

			// recupero lista partecipazioni per ricavare la migliore offerta
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(asta.getPartecipazioni());

			boolean result = astaService.partecipateAuction(u9, asta, offerta);

			Assertions.assertEquals(500d, u9.getSaldo());
			Assertions.assertEquals(false, result);
		}

		@DisplayName("Participate in a created auction")
		@Test
		void participateInACreatedAuctionTest() {
			Asta asta = AstaCreations.createdAstaLastingSevenDaysWithNoArtistFollowersAndNoBids();

			Utente u9 = new Utente();
			u9.setId(9);
			u9.setSaldo(500d);

			double offerta = 150d;

			// lockAsta()
			// TODO: impl
			// end lockAsta()

			// recupero versione aggiornata dei dati
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(u9);
			when(astaDao.doRetrieveById(anyInt())).thenReturn(asta);

			// recupero lista partecipazioni per ricavare la migliore offerta
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(asta.getPartecipazioni());

			boolean result = astaService.partecipateAuction(u9, asta, offerta);

			Assertions.assertEquals(500d, u9.getSaldo());
			Assertions.assertEquals(false, result);
		}

		@DisplayName("Best bidder raises his bid in an ongoing auction ")
		@Test
		void BestBidderRaisesHisBidInAnOngoingAuctionTest() {
			Asta asta = AstaCreations.ongoingAstaLastingSevenDaysWithNoArtistFollowersAndOneBid();
			Utente oldBestBidder = asta.getPartecipazioni().get(0).getUtente();

			double offerta = 450d;

			// lockAsta()
			// TODO: impl
			// end lockAsta()

			// recupero versione aggiornata dei dati
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(oldBestBidder, oldBestBidder);
			when(astaDao.doRetrieveById(anyInt())).thenReturn(asta);

			// recupero lista partecipazioni per ricavare la migliore offerta
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(asta.getPartecipazioni());

			Partecipazione nuovaOfferta = new Partecipazione(asta, oldBestBidder, offerta);

			// ripristino saldo del vecchio miglior offerente
			//doNothing().when(utenteDao).doUpdate(oldBestBidder);

			Notifica notifica = new Notifica(
							oldBestBidder,
							asta,
							new Rivendita(),
							Notifica.Tipo.SUPERATO,
							"La tua offerta è stata superata.",
							false
			);

			when(notificaDao.doCreate(notifica)).thenReturn(true);

			// aggiornamento dati persistenti
			when(partecipazioneDao.doCreate(nuovaOfferta)).thenReturn(true);
			doNothing().when(utenteDao).doUpdate(oldBestBidder);

			boolean result = astaService.partecipateAuction(oldBestBidder, asta, offerta);

			Assertions.assertEquals(550d, oldBestBidder.getSaldo());
			Assertions.assertEquals(true, result);
		}

		// TODO: test con più utenti che cercano di fare un'offerta alla stessa asta?
	}

	@Nested
	@DisplayName("Add asta")
	class addAstaTest {

	}

	@Nested
	@DisplayName("Remove asta")
	class removeAstaTest {
		@DisplayName("Remove an ongoing auction with no bids")
		@Test
		void removeAnOngoingAuctionWithNoBidsTest() {
			Asta toRemove = AstaCreations.ongoingAstaLastingSevenDaysWithNoArtistFollowersAndNoBids();
			Opera operaToRemove = toRemove.getOpera();

			// metodo privato bestOffer
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(toRemove.getPartecipazioni());

			when(operaDao.doRetrieveById(anyInt())).thenReturn(operaToRemove);
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(operaToRemove.getArtista());

			Notifica notificaPerArtista = new Notifica(
							operaToRemove.getArtista(),
							toRemove,
							new Rivendita(),
							Notifica.Tipo.ANNULLAMENTO,
							"",
							false
			);

			when(notificaDao.doCreate(notificaPerArtista)).thenReturn(true);
			doNothing().when(astaDao).doUpdate(toRemove);

			boolean result = astaService.removeAsta(toRemove);

			Assertions.assertEquals(true, result);
			Assertions.assertEquals(Asta.Stato.ELIMINATA, toRemove.getStato());
		}

		@DisplayName("Remove an ongoing auction with one bid")
		@Test
		void removeAnOngoingAuctionWithOneBidTest() {
			Asta toRemove = AstaCreations.ongoingAstaLastingSevenDaysWithNoArtistFollowersAndOneBid();
			Opera operaToRemove = toRemove.getOpera();
			Utente bestBidder = toRemove.getPartecipazioni().get(0).getUtente();

			// metodo privato bestOffer
			when(partecipazioneDao.doRetrieveAllByAuctionId(anyInt())).thenReturn(toRemove.getPartecipazioni());

			// metodo asta annullata (utenteDao prima cerca il miglior offerente e poi l'artista che ha creato
			// l'asta per generare le notifiche)
			when(utenteDao.doRetrieveById(anyInt())).thenReturn(bestBidder, operaToRemove.getArtista());
			doNothing().when(utenteDao).doUpdate(bestBidder);

			Notifica notificaPerMigliorOfferente = new Notifica(
							bestBidder,
							toRemove,
							new Rivendita(),
							Notifica.Tipo.ANNULLAMENTO,
							"",
							false
			);

			when(notificaDao.doCreate(notificaPerMigliorOfferente)).thenReturn(true);
			when(operaDao.doRetrieveById(anyInt())).thenReturn(operaToRemove);
			doNothing().when(operaDao).doUpdate(operaToRemove);

			when(operaDao.doRetrieveById(anyInt())).thenReturn(operaToRemove);

			Notifica notificaPerArtista = new Notifica(
							operaToRemove.getArtista(),
							toRemove,
							new Rivendita(),
							Notifica.Tipo.ANNULLAMENTO,
							"",
							false
			);

			when(notificaDao.doCreate(notificaPerArtista)).thenReturn(true);
			doNothing().when(astaDao).doUpdate(toRemove);

			boolean result = astaService.removeAsta(toRemove);

			Assertions.assertEquals(true, result);
			Assertions.assertEquals(Asta.Stato.ELIMINATA, toRemove.getStato());
			Assertions.assertEquals(Opera.Stato.PREVENDITA, toRemove.getOpera().getStato());
		}
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

	// Non vale la pena testare il metodo getAllOffers(),
	// non fa altro che restituire il risultato del metodo
	// doRetrieveAll di partecipazioneDao.
	/*
	@Nested
	@DisplayName("Get all offers")
	class getAllOffersTest {

	}
	*/

	@Nested
	@DisplayName("Execute timed tasks")
	class executeTimedTasksTest {

	}
}
