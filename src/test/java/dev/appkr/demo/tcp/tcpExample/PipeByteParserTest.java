package dev.appkr.demo.tcp.tcpExample;

import static org.junit.jupiter.api.Assertions.*;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import dev.appkr.demo.tcp.config.TcpClientProperties;
import dev.appkr.demo.tcp.visitor.Parser;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class PipeByteParserTest {

  final TcpClientProperties properties = new TcpClientProperties();

  @Test
  public void parse() throws Exception {
    //given
    final String tcpMessage = "김메쉬|010-1234-5678|홍길동|010-4321-8765|서울특별시 강남구 대치동 890-12 다봉타워 13층|68d8a5d5-2860-4a0c-bf64-3c879cf3cf61|지포라이터|50000|1|864bc23c-59eb-4a55-b68e-f3cbc9700315|지포라이터 부싯돌 셋트|5000|1|";

    //when
    final Packet packet = new Packet("root", tcpMessage.getBytes(properties.getCharset()));
    final TcpMessageTemplateFactory templateFactory = new PipeByteTcpMessageTemplateFactory();
    final Parser parser = new PipeByteParser(templateFactory);
    packet.accept(parser);

    //then
    log.info("{}", packet.toMap());
  }

  static class PipeByteTcpMessageTemplateFactory implements TcpMessageTemplateFactory {

    @Override
    public List<TcpMessage> create(byte[] tcpMessage, Charset charset) {
      final List<TcpMessage> components = new ArrayList<>();
      components.add(Item.of("ordererName", 1));
      components.add(Item.of("ordererPhone", 2));
      components.add(Item.of("receiverName", 3));
      components.add(Item.of("receiverPhone", 4));
      components.add(Item.of("receiverAddress", 5));

      final int noOfSubPacket = (getNoPipeline(tcpMessage, charset) - 5) / 4;
      Packet subPacket = new Packet("products");
      for (int i = 0; i < noOfSubPacket; i++) {
        subPacket.add(Item.of("productCode", 6 + (i * 4)));
        subPacket.add(Item.of("productName", 7 + (i * 4)));
        subPacket.add(Item.of("unitPrice", 8 + (i * 4)));
        subPacket.add(Item.of("quantity", 9 + (i * 4)));
      }
      components.add(subPacket);

      return components;
    }

    public int getNoPipeline(byte[] tcpMessage, Charset charset) {
      String message = new String(tcpMessage, charset);
      return message.length() - message.replace("|", "").length();
    }
  }
}