package dev.appkr.demo;

import dev.appkr.demo.tcp.config.TcpClientProperties;
import dev.appkr.demo.tcp.config.TcpServerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({TcpServerProperties.class, TcpClientProperties.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
