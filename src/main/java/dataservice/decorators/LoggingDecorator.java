package dataservice.decorators;

import dataservice.DataService;

import java.util.Optional;

public class LoggingDecorator implements DataService {
  private final DataService wrappedService;

  public LoggingDecorator(DataService wrappedService) {
    this.wrappedService = wrappedService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    System.out.println("Logging: поиск данных по ключу: " + key);
    Optional<String> result = wrappedService.findDataByKey(key);
    System.out.println("Logging: результат поиска - " + (result.isPresent() ? "найден" : "не найден"));
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    System.out.println("Logging: сохранение данных по ключу: " + key);
    wrappedService.saveData(key, data);
    System.out.println("Logging: данные успешно сохранены");
  }

  @Override
  public boolean deleteData(String key) {
    System.out.println("Logging: удаление данных по ключу: " + key);
    boolean result = wrappedService.deleteData(key);
    System.out.println("Logging: результат удаления - " + (result ? "успешно" : "ключ не найден"));
    return result;
  }
}
