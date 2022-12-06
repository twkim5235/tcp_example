package dev.appkr.demo.service;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import dev.appkr.demo.tcp.visitor.Parser;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PipeSeparatedParser implements Parser {

  private final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private final TcpMessageTemplateFactory factory;

  public PipeSeparatedParser(TcpMessageTemplateFactory factory) {
    this.factory = factory;
  }

  @Override
  public void parse(Packet parseable) {
    final byte[] src = parseable.getTcpMessage();
    final List<TcpMessage> components = factory.create(src);
    parseable.setMessageComponents(components);

    doParser(src, components);
  }

  private void doParser(byte[] src, List<TcpMessage> components) {
    String message = new String(src, DEFAULT_CHARSET);
    String[] strings = message.split("\\|");
    components.stream()
        .forEach(component -> {
          if (component instanceof Item) {
            component.setValue(strings[component.getPointer()]);
          } else {
            doParser(src, ((Packet)component).getMessageComponents());
          }
        });
  }
}
