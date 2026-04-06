package com.mipt.alexandergornostaev.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomArrayListTest {

  private CustomList<String> list;

  @BeforeEach
  void setUp() {
    list = new CustomArrayList<>();
  }

  @Test
  void testAddAndGet() {
    list.add("first");
    list.add("second");
    assertEquals("first", list.get(0));
    assertEquals("second", list.get(1));
  }

  @Test
  void testAddNullThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> list.add(null));
  }

  @Test
  void testRemove() {
    list.add("first");
    list.add("second");
    list.add("third");

    String removed = list.remove(1);
    assertEquals("second", removed);
    assertEquals(2, list.size());
    assertEquals("first", list.get(0));
    assertEquals("third", list.get(1));
  }

  @Test
  void testSizeAndIsEmpty() {
    assertTrue(list.isEmpty());
    list.add("element");
    assertEquals(1, list.size());
    assertFalse(list.isEmpty());
  }

  @Test
  void testIterator() {
    list.add("first");
    list.add("second");

    StringBuilder result = new StringBuilder();
    for (String element : (CustomArrayList<String>) list) {
      result.append(element);
    }
    assertEquals("firstsecond", result.toString());
  }
}
