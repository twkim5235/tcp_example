package dev.appkr.demo.service;

import dev.appkr.demo.service.dto.Money;
import dev.appkr.demo.service.dto.PlaceOrderRequest;
import dev.appkr.demo.service.dto.PlaceOrderRequest.Orderer;
import dev.appkr.demo.service.dto.PlaceOrderRequest.Receiver;
import dev.appkr.demo.service.dto.Product;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class OrderFixtures {

  static final String DUMMY_NAME = "foo";
  static final String DUMMY_PHONE = "01012345678";
  static final String DUMMY_ADDR = "서울특별시 강남구 대치동 890-12 다봉타워 13층";
  static final String DUMMY_CODE = UUID.randomUUID().toString();
  static final String DUMMY_ORDER_ID = "1b80c708-9951-4646-baff-55d33613aabe";
  static final String DUMMY_RESULT = "SUCCESS";
  static final int DUMMY_QTY = 1;

  public static byte[] aRequestBytes() {
    return "김메쉬|010-1234-5678|홍길동|010-4321-8765|서울특별시 강남구 대치동 890-12 다봉타워 13층|68d8a5d5-2860-4a0c-bf64-3c879cf3cf61|지포라이터|50000|1|864bc23c-59eb-4a55-b68e-f3cbc9700315|지포라이터 부싯돌 셋트|5000|1".getBytes(
        StandardCharsets.UTF_8);
  }

  public static byte[] aResponseBytes() {
    return (DUMMY_ORDER_ID + "|" + DUMMY_RESULT).getBytes(StandardCharsets.UTF_8);
  }

  public static Orderer anOrderer() {
    return new Orderer(DUMMY_NAME, DUMMY_PHONE);
  }

  public static Receiver aReceiver() {
    return new Receiver(DUMMY_NAME, DUMMY_PHONE, DUMMY_ADDR);
  }

  public static Product aProduct() {
    return new Product(DUMMY_CODE, DUMMY_NAME, Money.ZERO, DUMMY_QTY);
  }

  public static PlaceOrderRequest aRequest() {
    return new PlaceOrderRequest(anOrderer(), aReceiver(), List.of(aProduct()));
  }
}
