package dev.appkr.demo.tcp;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public interface TcpMessageTemplateFactory {

  // tcpMessage 길이를 분석하여, 하위 Packet의 반복 횟수를 구하여 객체를 생성한다
  List<TcpMessage> create(byte[] tcpMessage, Charset charset);

  // tcpMessage의 response를 생성합니다.
  TcpMessage createResponse(Map<String, String> response);
}
