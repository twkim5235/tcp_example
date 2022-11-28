package dev.appkr.demo.tcp.tcpExample;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import dev.appkr.demo.tcp.visitor.Parser;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class PipeByteParser implements Parser {

  @Setter
  private Charset charset = StandardCharsets.UTF_8;

  private final TcpMessageTemplateFactory templateFactory;

  public PipeByteParser(TcpMessageTemplateFactory templateFactory) {
    this.templateFactory = templateFactory;
  }


  @Override
  public void parse(Packet parseable) {
    final byte[] src = parseable.getTcpMessage();
    final List<TcpMessage> components = templateFactory.create(src, charset);
    parseable.setMessageComponents(components);

    doParse(new String(src, charset), components);
  }

  private void doParse(String message, List<TcpMessage> components) {
    String[] split = message.split("\\|");
    components.stream()
        .forEach(component -> {

          if (component instanceof Item) {
            final String value = split[component.getPointer() - 1];
            component.setValue(value);
          } else {
            final String subPacketName = String.valueOf(component.getName());
            component.setName(subPacketName);
            ((Packet) component).setTcpMessage(message.getBytes(charset));
            doParse(message, ((Packet) component).getMessageComponents());
          }
        });
  }
}
