package dev.appkr.demo.tcp.server;

import dev.appkr.demo.tcp.Packet;
import dev.appkr.demo.tcp.TcpMessageTemplateFactory;
import dev.appkr.demo.tcp.config.TcpServerProperties;
import dev.appkr.demo.tcp.tcpExample.PipeByteResponseTcpMessageTemplateFactory;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PipeTcpServer extends TcpServer{
  final Charset charset;

  final Formatter formatter;
  final Parser parser;
  //Dip 위반이긴하나, parser의 factory와 구분하기 위해 인스턴스를 명시하였습니다.
  final TcpMessageTemplateFactory templateFactory = new PipeByteResponseTcpMessageTemplateFactory();

  public PipeTcpServer(TcpServerProperties properties, Formatter formatter, Parser parser) {
    super(properties.getPort(), Executors.newFixedThreadPool(properties.getMaxConnection()));
    this.charset = StandardCharsets.UTF_8;
    this.formatter = formatter;
    this.parser = parser;
  }

  @Override
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
            Packet response;
            try {
              parseRequestPacket(request);
              response = makeResponsePacket(UUID.randomUUID(), "SUCCESS");
            } catch (Exception e) {
              response = makeResponsePacket(UUID.randomUUID(), "FAULT");
            }
            response.accept(formatter);
            writer.println(new String(response.getTcpMessage(), charset));
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

  private void parseRequestPacket(String message) {
    final Packet requestPacket = new Packet("root", message.getBytes(charset));
    requestPacket.accept(parser);
  }

  private Packet makeResponsePacket(UUID uuid, String result) {
    Map<String, String> map = new LinkedHashMap<>();
    map.put("orderId", uuid.toString());
    map.put("result", result);

    return (Packet) templateFactory.createResponse(map);
  }
}
