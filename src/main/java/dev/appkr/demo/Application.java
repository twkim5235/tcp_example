package dev.appkr.demo;

import dev.appkr.demo.tcp.config.TcpClientProperties;
import dev.appkr.demo.tcp.server.EchoServer;
import dev.appkr.demo.tcp.config.TcpServerProperties;
import dev.appkr.demo.tcp.server.PipeTcpServer;
import dev.appkr.demo.tcp.server.TcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableConfigurationProperties({TcpServerProperties.class, TcpClientProperties.class})
public class Application {

	final TcpServer pipeTcpServer;

	public Application(TcpServer pipeTcpServer) {
		this.pipeTcpServer = pipeTcpServer;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener
	void onApplicationReadyEvent(ApplicationReadyEvent e) {
		pipeTcpServer.start();
	}
}
