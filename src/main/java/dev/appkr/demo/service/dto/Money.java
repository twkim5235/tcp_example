package dev.appkr.demo.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = {"value"})
@ToString(of = {"value"})
public class Money {

  public static final Money ZERO = new Money(0L);

  private final Long value;

  public Money(Long value) {
    this.value = value;
  }

  public Money add(Money that) {
    return new Money(this.value + that.value);
  }

  public Money multiply(Integer quantity) {
    return new Money(this.value * quantity);
  }
}
