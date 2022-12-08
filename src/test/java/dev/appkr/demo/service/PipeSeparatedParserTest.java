package dev.appkr.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.visitor.Parser;
import org.junit.jupiter.api.Test;

class PipeSeparatedParserTest {

  @Test
  public void parse() throws Exception {
    //given
    Parser parser = new PipeSeparatedParser(new PipeSeparatedResponseTemplateFactory());
    byte[] responseBytes = OrderFixtures.aResponseBytes();
    Packet responsePacket = new Packet("response", responseBytes);

    //when
    parser.parse(responsePacket);

    //then
    assertThat(responsePacket.toMap().get("orderId").getValue())
        .isEqualTo(OrderFixtures.DUMMY_ORDER_ID);
    assertThat(responsePacket.toMap().get("result").getValue())
        .isEqualTo(OrderFixtures.DUMMY_RESULT);
  }
}
