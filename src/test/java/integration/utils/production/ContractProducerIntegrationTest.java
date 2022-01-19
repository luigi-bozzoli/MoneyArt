package integration.utils.production;

import static org.junit.jupiter.api.Assertions.*;

import it.unisa.c02.moneyart.utils.production.ContractProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContractProducerIntegrationTest {

  private ContractProducer contractProducer;

  @BeforeEach
  public void setUp(){

    contractProducer = new ContractProducer();
  }

  @Test
  void contractInizializer() {
    Assertions.assertNotNull(contractProducer.contractInizializer());
  }
}