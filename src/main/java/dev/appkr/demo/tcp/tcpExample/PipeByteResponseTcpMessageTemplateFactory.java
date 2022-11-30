package dev.appkr.demo.tcp.tcpExample;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PipeByteResponseTcpMessageTemplateFactory implements TcpMessageTemplateFactory {
  @Override
  public List<TcpMessage> create(byte[] tcpMessage, Charset charset) {
    final List<TcpMessage> components = new ArrayList<>();
    components.add(Item.of("orderId", 1));
    components.add(Item.of("result", 2));

    return components;
  }

  @Override
  public TcpMessage createResponse(Map<String, String> response) {
    final Packet rootPacket = new Packet("root");
    int i = 1;
    for (String key : response.keySet()) {
      rootPacket.add(Item.of(key, i++, response.get(key)));
    }
    return rootPacket;
  }
}
