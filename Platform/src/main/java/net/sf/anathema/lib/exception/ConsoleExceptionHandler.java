package net.sf.anathema.lib.exception;

public class ConsoleExceptionHandler implements ExceptionHandler {
  @Override
  public void handle(Throwable exception) {
    exception.printStackTrace();
  }

  @Override
  public void handle(Throwable exception, String message) {
    System.out.println(message);
    exception.printStackTrace();
  }
}