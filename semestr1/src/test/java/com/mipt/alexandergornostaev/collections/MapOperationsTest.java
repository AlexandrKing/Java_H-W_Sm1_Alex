package com.mipt.alexandergornostaev.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class MapOperationsTest {

  private HashMap<Integer, Student> hashMap;
  private TreeMap<Integer, Student> treeMap;

  @BeforeEach
  void setUp() {
    hashMap = new HashMap<>();
    treeMap = new TreeMap<>(Comparator.reverseOrder());

    Student[] students = {
        new Student(1, "Alice", 4.5),
        new Student(2, "Bob", 3.8),
        new Student(3, "Charlie", 4.2),
        new Student(4, "Diana", 3.5),
        new Student(5, "Eve", 4.8)
    };

    for (Student student : students) {
      hashMap.put(student.getId(), student);
      treeMap.put(student.getId(), student);
    }
  }

  @Test
  void testFindStudentsByGradeRange() {
    List<Student> result = MapOperations.findStudentsByGradeRange(hashMap, 4.0, 4.5);
    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(s -> s.getName().equals("Alice")));
    assertTrue(result.stream().anyMatch(s -> s.getName().equals("Charlie")));
  }

  @Test
  void testGetTopNStudents() {
    List<Student> result = MapOperations.getTopNStudents(treeMap, 3);
    assertEquals(3, result.size());
    assertEquals("Eve", result.get(0).getName());
    assertEquals("Diana", result.get(1).getName());
    assertEquals("Charlie", result.get(2).getName());
  }

  @Test
  void testStudentEqualsAndHashCode() {
    Student student1 = new Student(1, "Alice", 4.5);
    Student student2 = new Student(1, "Alice", 4.5);
    assertEquals(student1, student2);
    assertEquals(student1.hashCode(), student2.hashCode());
  }
}
