package com.mipt.alexandergornostaev.collections;

import java.util.*;

public class CollectionPerformanceTester {

  public static void main(String[] args) {
    testPerformance();
  }

  public static void testPerformance() {
    int elementCount = 10000;
    List<Integer> arrayList = new ArrayList<>();
    List<Integer> linkedList = new LinkedList<>();

    System.out.println("Результаты тестирования производительности для " + elementCount + " элементов:");
    System.out.println("==================================================================");
    System.out.printf("%-20s | %-15s | %-15s%n", "Операция", "ArrayList (мс)", "LinkedList (мс)");
    System.out.println("==================================================================");

    long arrayListTime = measureTime(() -> {
      for (int i = 0; i < elementCount; i++) arrayList.add(i);
    });
    long linkedListTime = measureTime(() -> {
      for (int i = 0; i < elementCount; i++) linkedList.add(i);
    });
    System.out.printf("%-20s | %-15d | %-15d%n", "Добавление в конец", arrayListTime, linkedListTime);

    arrayList.clear();
    linkedList.clear();

    arrayListTime = measureTime(() -> {
      for (int i = 0; i < elementCount; i++) arrayList.add(0, i);
    });
    linkedListTime = measureTime(() -> {
      for (int i = 0; i < elementCount; i++) linkedList.add(0, i);
    });
    System.out.printf("%-20s | %-15d | %-15d%n", "Добавление в начало", arrayListTime, linkedListTime);

    for (int i = 0; i < elementCount; i++) {
      arrayList.add(i);
      linkedList.add(i);
    }

    arrayListTime = measureTime(() -> {
      for (int i = 0; i < 1000; i++) arrayList.add(arrayList.size() / 2, i);
    });
    linkedListTime = measureTime(() -> {
      for (int i = 0; i < 1000; i++) linkedList.add(linkedList.size() / 2, i);
    });
    System.out.printf("%-20s | %-15d | %-15d%n", "Вставка в середину", arrayListTime, linkedListTime);

    arrayListTime = measureTime(() -> {
      for (int i = 0; i < elementCount; i++) arrayList.get(i);
    });
    linkedListTime = measureTime(() -> {
      for (int i = 0; i < elementCount; i++) linkedList.get(i);
    });
    System.out.printf("%-20s | %-15d | %-15d%n", "Доступ по индексу", arrayListTime, linkedListTime);

    arrayListTime = measureTime(() -> {
      while (!arrayList.isEmpty()) arrayList.remove(0);
    });
    linkedListTime = measureTime(() -> {
      while (!linkedList.isEmpty()) linkedList.remove(0);
    });
    System.out.printf("%-20s | %-15d | %-15d%n", "Удаление из начала", arrayListTime, linkedListTime);

    for (int i = 0; i < elementCount; i++) {
      arrayList.add(i);
      linkedList.add(i);
    }

    arrayListTime = measureTime(() -> {
      while (!arrayList.isEmpty()) arrayList.remove(arrayList.size() - 1);
    });
    linkedListTime = measureTime(() -> {
      while (!linkedList.isEmpty()) linkedList.remove(linkedList.size() - 1);
    });
    System.out.printf("%-20s | %-15d | %-15d%n", "Удаление из конца", arrayListTime, linkedListTime);
    System.out.println("==================================================================");
  }

  private static long measureTime(Runnable operation) {
    long startTime = System.currentTimeMillis();
    operation.run();
    return System.currentTimeMillis() - startTime;
  }
}
