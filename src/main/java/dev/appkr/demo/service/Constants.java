package dev.appkr.demo.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {

  public static final String PIPE_SEPARATOR = "|";
  public static final String REGEX_SEPARATOR = "\\" + PIPE_SEPARATOR;
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  public static final String LINE_FEED = System.lineSeparator();
}
