package dev.appkr.demo.service;

import dev.appkr.demo.tcp.client.TcpClient;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingsTcpClient {

  static final String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");
  static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  private final TcpClient tcpClient;

  public String hello(String greeting) {
    greeting = preprocess(greeting);

    byte[] response = {};
    try {
      tcpClient.write(greeting.getBytes(DEFAULT_CHARSET));
      response = tcpClient.read();
    } catch (Exception e) {
      throw new GreetingFailedException();
    }

    return new String(response, DEFAULT_CHARSET);
  }

  private String preprocess(String original) {
    return original.endsWith(DEFAULT_LINE_SEPARATOR)
        ? original
        : original + DEFAULT_LINE_SEPARATOR;
  }
}
