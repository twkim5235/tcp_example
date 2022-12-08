package dev.appkr.demo.config;

import dev.appkr.demo.service.PipeSeparatedFormatter;
import dev.appkr.demo.service.PipeSeparatedParser;
import dev.appkr.demo.service.PipeSeparatedRequestTemplateFactory;
import dev.appkr.demo.service.PipeSeparatedResponseTemplateFactory;
import dev.appkr.demo.tcp.client.DisposableTcpClient;
import dev.appkr.demo.tcp.client.TcpClient;
import dev.appkr.demo.tcp.config.TcpClientProperties;
import dev.appkr.demo.tcp.visitor.Formatter;
import dev.appkr.demo.tcp.visitor.Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
  @Primary
  public Parser requestParser(PipeSeparatedRequestTemplateFactory factory) {
    return new PipeSeparatedParser(factory);
  }

  @Bean
  public Parser responseParser(PipeSeparatedResponseTemplateFactory factory) {
    return new PipeSeparatedParser(factory);
  }
}
