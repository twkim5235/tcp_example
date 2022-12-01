package dev.appkr.demo.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TcpServer {

  final int port;
  final ExecutorService executor;
  final AtomicReference<ServerSocket> serverSocketHolder = new AtomicReference<>();
  final AtomicReference<List<Socket>> socketHolder = new AtomicReference<>(new ArrayList<>());

  public TcpServer(int port, ExecutorService executor) {
    this.port = port;
    this.executor = executor;
  }

  public abstract void start();

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
