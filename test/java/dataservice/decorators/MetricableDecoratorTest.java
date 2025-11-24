package dataservice.decorators;

import dataservice.DataService;
import dataservice.SimpleDataService;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class MetricableDecoratorTest {

  @Test
  void testFindDataByKey_SendsMetrics() {
    DataService baseService = new SimpleDataService();
    MetricableDecorator metricService = new MetricableDecorator(baseService);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      baseService.saveData("test", "value");
      metricService.findDataByKey("test");

      String output = outputStream.toString();
      assertTrue(output.contains("Метод выполнялся: PT"));
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testSaveData_SendsMetrics() {
    DataService baseService = new SimpleDataService();
    MetricableDecorator metricService = new MetricableDecorator(baseService);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      metricService.saveData("key", "data");

      String output = outputStream.toString();
      assertTrue(output.contains("Метод выполнялся: PT"));
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testDeleteData_SendsMetrics() {
    DataService baseService = new SimpleDataService();
    MetricableDecorator metricService = new MetricableDecorator(baseService);

    baseService.saveData("key", "data");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      metricService.deleteData("key");

      String output = outputStream.toString();
      assertTrue(output.contains("Метод выполнялся: PT"));
    } finally {
      System.setOut(originalOut);
    }
  }
}
