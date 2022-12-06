package dev.appkr.demo.service.dto;

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
    final Packet root = new Packet("root");
    root.add(Item.of("ordererName", 0, orderer.name));
    root.add(Item.of("ordererPhone", 1, orderer.phone));
    root.add(Item.of("receiverName", 2, receiver.name));
    root.add(Item.of("receiverPhone", 3, receiver.phone));
    root.add(Item.of("receiverAddress", 4, receiver.address));

    for (int i = 0; i < products.size(); i++) {
      final Packet subPacket = new Packet("product" + i + 1);
      final Product product = products.get(i);
      subPacket.add(Item.of("productCode", getPointer(5, i), product.code));
      subPacket.add(Item.of("productName", getPointer(6, i), product.name));
      subPacket.add(Item.of("unitPrice", getPointer(7, i), String.valueOf(product.unitPrice.getValue())));
      subPacket.add(Item.of("quantity", getPointer(8, i), String.valueOf(product.quantity)));
      root.add(subPacket);
    }
    return root;
  }

  private Integer getPointer(int baseNum, int index) {
    return baseNum + index * 4;
  }
}
