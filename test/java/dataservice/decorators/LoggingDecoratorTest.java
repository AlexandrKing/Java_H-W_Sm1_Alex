package dataservice.decorators;

import dataservice.DataService;
import dataservice.SimpleDataService;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class LoggingDecoratorTest {

  @Test
  void testFindDataByKey_LogsAction() {
    DataService baseService = new SimpleDataService();
    LoggingDecorator loggingService = new LoggingDecorator(baseService);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      baseService.saveData("test", "value");
      loggingService.findDataByKey("test");

      String output = outputStream.toString();
      assertTrue(output.contains("Logging: поиск данных по ключу: test"));
      assertTrue(output.contains("Logging: результат поиска - найден"));
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testSaveData_LogsAction() {
    DataService baseService = new SimpleDataService();
    LoggingDecorator loggingService = new LoggingDecorator(baseService);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      loggingService.saveData("key", "data");

      String output = outputStream.toString();
      assertTrue(output.contains("Logging: сохранение данных по ключу: key"));
      assertTrue(output.contains("Logging: данные успешно сохранены"));
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testDeleteData_LogsAction() {
    DataService baseService = new SimpleDataService();
    LoggingDecorator loggingService = new LoggingDecorator(baseService);

    baseService.saveData("key", "data");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      loggingService.deleteData("key");

      String output = outputStream.toString();
      assertTrue(output.contains("Logging: удаление данных по ключу: key"));
      assertTrue(output.contains("Logging: результат удаления - успешно"));
    } finally {
      System.setOut(originalOut);
    }
  }
}
