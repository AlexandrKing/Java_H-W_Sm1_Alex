package com.mipt.alexandergornostaev.collections;

import java.util.*;
import java.util.stream.Collectors;

public class MapOperations {

  public static List<Student> findStudentsByGradeRange(Map<Integer, Student> map,
                                                       double minGrade, double maxGrade) {
    return map.values().stream()
        .filter(student -> student.getGrade() >= minGrade && student.getGrade() <= maxGrade)
        .collect(Collectors.toList());
  }

  public static List<Student> getTopNStudents(TreeMap<Integer, Student> map, int n) {
    return map.descendingMap().values().stream()
        .limit(n)
        .collect(Collectors.toList());
  }
}
