package dev.appkr.demo.tcp.server;

import dev.appkr.demo.tcp.Item;
import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.config.TcpServerProperties;
import dev.appkr.demo.tcp.visitor.Formatter;
import dev.appkr.demo.tcp.visitor.Parser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PipeTcpServer {
  final int port;
  final ExecutorService executor;
  final Charset charset;

  final AtomicReference<ServerSocket> serverSocketHolder = new AtomicReference<>();
  final AtomicReference<List<Socket>> socketHolder = new AtomicReference<>(new ArrayList<>());

  final Formatter formatter;
  final Parser parser;

  public PipeTcpServer(TcpServerProperties properties, Formatter formatter, Parser parser) {
    this.port = properties.getPort();
    this.executor = Executors.newFixedThreadPool(properties.getMaxConnection());
    this.charset = StandardCharsets.UTF_8;
    this.formatter = formatter;
    this.parser = parser;
  }

  @SneakyThrows
  public void start() {
    final ServerSocket serverSocket = new ServerSocket(port);
    this.serverSocketHolder.set(serverSocket);
    log.info("Tcp server is listening at port {}", port);

    while (true) {
      final Socket socket = serverSocket.accept(); // 여기서 블록킹하고 있다가, 클라이언트가 접속하면 해제됨
      socketHolder.get().add(socket);
      log.info("A connection established to port {}", socket.getPort());

      CompletableFuture.runAsync(() -> {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
          writer = new PrintWriter(socket.getOutputStream(), true);
          reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

          String request;
          while ((request = reader.readLine()) != null) {
            try {
              validRequestPacket(request);
            } catch (Exception e) {
              final Packet packet = makeResponsePacket(UUID.randomUUID(), "FAULT");
              packet.accept(formatter);
              writer.println(new String(packet.getTcpMessage(), charset));
              break;
            }
            final Packet packet = makeResponsePacket(UUID.randomUUID(), "SUCCESS");
            packet.accept(formatter);
            writer.println(new String(packet.getTcpMessage(), charset));
          }
        } catch (IOException e) {
          log.warn("{}: {}", e.getMessage(), socket.getPort());
        } finally {
          try {
            reader.close();
            writer.close();
            socket.close();
          } catch (IOException e) {
            log.error(String.format("Connection to port %s was not closed", socket.getPort()));
          }
        }
      }, executor);
    }
  }

  private void validRequestPacket(String message) {
    final Packet requestPacket = new Packet("root", message.getBytes(charset));
    requestPacket.accept(parser);
  }

  private Packet makeResponsePacket(UUID uuid, String result) {
    final Packet rootPacket = new Packet("root");
    rootPacket.add(Item.of("orderId", 1, uuid.toString()));
    rootPacket.add(Item.of("result", 2, result));
    return rootPacket;
  }

  @PreDestroy
  @SneakyThrows
  public void stop() {
    if (socketHolder != null) {
      socketHolder.get().forEach(s -> {
        try {
          s.close();
        } catch (IOException e) {
          log.error(String.format("Connection to port %s was not closed", s.getPort()));
        }
      });
    }

    if (serverSocketHolder != null && serverSocketHolder.get() != null) {
      serverSocketHolder.get().close();
    }

    executor.awaitTermination(5, TimeUnit.SECONDS);
    executor.shutdown();
  }
}
