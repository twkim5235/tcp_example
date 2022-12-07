package dev.appkr.demo.service;

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
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderServer extends AbstractTcpServer {

  final Formatter formatter;
  final Parser parser;

  public OrderServer(TcpServerProperties properties, Formatter formatter, Parser parser) {
    super(properties);
    this.formatter = formatter;
    this.parser = parser;
  }

  @SneakyThrows
  @Override
  public void handleMessage(BufferedReader reader, PrintWriter writer) {
    final String message = reader.readLine();

    final Packet requestPacket =
        new Packet("request", message.getBytes(Constants.DEFAULT_CHARSET));
    requestPacket.accept(parser);
    log.info("request: {}", requestPacket.toMap());

    Packet responsePacket = new Packet("response");
    responsePacket.add(Item.of("orderId", 0, UUID.randomUUID().toString()));
    responsePacket.add(Item.of("result", 1, "SUCCESS"));

    responsePacket.accept(formatter);

    writer.println(responsePacket.getTcpMessage());
  }
}
