package dev.appkr.demo.service;

import static dev.appkr.demo.service.OrderTcpMessage.DEFAULT_CHARSET;
import static dev.appkr.demo.service.OrderTcpMessage.LINE_FEED;

import dev.appkr.demo.tcp.client.TcpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingsTcpClient {

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
    return original.endsWith(LINE_FEED)
        ? original
        : original + LINE_FEED;
  }
}
