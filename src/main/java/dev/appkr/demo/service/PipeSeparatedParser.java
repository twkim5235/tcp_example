package dev.appkr.demo.service;

import static dev.appkr.demo.service.OrderTcpMessage.DEFAULT_CHARSET;
import static dev.appkr.demo.service.OrderTcpMessage.REGEX_SEPARATOR;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import dev.appkr.demo.tcp.visitor.Parser;
import java.util.List;

public class PipeSeparatedParser implements Parser {

  private final TcpMessageTemplateFactory factory;

  public PipeSeparatedParser(TcpMessageTemplateFactory factory) {
    this.factory = factory;
  }

  @Override
  public void parse(Packet parseable) {
    final byte[] src = parseable.getTcpMessage();
    final List<TcpMessage> components = factory.create(src);
    parseable.setMessageComponents(components);

    doParse(src, components);
  }

  private void doParse(byte[] src, List<TcpMessage> components) {
    String message = new String(src, DEFAULT_CHARSET);
    String[] parts = message.split(REGEX_SEPARATOR);
    components.stream()
        .forEach(component -> {
          if (component instanceof Item) {
            component.setValue(parts[component.getPointer()]);
          } else {
            doParse(src, ((Packet) component).getMessageComponents());
          }
        });
  }
}
