package org.group.mall.common;

public class MyException extends RuntimeException {

  public MyException() {
  }

  public MyException(String message) {
    super(message);
  }

  /**
   * 丢出一个异常
   *
   * @param message
   */
  public static void fail(String message) {
    throw new MyException(message);
  }

}
