package dev.appkr.demo.service;

import static dev.appkr.demo.service.OrderFixtures.aRequestBytes;
import static dev.appkr.demo.service.OrderFixtures.aResponseBytes;

import dev.appkr.demo.Application;
import dev.appkr.demo.service.dto.PlaceOrderResponse;
import dev.appkr.demo.tcp.client.TcpClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {Application.class})
@Slf4j
class OrderServiceTest {

  @Autowired
  OrderService orderService;
  @MockBean
  TcpClient tcpClientMock;

  @Test
  void placeOrder() throws Exception {
    Mockito.doNothing().when(tcpClientMock).write(aRequestBytes());
    Mockito.when(tcpClientMock.read()).thenReturn(aResponseBytes());

    final PlaceOrderResponse aResponse = orderService.placeOrder(OrderFixtures.aRequest());
    log.info("response: {}", aResponse);
  }
}
