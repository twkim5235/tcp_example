package dev.appkr.demo.tcp.pool;

public class MyObject {

  private static int count;

  public MyObject() {
    count++;
  }

  public static int getCount() {
    return count;
  }
}
