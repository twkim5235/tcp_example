package dev.appkr.demo.service.dto;

import static dev.appkr.demo.service.OrderTcpMessage.ResponseField.ORDER_ID;
import static dev.appkr.demo.service.OrderTcpMessage.ResponseField.RESULT;

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
    final String orderId = map.get(ORDER_ID).getValue();
    final String result = map.get(RESULT).getValue();

    return new PlaceOrderResponse(orderId, result);
  }
}
