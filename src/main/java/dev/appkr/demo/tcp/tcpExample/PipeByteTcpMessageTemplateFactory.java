package dev.appkr.demo.tcp.tcpExample;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PipeByteTcpMessageTemplateFactory implements TcpMessageTemplateFactory {
  @Override
  public List<TcpMessage> create(byte[] tcpMessage, Charset charset) {
    final List<TcpMessage> components = new ArrayList<>();
    components.add(Item.of("ordererName", 1));
    components.add(Item.of("ordererPhone", 2));
    components.add(Item.of("receiverName", 3));
    components.add(Item.of("receiverPhone", 4));
    components.add(Item.of("receiverAddress", 5));

    final int noOfSubPacket = (getNoPipeline(tcpMessage, charset) - 5) / 4;
    Packet subPacket = new Packet("products");
    for (int i = 0; i < noOfSubPacket; i++) {
      subPacket.add(Item.of("productCode", 6 + (i * 4)));
      subPacket.add(Item.of("productName", 7 + (i * 4)));
      subPacket.add(Item.of("unitPrice", 8 + (i * 4)));
      subPacket.add(Item.of("quantity", 9 + (i * 4)));
    }
    components.add(subPacket);

    return components;
  }

  public int getNoPipeline(byte[] tcpMessage, Charset charset) {
    String message = new String(tcpMessage, charset);
    return message.length() - message.replace("|", "").length();
  }
}
