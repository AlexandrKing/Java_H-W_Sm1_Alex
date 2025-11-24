package dataservice.decorators;

import dataservice.DataService;
import dataservice.SimpleDataService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationDecoratorTest {

  @Test
  void testSaveData_ValidatesKey() {
    DataService baseService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(baseService);

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("", "data");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData(null, "data");
    });

    assertDoesNotThrow(() -> {
      validationService.saveData("validKey", "data");
    });
  }

  @Test
  void testSaveData_ValidatesData() {
    DataService baseService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(baseService);

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("key", null);
    });

    assertDoesNotThrow(() -> {
      validationService.saveData("key", "validData");
    });
  }

  @Test
  void testFindDataByKey_ValidatesKey() {
    DataService baseService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(baseService);

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.findDataByKey("");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.findDataByKey(null);
    });
  }

  @Test
  void testDeleteData_ValidatesKey() {
    DataService baseService = new SimpleDataService();
    ValidationDecorator validationService = new ValidationDecorator(baseService);

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.deleteData("");
    });

    assertThrows(IllegalArgumentException.class, () -> {
      validationService.deleteData(null);
    });
  }
}
