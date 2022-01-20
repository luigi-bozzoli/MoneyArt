package unit.gestione.service;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.gestione.utente.service.UtenteServiceImpl;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

    static List<Utente> getUtenti () {

        Utente u1 = new Utente("Mario Seguito", "Peluso", null, "marioseguitopeluso@unisa.it",
                "mpelGYUgugyugYUG", null, new byte[10], 2000000.2);
        u1.setId(1);

        Utente u2 = new Utente("Luigi", "Bros", null, "luigibros@unisa.it",
                "vuivuovuuGYUGIY", u1, new byte[10], 2.2);
        u2.setId(2);

        Utente u3 = new Utente("Stefano", "Zarro", null, "stefanus@unisa.it",
                "vghuivuiVGHUIVUI", u1, new byte[10], 0.002);
        u3.setId(3);

        List<Utente> utenti = Arrays.asList(u1, u2, u3);

        return utenti;
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

    @Nested
    @DisplayName("Test Suite checkUser")
    class testCheckUser {

        @DisplayName("Check User")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUser(Utente utente) {
            String pwdOrigin = "gattoPardo";
            utente.setPassword(encryptPassword(pwdOrigin));

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(utente);

            Utente result = utenteService.checkUser(utente.getUsername(), pwdOrigin);

            assertEquals(result, utente);

        }

        @DisplayName("checkUserUsernameNull")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUserUsernameNull(Utente utente) {

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(utente);

            assertThrows(IllegalArgumentException.class, () -> utenteService.checkUser(null, utente.getPassword().toString()));
        }

        @DisplayName("checkUserPasswordNull")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUserPasswordNull(Utente utente) {
            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(utente);

           assertThrows(IllegalArgumentException.class, () -> utenteService.checkUser(utente.getUsername(), null));

        }

        @DisplayName("CheckUserErr")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUserErr(Utente utente) {

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(utente);

            Utente result = utenteService.checkUser(utente.getUsername(), utente.getPassword().toString());

            assertNull(result);

        }

        public byte[] encryptPassword(String password) {
            if (password == null) {
                throw new IllegalArgumentException("Password is null");
            }

            byte[] pswC = null;
            try {
                MessageDigest criptarino = MessageDigest.getInstance("SHA-256");
                pswC = criptarino.digest(password.getBytes());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return pswC;
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

        @DisplayName("getUserInformationByUsernameErr")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void getUserInformationByUsernameErr(Utente utente) {
            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(null);
            assertNull(utenteService.getUserInformation(utente.getUsername()));
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

        @DisplayName("getUserInformationByIdErr")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void getUserInformationByIdErr(Utente utente) {
            when(utenteDao.doRetrieveById(anyInt())).thenReturn(null);
            assertNull(utenteService.getUserInformation(utente.getId()));
        }

    }

    @Nested
    @DisplayName("Test Suite SignUpUser")
    class testSignUpUser {

        @DisplayName("signUpUser")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void signUpUser(Utente utente) {
            when(utenteDao.doCreate(any())).thenReturn(true);

            assertTrue(utenteService.signUpUser(utente));
        }

        @DisplayName("signUpUserErr")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void signUpUserErr(Utente utente) {
            when(utenteDao.doCreate(any())).thenReturn(false);
            assertTrue(!(utenteService.signUpUser(utente)));
        }
    }


    @Nested
    @DisplayName("Test Suite getAllUsers")
    class testGetAllUsers {

        @DisplayName("getAllUsers")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void getAllUsers() {
            List<Utente> utenti = UtenteServiceImplUnitTest.getUtenti();

            when(utenteDao.doRetrieveAll(anyString())).thenReturn(utenti);

            when(operaDao.doRetrieveAllByOwnerId(anyInt())).thenReturn(null);
            when(operaDao.doRetrieveAllByArtistId(anyInt())).thenReturn(null);
            when(notificaDao.doRetrieveAllByUserId(anyInt())).thenReturn(null);
            when(partecipazioneDao.doRetrieveAllByUserId(anyInt())).thenReturn(null);

           when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(casualFollowers(utenti.get(0)));

           assertEquals(utenti, utenteService.getAllUsers());
        }

        @DisplayName("getAllUsersErr")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void getAllUsersErr() {
            List<Utente> utenti = null;

            when(utenteDao.doRetrieveAll(anyString())).thenReturn(utenti);

            when(operaDao.doRetrieveAllByOwnerId(anyInt())).thenReturn(null);
            when(operaDao.doRetrieveAllByArtistId(anyInt())).thenReturn(null);
            when(notificaDao.doRetrieveAllByUserId(anyInt())).thenReturn(null);
            when(partecipazioneDao.doRetrieveAllByUserId(anyInt())).thenReturn(null);

            when(utenteDao.doRetrieveFollowersByUserId(anyInt())).thenReturn(null);

            assertNull(utenteService.getAllUsers());
        }

    }

    @Nested
    @DisplayName("Test Suite searchUsers")
    class testSearchUsers{

        @DisplayName("searchUsers")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void searchUsers() {
            List<Utente> utenti = getUtenti();
            when(utenteDao.researchUser(anyString())).thenReturn(getUtenti());
            assertEquals(utenti, utenteService.searchUsers("users"));

        }

        @DisplayName("searchUsersErr")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void searchUsersErr() {
            List<Utente> utenti = getUtenti();
            when(utenteDao.researchUser(anyString())).thenReturn(null);
            assertTrue(!(utenti.equals(utenteService.searchUsers("users"))));

        }

        @DisplayName("searchUsersErrCatch")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void searchUsersCatch() {
            when(utenteDao.researchUser(anyString())).thenReturn(getUtenti());

            assertThrows(IllegalArgumentException.class, () -> utenteService.searchUsers(null));

        }

    }

    @Nested
    @DisplayName("Test Suite testCheckUsername")
    class testCheckUsername{

        @DisplayName("checkUsername")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUsername(Utente utente) {
            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            assertTrue(utenteService.checkUsername(utente.getUsername()));

        }

        @DisplayName("checkUsernameCatch")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUsernameCatch(Utente utente) {
            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            assertThrows(IllegalArgumentException.class, () -> utenteService.checkUsername(null));

        }

        @DisplayName("checkUsernameErr")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkUsernameErr(Utente utente) {
            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(null);
            assertTrue(!(utenteService.checkUsername(utente.getUsername())));

        }

    }

    @Nested
    @DisplayName("Test Suite testCheckEmail")
    class testCheckEmail{

        @DisplayName("checkEmail")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkEmail(Utente utente) {
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(utente);
            assertTrue(utenteService.checkEmail(utente.getEmail()));

        }

        @DisplayName("checkEmailCatch")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkEmailCatch(Utente utente) {
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(utente);
            assertThrows(IllegalArgumentException.class, () -> utenteService.checkEmail(null));

        }

        @DisplayName("checkEmailErr")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void checkEmailErr(Utente utente) {
            when(utenteDao.doRetrieveByEmail(anyString())).thenReturn(null);
            assertTrue(!(utenteService.checkEmail(utente.getEmail())));

        }

    }


    @Nested
    @DisplayName("Test Suite testFollow")
    class testFollow{

        @Test
        @DisplayName("follow")
        void follow() {

            Utente followed = new Utente("Mario Seguito", "Peluso", null, "marioseguitopeluso@unisa.it",
                    "mpelGYUgugyugYUG", null, new byte[10], 2000000.2);
            followed.setId(1);

            Utente follower = new Utente("Luigi", "Bros", null, "luigibros@unisa.it",
                    "vuivuovuuGYUGIY", null, new byte[10], 2.2);
            follower.setId(2);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(followed);
            doNothing().when(utenteDao).doUpdate(any());

            assertTrue(utenteService.follow(follower, followed));

        }

        @Test
        @DisplayName("followCatch1")
        void followCatch1() {

            Utente followed = new Utente("Mario Seguito", "Peluso", null, "marioseguitopeluso@unisa.it",
                    "mpelGYUgugyugYUG", null, new byte[10], 2000000.2);
            followed.setId(1);

            Utente follower = new Utente("Luigi", "Bros", null, "luigibros@unisa.it",
                    "vuivuovuuGYUGIY", null, new byte[10], 2.2);
            follower.setId(2);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(followed);
            doNothing().when(utenteDao).doUpdate(any());

           assertThrows(IllegalArgumentException.class, () -> utenteService.follow(null, followed));

        }

        @Test
        @DisplayName("followCatch2")
        void followCatch2() {

            Utente followed = new Utente("Mario Seguito", "Peluso", null, "marioseguitopeluso@unisa.it",
                    "mpelGYUgugyugYUG", null, new byte[10], 2000000.2);
            followed.setId(1);

            Utente follower = new Utente("Luigi", "Bros", null, "luigibros@unisa.it",
                    "vuivuovuuGYUGIY", null, new byte[10], 2.2);
            follower.setId(2);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(followed);
            doNothing().when(utenteDao).doUpdate(any());

            assertThrows(IllegalArgumentException.class, () -> utenteService.follow(follower, null));

        }

        @Test
        @DisplayName("followCatch3")
        void followCatch3() {

            Utente followed = new Utente("Mario Seguito", "Peluso", null, "marioseguitopeluso@unisa.it",
                    "mpelGYUgugyugYUG", null, new byte[10], 2000000.2);
            followed.setId(1);

            Utente follower = new Utente("Luigi", "Bros", null, "luigibros@unisa.it",
                    "vuivuovuuGYUGIY", null, new byte[10], 2.2);
            follower.setId(2);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(null);
            doNothing().when(utenteDao).doUpdate(any());

            assertThrows(IllegalArgumentException.class, () -> utenteService.follow(follower, followed));

        }

        @Test
        @DisplayName("followCatchErr")
        void followCatchErr() {

            Utente followed = new Utente("Mario Seguito", "Peluso", null, "marioseguitopeluso@unisa.it",
                    "mpelGYUgugyugYUG", null, new byte[10], 2000000.2);
            followed.setId(1);

            Utente follower = new Utente("Luigi", "Bros", null, "luigibros@unisa.it",
                    "vuivuovuuGYUGIY", null, new byte[10], 2.2);
            follower.setId(2);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(followed);
            follower.setSeguito(getUtenti().get(0)); //il follower segue già un'altro artista
            doNothing().when(utenteDao).doUpdate(any());

            assertTrue(!(utenteService.follow(follower, followed)));

        }

    }


    @Nested
    @DisplayName("Test Suite testDeposit")
    class testDeposit{

        @DisplayName("deposit")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void deposit(Utente utente) {
            double amount = 1689.85;

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            doNothing().when(utenteDao).doUpdate(any());

            assertTrue(utenteService.deposit(utente, amount));

        }

        @DisplayName("depositCatch1")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void depositCatch1(Utente utente) {
            double amount = 1689.85;

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            doNothing().when(utenteDao).doUpdate(any());

            assertThrows(IllegalArgumentException.class, () -> utenteService.deposit(null, amount));

        }

        @DisplayName("depositCatch2")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void depositCatch2(Utente utente) {
            double amount = -1689.85;

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            doNothing().when(utenteDao).doUpdate(any());

            assertThrows(IllegalArgumentException.class, () -> utenteService.deposit(utente, amount));

        }

        @DisplayName("depositCatchErr")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void depositCatchErr(Utente utente) {
            double amount = 1689.85;

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(null);
            doNothing().when(utenteDao).doUpdate(any());

            assertTrue(!(utenteService.deposit(utente, amount)));

        }

    }


    @Nested
    @DisplayName("Test Suite testWithdraw")
    class testWithdraw{


        @DisplayName("withdraw")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void withdraw(Utente utente) {
            Double amount = utente.getSaldo() - (utente.getSaldo()-1) ;
            utente.setSaldo(amount);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            doNothing().when(utenteDao).doUpdate(any());

            assertTrue(utenteService.withdraw(utente, amount));

        }

        @DisplayName("withdrawCatch")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void withdrawCatch(Utente utente) {
            Double amount = utente.getSaldo() - (utente.getSaldo()-1) ;
            utente.setSaldo(amount);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            doNothing().when(utenteDao).doUpdate(any());

            assertThrows(IllegalArgumentException.class, () -> utenteService.withdraw(null, amount));

        }

        @DisplayName("withdrawErr1")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void withdrawErr1(Utente utente) {
            Double amount = utente.getSaldo() - (utente.getSaldo()-1) ;
            utente.setSaldo(amount);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(null);
            doNothing().when(utenteDao).doUpdate(any());

            assertTrue(!(utenteService.withdraw(utente, amount)));

        }

        @DisplayName("withdrawErr2")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void withdrawErr2(Utente utente) {
            Double amount = -11.01 ;
            utente.setSaldo(amount);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            doNothing().when(utenteDao).doUpdate(any());

            assertTrue(!(utenteService.withdraw(utente, amount)));

        }

        @DisplayName("withdrawErr3")
        @ParameterizedTest
        @ArgumentsSource(UtenteProvider.class)
        void withdrawErr3(Utente utente) {
            Double amount = utente.getSaldo() - (utente.getSaldo()+1) ; //amount > saldo
            utente.setSaldo(amount);

            when(utenteDao.doRetrieveByUsername(anyString())).thenReturn(utente);
            doNothing().when(utenteDao).doUpdate(any());

            assertTrue(!(utenteService.withdraw(utente, amount)));

        }


    }


}

