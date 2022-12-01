package dev.appkr.demo.tcp.client;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.config.TcpClientProperties;
import dev.appkr.demo.tcp.tcpExample.PipeByteFormatter;
import dev.appkr.demo.tcp.tcpExample.PipeByteParser;
import dev.appkr.demo.tcp.tcpExample.PipeByteResponseTcpMessageTemplateFactory;
import dev.appkr.demo.tcp.visitor.Formatter;
import dev.appkr.demo.tcp.visitor.Parser;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class TcpClientPipeMessageTest {

  final TcpClientProperties properties = new TcpClientProperties();
  final Formatter formatter = new PipeByteFormatter();
  final Parser parser = new PipeByteParser(new PipeByteResponseTcpMessageTemplateFactory());
  final TcpClient client = new DisposableTcpClient(properties.getHost(), properties.getPort(),
      properties.getCharset());

  @Test
  void disposableTcpClient() throws Exception {
    final Packet packet = createPacket();
    packet.accept(formatter);
    client.write(packet.getTcpMessage());
    final Packet response = new Packet("root", client.read());
    response.accept(parser);

    Assertions.assertThat(response.getMessageComponents().get(1).getValue()).isEqualTo("SUCCESS");
  }

  private Packet createPacket() {
    final Packet rootPacket = new Packet("root");
    rootPacket.add(Item.of("ordererName", 1, "김메쉬"));
    rootPacket.add(Item.of("ordererPhone", 2, "010-1234-5678"));
    rootPacket.add(Item.of("receiverName", 3, "홍길동"));
    rootPacket.add(Item.of("receiverPhone", 4, "010-4321-8765"));
    rootPacket.add(Item.of("receiverAddress", 5, "서울특별시 강남구 대치동 890-12 다봉타워 13층"));

    Packet subPacket = new Packet("products");
    subPacket.add(Item.of("productCode", 6, "68d8a5d5-2860-4a0c-bf64-3c879cf3cf61"));
    subPacket.add(Item.of("productName", 7, "지포라이터"));
    subPacket.add(Item.of("unitPrice", 8, "50000"));
    subPacket.add(Item.of("quantity", 9, "1"));

    subPacket.add(Item.of("productCode", 10, "864bc23c-59eb-4a55-b68e-f3cbc9700315"));
    subPacket.add(Item.of("productName", 11, "지포라이터 부싯돌 셋트"));
    subPacket.add(Item.of("unitPrice", 12, "5000"));
    subPacket.add(Item.of("quantity", 13, "1"));
    rootPacket.add(subPacket);

    return rootPacket;
  }

}
