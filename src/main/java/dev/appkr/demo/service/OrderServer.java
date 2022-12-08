package dev.appkr.demo.service;

import static dev.appkr.demo.service.OrderTcpMessage.DEFAULT_CHARSET;

import dev.appkr.demo.service.OrderTcpMessage.RequestField;
import dev.appkr.demo.service.OrderTcpMessage.ResponseField;
import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.config.TcpServerProperties;
import dev.appkr.demo.tcp.server.AbstractTcpServer;
import dev.appkr.demo.tcp.visitor.Formatter;
import dev.appkr.demo.tcp.visitor.Parser;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderServer extends AbstractTcpServer {

  final Formatter formatter;
  final Parser parser;

  public OrderServer(TcpServerProperties properties,
      Formatter formatter,
      @Qualifier("requestParser") Parser parser) {
    super(properties);
    this.formatter = formatter;
    this.parser = parser;
  }

  @SneakyThrows
  @Override
  public void handleMessage(BufferedReader reader, PrintWriter writer) {
    // 요청 메시지를 읽는다
    final String message = reader.readLine();

    // 요청 메시지를 파싱한다
    final Packet requestPacket =
        new Packet(RequestField.PACKET_NAME, message.getBytes(DEFAULT_CHARSET));
    requestPacket.accept(parser);
    log.info("request: {}", requestPacket.toMap());

    // 응답 메시지를 만든다
    Packet responsePacket = new Packet(ResponseField.PACKET_NAME);
    responsePacket.add(Item.of(ResponseField.ORDER_ID, 0, UUID.randomUUID().toString()));
    responsePacket.add(Item.of(ResponseField.RESULT, 1, "SUCCESS"));

    // 응답 메시지를 포맷팅한다
    responsePacket.accept(formatter);

    // 응답 메시지를 쓴다
    // PrintWriter를 쓰고 있으므로, String으로 다시 치환해야 함
    final String responseMessage = new String(responsePacket.getTcpMessage(), DEFAULT_CHARSET);
    writer.println(responseMessage);
  }
}
