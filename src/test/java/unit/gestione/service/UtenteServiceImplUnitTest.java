package unit.gestione.service;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteServiceImpl;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.sql.rowset.serial.SerialBlob;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UtenteServiceImplUnitTest {

    @Mock
    private UtenteDao utenteDao;
    @Mock
    private OperaDao operaDao;
    @Mock
    private NotificaDao notificaDao;
    @Mock
    private PartecipazioneDao partecipazioneDao;


    private UtenteService utenteService;

    @BeforeEach
    void setUp() {
        utenteService = new UtenteServiceImpl(utenteDao, operaDao, notificaDao, partecipazioneDao);
    }

    @AfterEach
    void tearDown() {
    }

    //restituiscono i parametri per i test
    static class UtenteProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

            Utente u1 = new Utente("Mario Seguito", "Peluso", null, "marioseguitopeluso@unisa.it",
                    "mpelGYUgugyugYUG", null, new byte[10], 2000000.2);
            u1.setId(1);

            Utente u2 = new Utente("Luigi", "Bros", null, "luigibros@unisa.it",
                    "vuivuovuuGYUGIY", u1, new byte[10], 2.2);
            u2.setId(2);

            Utente u3 = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
                    "vghuivuiVGHUIVUI", u1, new byte[10], 0.002);
            u3.setId(3);

            return Stream.of(
                    Arguments.of(u1), //ognuno di questi rappresenta un input istanziato per un metodo di test
                    Arguments.of(u2),
                    Arguments.of(u3)
            );
        }
    }

    static class OperaProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

            String s1 = "image blob 1";
            Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
            Utente u1 = new Utente();
            u1.setId(1);
            Opera o1 = new Opera("The Shibosis", "Descrizione", Opera.Stato.PREVENDITA,
                    b1, u1, u1, "xxxxx");
            o1.setId(1);

            String s2 = "image blob 2";
            Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
            Utente u2 = new Utente();
            u2.setId(2);
            Opera o2 = new Opera("The Shibosis 2", "Descrizione", Opera.Stato.PREVENDITA,
                    b2, u2, u2, "yyyyyyyyy");
            o2.setId(2);

            String s3 = "image blob 3";
            Blob b3 = new SerialBlob(s3.getBytes(StandardCharsets.UTF_8));
            Utente u3 = new Utente();
            u3.setId(3);
            Opera o3 = new Opera("PIXELARTARTARTA", "Descrizione", Opera.Stato.PREVENDITA,
                    b3, u3, u3, "zzzzzz");
            o3.setId(3);

            return Stream.of(
                    Arguments.of(o1), //ognuno di questi rappresenta un input istanziato per un metodo di test
                    Arguments.of(o2),
                    Arguments.of(o3)
            );

        }
    }



    //ritorna una lista di opere (può servire per esempio per le opere di un utente)
    static class ListOpereProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            List<Opera> opere = getListOpere();

            return Stream.of(Arguments.of(opere));
        }

        static List<Opera> getListOpere() throws SQLException {
            String s1 = "image blob 1";
            Blob b1 = new SerialBlob(s1.getBytes(StandardCharsets.UTF_8));
            Utente u1 = new Utente();
            u1.setId(1);
            Opera o1 = new Opera("The Shibosis", "Descrizione", Opera.Stato.PREVENDITA,
                    b1, u1, u1, "xxxxx");
            o1.setId(1);

            String s2 = "image blob 2";
            Blob b2 = new SerialBlob(s2.getBytes(StandardCharsets.UTF_8));
            Utente u2 = new Utente();
            u2.setId(2);
            Opera o2 = new Opera("The Shibosis 2", "Descrizione", Opera.Stato.PREVENDITA,
                    b2, u2, u1, "yyyyyyyyy");
            o2.setId(2);

            String s3 = "image blob 3";
            Blob b3 = new SerialBlob(s3.getBytes(StandardCharsets.UTF_8));
            Utente u3 = new Utente();
            u3.setId(3);
            Opera o3 = new Opera("PIXELARTARTARTA", "Descrizione", Opera.Stato.PREVENDITA,
                    b3, u3, u1, "zzzzzz");
            o3.setId(3);

            List<Opera> opere = new ArrayList<>();
            opere.add(o1);
            opere.add(o2);
            opere.add(o3);

            return opere;
        }

    }




    @Nested
    @DisplayName("Test Suite checkUser")
    class testCheckUser {

        @DisplayName("Check User")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUser(Utente utente) {

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(utente);

            Utente result = utenteService.checkUser(utente.getUsername(), utente.getPassword().toString());
            //...impl
            System.out.println(result);
            System.out.println(utente);
            assertTrue(result.getId() == utente.getId());
        }

        @DisplayName("checkUserUsernameNull")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUserUsernameNull(Utente utente) {
            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(null);
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(null);

            Utente result = utenteService.checkUser(utente.getUsername(), utente.getPassword().toString());

            System.out.println(result);
            System.out.println(utente);
            assertNull(result);
        }

        @DisplayName("checkUserPasswordNull")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUserPasswordNull(Utente utente) {
            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(utente);

            Utente result = utenteService.checkUser(utente.getUsername(), null);

            System.out.println(result);
            System.out.println(utente);
            assertNull(result);
        }

    }


    @Nested
    @DisplayName("Test Suite GetUserInformation")
    class testGetUserInformation {

        @DisplayName("getUserInformationByUsername")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void getUserInformationByUsername(Utente utente) {

            utente.setSeguito(null);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            if (utente.getSeguito()!=null){
                if (utente.getSeguito().getId() != null){
                    when(utenteDao.doRetrieveById(anyInt())).thenReturn(null);
                }
            }

            when(operaDao.doRetrieveAllByOwnerId(anyInt())).thenReturn(null);
            when(operaDao.doRetrieveAllByArtistId(anyInt())).thenReturn(null);
            when(notificaDao.doRetrieveAllByUserId(anyInt())).thenReturn(null);
            when(partecipazioneDao.doRetrieveAllByUserId(anyInt())).thenReturn(null);

            when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(casualFollowers(utente));

            System.out.println(utente+"\nfollowers:\n"+casualFollowers(utente));

            assertTrue(utenteService.getUserInformation(utente.getUsername()).getId()==utente.getId());

        }

        //ritorna una lista casuale di 2 utenti che seguono l'utente passato in input
        List<Utente> casualFollowers (Utente followed){
            Utente foll1 = new Utente("Giacomo", "Lancuso", null, "giacom34@unisa.it",
                    "gLancus", null, new byte[10], 0.2);
            Utente foll2 = new Utente("Massie", "Wood", null, "m.wood23@unisa.it",
                    "massieWoooo", null, new byte[10], 22.34);

            foll1.setSeguito(followed);
            foll2.setSeguito(followed);

            List<Utente> followers = Arrays.asList(foll1, foll2);
            return followers;
        }

        @DisplayName("getUserInformationById")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void getUserInformationById(Utente utente) {

            utente.setSeguito(null);

            when(utenteDao.doRetrieveById(anyInt())).thenReturn(utente);
            if (utente.getSeguito()!=null){
                if (utente.getSeguito().getId() != null){
                    when(utenteDao.doRetrieveById(anyInt())).thenReturn(null);
                }
            }

            when(operaDao.doRetrieveAllByOwnerId(anyInt())).thenReturn(null);
            when(operaDao.doRetrieveAllByArtistId(anyInt())).thenReturn(null);
            when(notificaDao.doRetrieveAllByUserId(anyInt())).thenReturn(null);
            when(partecipazioneDao.doRetrieveAllByUserId(anyInt())).thenReturn(null);

            when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(casualFollowers(utente));

            System.out.println(utente+"\nfollowers:\n"+casualFollowers(utente));

            assertTrue(utenteService.getUserInformation(utente.getId()).getId()==utente.getId());

        }


    }


}



/*
    @Test
    void getUserInformation() {
    }

    @Test
    void testGetUserInformation() {
    }

    @Test
    void signUpUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUsersSortedByFollowers() {
    }

    @Test
    void searchUsers() {
    }

    @Test
    void checkUsername() {
    }

    @Test
    void checkEmail() {
    }

    @Test
    void follow() {
    }

    @Test
    void unfollow() {
    }

    @Test
    void deposit() {
    }

    @Test
    void withdraw() {
    }

    @Test
    void getBalance() {
    }

    @Test
    void encryptPassword() {
    }

    */
