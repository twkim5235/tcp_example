package dev.appkr.demo.service;

import static dev.appkr.demo.service.OrderTcpMessage.NO_OF_SUBPACKET_FIELDS;
import static dev.appkr.demo.service.OrderTcpMessage.byteToStringArray;
import static dev.appkr.demo.service.OrderTcpMessage.getNoOfSubPacket;
import static dev.appkr.demo.service.OrderTcpMessage.getSubPacketName;

import dev.appkr.demo.service.OrderTcpMessage.RequestField;
import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PipeSeparatedRequestTemplateFactory implements TcpMessageTemplateFactory {

  /**
   * 클라이언트가 제출하는 Tcp Message
   *
   * ordererName: "김메쉬"
   * ordererPhone: "010-1234-5678"
   * receiverName: "홍길동"
   * receiverPhone: "010-4321-8765"
   * receiverAddress: "서울특별시 강남구 대치동 890-12 다봉타워 13층"
   * - productCode: "68d8a5d5-2860-4a0c-bf64-3c879cf3cf61"
   *   productName: "지포라이터""
   *   unitPrice: "50000"
   *   quantity: "1"
   * - productCode: "864bc23c-59eb-4a55-b68e-f3cbc9700315"
   *   productName: "지포라이터 부싯돌 셋트"
   *   unitPrice: "5000"
   *   quantity: "1"
   */
  @Override
  public List<TcpMessage> create(byte[] tcpMessage) {
    final List<TcpMessage> components = new ArrayList<>();
    components.add(Item.of(RequestField.ORDERER_NAME, 0));
    components.add(Item.of(RequestField.ORDERER_PHONE, 1));
    components.add(Item.of(RequestField.RECEIVER_NAME, 2));
    components.add(Item.of(RequestField.RECEIVER_PHONE, 3));
    components.add(Item.of(RequestField.RECEIVER_ADDRESS, 4));

    final String[] parts = byteToStringArray(tcpMessage);
    for (int subPacketIndex = 1; subPacketIndex <= getNoOfSubPacket(parts); subPacketIndex++) {
      final Packet subPacket = new Packet(getSubPacketName(subPacketIndex));
      subPacket.add(Item.of(RequestField.SUBPACKET_PRODUCT_CODE, subPacketIndex * NO_OF_SUBPACKET_FIELDS + 1));
      subPacket.add(Item.of(RequestField.SUBPACKET_PRODUCT_NAME, subPacketIndex * NO_OF_SUBPACKET_FIELDS + 2));
      subPacket.add(Item.of(RequestField.SUBPACKET_UNIT_PRICE, subPacketIndex * NO_OF_SUBPACKET_FIELDS + 3));
      subPacket.add(Item.of(RequestField.SUBPACKET_QUANTITY, subPacketIndex * NO_OF_SUBPACKET_FIELDS + 4));

      components.add(subPacket);
    }

    return components;
  }
}
