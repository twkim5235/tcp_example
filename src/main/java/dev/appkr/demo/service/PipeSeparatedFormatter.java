package dev.appkr.demo.service;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.visitor.Formatter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class PipeSeparatedFormatter implements Formatter {

  private final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  @Override
  public void format(Packet formattable) {
    final String formatted = doFormat(formattable.getMessageComponents());
    formattable.setTcpMessage(formatted.getBytes(DEFAULT_CHARSET));
  }

  private String doFormat(List<TcpMessage> components) {
    return components.stream()
        .map(component -> {
          String fragment;
          if (component instanceof Item) {
            fragment = component.getValue();
          } else {
            fragment = doFormat(((Packet) component).getMessageComponents());
          }
          return fragment;
        }).collect(Collectors.joining("|"));
  }
}
