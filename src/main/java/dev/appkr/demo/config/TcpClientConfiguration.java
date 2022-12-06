package dev.appkr.demo.config;

import dev.appkr.demo.service.PipeSeparatedFormatter;
import dev.appkr.demo.service.PipeSeparatedParser;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import dev.appkr.demo.tcp.client.DisposableTcpClient;
import dev.appkr.demo.tcp.client.TcpClient;
import dev.appkr.demo.tcp.config.TcpClientProperties;
import dev.appkr.demo.tcp.visitor.Formatter;
import dev.appkr.demo.tcp.visitor.Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TcpClientConfiguration {

  private final TcpClientProperties properties;

  public TcpClientConfiguration(TcpClientProperties properties) {
    this.properties = properties;
  }

  @Bean
  TcpClient defaultTcpClient() {
    return new DisposableTcpClient(properties.getHost(), properties.getPort(),
        properties.getCharset());
  }

  @Bean
  public Formatter defaultFormatter() {
    return new PipeSeparatedFormatter();
  }

  @Bean
  public Parser defaultParser(TcpMessageTemplateFactory factory) {
    return new PipeSeparatedParser(factory);
  }
}
