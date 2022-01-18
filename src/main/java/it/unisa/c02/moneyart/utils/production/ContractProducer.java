package it.unisa.c02.moneyart.utils.production;

import it.unisa.c02.moneyart.model.blockchain.MoneyArtNft;
import java.math.BigInteger;
import javax.enterprise.inject.Produces;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

/**
 * Classe che modella un contratto.
 *
 */
public class ContractProducer {

  /**
   * Inizializzazione di un contratto.
   *
   * @return di un contratto
   */
  @Produces
  @Sing
  public MoneyArtNft contractInizializer() {
    Web3j web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));

    ContractGasProvider contractGasProvider = getGasProvider();

    Credentials credentials = getCredentialsFromPrivateKey();

    return loadContract(CONTRACT_ADDRESS, web3j, credentials, contractGasProvider);

  }

  private static ContractGasProvider getGasProvider() {
    ContractGasProvider contractGasProvider = new StaticGasProvider(GAS_PRICE, GAS_LIMIT);
    return contractGasProvider;
  }

  private static Credentials getCredentialsFromPrivateKey() {
    return Credentials.create(PRIVATE_KEY);
  }

  private static MoneyArtNft loadContract(String contractAddress, Web3j web3j,
                                          Credentials credentials,
                                          ContractGasProvider contractGasProvider) {
    return MoneyArtNft.load(contractAddress, web3j, credentials, contractGasProvider);
  }

  private static final String PRIVATE_KEY =
      "d53e4c1d9fb9ae4748924a1b2fd6396ba5de7bab31bd686c1c871ce1e9f51d28";

  private static final BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
  private static final BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
  private static final String CONTRACT_ADDRESS = "0xC33918f93E9F46Ef4366ebfa84C3dA8C10AB9ec6";
}
