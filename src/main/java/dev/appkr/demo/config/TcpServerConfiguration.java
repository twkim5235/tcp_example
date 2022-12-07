package dev.appkr.demo.config;

import dev.appkr.demo.service.OrderServer;
import dev.appkr.demo.tcp.server.EchoServer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class TcpServerConfiguration {

  @Bean
  ApplicationRunner tcpServerStarter(OrderServer server) {
    return (args) -> server.start();
  }
}
