package dev.appkr.demo.tcp.tcpExample;

import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import dev.appkr.demo.tcp.config.TcpClientProperties;
import dev.appkr.demo.tcp.visitor.Parser;
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
}