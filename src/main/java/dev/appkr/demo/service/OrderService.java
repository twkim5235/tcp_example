package dev.appkr.demo.service;

import static dev.appkr.demo.service.OrderTcpMessage.ROOT_PACKET_NAME;

import dev.appkr.demo.service.dto.PlaceOrderRequest;
import dev.appkr.demo.service.dto.PlaceOrderResponse;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.client.TcpClient;
import dev.appkr.demo.tcp.visitor.Formatter;
import dev.appkr.demo.tcp.visitor.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

  private final TcpClient tcpClient;
  private final Formatter formatter;
  private final Parser parser;

  public OrderService(TcpClient tcpClient,
      @Qualifier("defaultFormatter") Formatter formatter,
      @Qualifier("responseParser") Parser parser) {
    this.tcpClient = tcpClient;
    this.formatter = formatter;
    this.parser = parser;
  }

  public PlaceOrderResponse placeOrder(PlaceOrderRequest req) {
    Packet reqPacket;
    Packet resPacket = new Packet(ROOT_PACKET_NAME);
    try {
      // PlaceOrderRequest를 byte[]로 직렬화한다
      reqPacket = req.toPacket();
      reqPacket.accept(formatter);

      // 전송한다
      tcpClient.write(reqPacket.getTcpMessage());

      // 수신한다
      final byte[] res = tcpClient.read();

      // 수신된 메시지를 파싱하여 Packet으로 역직렬화한다
      resPacket = new Packet(ROOT_PACKET_NAME, res);
      resPacket.accept(parser);

      log.info("response: {}", resPacket.toMap());
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    return PlaceOrderResponse.fromPacket(resPacket);
  }
}
