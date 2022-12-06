package dev.appkr.demo.service;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PipeSeparatedTcpMessageTemplateFactory implements TcpMessageTemplateFactory {

  @Override
  public List<TcpMessage> create(byte[] tcpMessage) {
    final List<TcpMessage> components = new ArrayList<>();
    components.add(Item.of("orderId", 0));
    components.add(Item.of("result", 1));

    return components;
  }
}

//  클라이언트가 제출하는 Tcp Message
//
//ordererName: "김메쉬"
//    ordererPhone: "010-1234-5678"
//    receiverName: "홍길동"
//    receiverPhone: "010-4321-8765"
//    receiverAddress: "서울특별시 강남구 대치동 890-12 다봉타워 13층"
//    products
//    - productCode: "68d8a5d5-2860-4a0c-bf64-3c879cf3cf61"
//    productName: "지포라이터""
//    unitPrice: "50000"
//    quantity: "1"
//    - productCode: "864bc23c-59eb-4a55-b68e-f3cbc9700315"
//    productName: "지포라이터 부싯돌 셋트"
//    unitPrice: "5000"
//    quantity: "1"
//
//    서버가 응답하는 Tcp Message
//
//    orderId: "1b80c708-9951-4646-baff-55d33613aabe"
//    result: "SUCCESS"
