package dev.appkr.demo.tcp.service;

import dev.appkr.demo.commons.exception.InvalidPhoneNumberException;
import dev.appkr.demo.tcp.Packet;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ValidateRequestPacketService {

  private static final Pattern phoneNumberPattern = Pattern.compile(
      "^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");

  public void validRequestPacket(Packet request) {
    request.getMessageComponents().forEach(
        mc -> {
          if (mc.getName().contains("Phone")) {
            if (!phoneNumberPattern.matcher(mc.getValue()).matches()) {
              throw new InvalidPhoneNumberException();
            }
          }
        }
    );
  }
}
