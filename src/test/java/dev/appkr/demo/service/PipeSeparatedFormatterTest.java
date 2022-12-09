package dev.appkr.demo.service;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.visitor.Formatter;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PipeSeparatedFormatterTest {

  static final String ORDER_ID = "12345678";
  static final String ADDRESS = "서울특별시 강남구 대치동";

  @Test
  public void format() throws Exception {
    final Formatter formatter = new PipeSeparatedFormatter();
    Packet packet = new Packet("root");
    packet.add(Item.of("orderId", 0, ORDER_ID));
    packet.add(Item.of("address", 1, ADDRESS));

    formatter.format(packet);

    byte[] tcpMessage = packet.getTcpMessage();
    Assertions.assertThat(new String(tcpMessage, StandardCharsets.UTF_8))
        .isEqualTo(ORDER_ID + "|" + ADDRESS);
  }
}