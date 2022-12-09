package dev.appkr.demo.service;

import dev.appkr.demo.service.OrderTcpMessage.ResponseField;
import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PipeSeparatedResponseTemplateFactory implements TcpMessageTemplateFactory {

  /** 서버가 응답하는 Tcp Message
   * orderId: "1b80c708-9951-4646-baff-55d33613aabe"
   * result: "SUCCESS"
   */
  @Override
  public List<TcpMessage> create(byte[] tcpMessage) {
    final List<TcpMessage> components = new ArrayList<>();
    components.add(Item.of(ResponseField.ORDER_ID, 0));
    components.add(Item.of(ResponseField.RESULT, 1));

    return components;
  }
}
