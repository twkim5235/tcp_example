package dev.appkr.demo.service;

import static dev.appkr.demo.service.OrderTcpMessage.RequestField.SUBPACKET_NAME;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

public class OrderTcpMessage {

  public static final String PIPE_SEPARATOR = "|";
  public static final String REGEX_SEPARATOR = "\\" + PIPE_SEPARATOR;
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  public static final String LINE_FEED = System.lineSeparator();
  public static final int NO_OF_PACKET_FIELDS = 5;
  public static final int NO_OF_SUBPACKET_FIELDS = 4;

  public static class RequestField {
    public static final String PACKET_NAME = "request";
    public static final String ORDERER_NAME = "ordererName";
    public static final String ORDERER_PHONE = "ordererPhone";
    public static final String RECEIVER_NAME = "receiverName";
    public static final String RECEIVER_PHONE = "receiverPhone";
    public static final String RECEIVER_ADDRESS = "receiverAddress";
    public static final String SUBPACKET_NAME = "products";
    public static final String SUBPACKET_PRODUCT_CODE = "productCode";
    public static final String SUBPACKET_PRODUCT_NAME = "productName";
    public static final String SUBPACKET_UNIT_PRICE = "unitPrice";
    public static final String SUBPACKET_QUANTITY = "quantity";
  }

  public static class ResponseField {
    public static final String PACKET_NAME = "response";
    public static final String ORDER_ID = "orderId";
    public static final String RESULT = "result";
  }

  public static String[] byteToStringArray(byte[] tcpMessage) {
    if (tcpMessage.length == 0) {
      return IntStream.range(0, NO_OF_SUBPACKET_FIELDS)
          .mapToObj(i -> "")
          .toArray(String[]::new);
    }

    return new String(tcpMessage, DEFAULT_CHARSET)
        .split(REGEX_SEPARATOR);
  }

  public static String getSubPacketName(int subPacketIndex) {
    return SUBPACKET_NAME + subPacketIndex;
  }

  public static int getNoOfSubPacket(String[] parts) {
    return (parts.length - NO_OF_PACKET_FIELDS) / NO_OF_SUBPACKET_FIELDS;
  }

  private OrderTcpMessage() {
  }
}
