package dev.appkr.demo.service.dto;

import static dev.appkr.demo.service.OrderTcpMessage.ROOT_PACKET_NAME;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.ORDERER_NAME;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.ORDERER_PHONE;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.RECEIVER_ADDRESS;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.RECEIVER_NAME;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.RECEIVER_PHONE;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.SUBPACKET_NAME;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.SUBPACKET_PRODUCT_CODE;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.SUBPACKET_PRODUCT_NAME;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.SUBPACKET_QUANTITY;
import static dev.appkr.demo.service.OrderTcpMessage.RequestField.SUBPACKET_UNIT_PRICE;

import dev.appkr.demo.service.OrderTcpMessage.RequestField;
import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceOrderRequest {

  Orderer orderer;
  Receiver receiver;
  List<Product> products;

  @Data
  @AllArgsConstructor
  public static class Orderer {

    String name;
    String phone;
  }

  @Data
  @AllArgsConstructor
  public static class Receiver {

    String name;
    String phone;
    String address;
  }

  public Packet toPacket() {
    final Packet root = new Packet(ROOT_PACKET_NAME);
    root.add(Item.of(ORDERER_NAME, 0, orderer.name));
    root.add(Item.of(ORDERER_PHONE, 1, orderer.phone));
    root.add(Item.of(RECEIVER_NAME, 2, receiver.name));
    root.add(Item.of(RECEIVER_PHONE, 3, receiver.phone));
    root.add(Item.of(RECEIVER_ADDRESS, 4, receiver.address));

    for (int i = 0; i < products.size(); i++) {
      final Packet subPacket = new Packet(SUBPACKET_NAME + i + 1);
      final Product product = products.get(i);
      subPacket.add(Item.of(SUBPACKET_PRODUCT_CODE, getPointer(5, i), product.code));
      subPacket.add(Item.of(SUBPACKET_PRODUCT_NAME, getPointer(6, i), product.name));
      subPacket.add(Item.of(SUBPACKET_UNIT_PRICE, getPointer(7, i), String.valueOf(product.unitPrice.getValue())));
      subPacket.add(Item.of(SUBPACKET_QUANTITY, getPointer(8, i), String.valueOf(product.quantity)));
      root.add(subPacket);
    }
    return root;
  }

  private Integer getPointer(int baseNum, int index) {
    return baseNum + index * 4;
  }
}
