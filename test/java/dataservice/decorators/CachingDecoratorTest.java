package dataservice.decorators;

import dataservice.DataService;
import dataservice.SimpleDataService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CachingDecoratorTest {

  @Test
  void testFindDataByKey_CachesResult() {
    DataService baseService = new SimpleDataService();
    CachingDecorator cachingService = new CachingDecorator(baseService);

    baseService.saveData("test", "value");

    var result1 = cachingService.findDataByKey("test");
    assertTrue(result1.isPresent());
    assertEquals("value", result1.get());

    baseService.deleteData("test");

    var result2 = cachingService.findDataByKey("test");
    assertTrue(result2.isPresent());
    assertEquals("value", result2.get());
  }

  @Test
  void testSaveData_UpdatesCache() {
    DataService baseService = new SimpleDataService();
    CachingDecorator cachingService = new CachingDecorator(baseService);

    cachingService.saveData("key", "data");

    var result = cachingService.findDataByKey("key");
    assertTrue(result.isPresent());
    assertEquals("data", result.get());
  }

  @Test
  void testDeleteData_InvalidatesCache() {
    DataService baseService = new SimpleDataService();
    CachingDecorator cachingService = new CachingDecorator(baseService);

    baseService.saveData("key", "data");

    cachingService.findDataByKey("key");

    boolean deleted = cachingService.deleteData("key");
    assertTrue(deleted);

    var result = cachingService.findDataByKey("key");
    assertFalse(result.isPresent());
  }
}
