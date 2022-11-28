package dev.appkr.demo.tcp.tcpExample;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PipeByteResponseTcpMessageTemplateFactory implements TcpMessageTemplateFactory {
  @Override
  public List<TcpMessage> create(byte[] tcpMessage, Charset charset) {
    final List<TcpMessage> components = new ArrayList<>();
    components.add(Item.of("orderId", 1));
    components.add(Item.of("result", 2));

    return components;
  }

  public int getNoPipeline(byte[] tcpMessage, Charset charset) {
    String message = new String(tcpMessage, charset);
    return message.length() - message.replace("|", "").length();
  }
}
