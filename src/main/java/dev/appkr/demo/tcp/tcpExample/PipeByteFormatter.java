package dev.appkr.demo.tcp.tcpExample;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessage;
import dev.appkr.demo.tcp.visitor.Formatter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class PipeByteFormatter implements Formatter {

  @Setter
  private Charset charset = StandardCharsets.UTF_8;

  @Override
  public void format(Packet packet) {
    String tcpMessage = doFormat(packet.getMessageComponents()) + "\n";
    packet.setTcpMessage(tcpMessage.getBytes(charset));
  }

  private String doFormat(List<TcpMessage> components) {
    StringBuilder sb = new StringBuilder();
    components.forEach(
        component -> {
          String formattedMessage;
          if (component instanceof Item) {
            formattedMessage = formatFor(component);
          } else {
            formattedMessage = doFormat(((Packet) component).getMessageComponents());
          }

          sb.append(formattedMessage);
        }
    );

    return sb.toString();
  }

  private String formatFor(TcpMessage tcpMessage) {
    return tcpMessage.getValue() + "|";
  }
}
