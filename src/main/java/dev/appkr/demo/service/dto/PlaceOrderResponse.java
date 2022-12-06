package dev.appkr.demo.service.dto;

import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceOrderResponse {

  private String orderId;
  private String result;

  public static PlaceOrderResponse fromPacket(Packet packet) {
    final Map<String, TcpMessage> map = packet.toMap();
    final String orderId = map.get("orderId").getValue();
    final String result = map.get("result").getValue();

    return new PlaceOrderResponse(orderId, result);
  }
}
