package dev.appkr.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

  String code;
  String name;
  Money unitPrice;
  Integer quantity;
}
