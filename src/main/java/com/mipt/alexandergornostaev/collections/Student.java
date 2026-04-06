package com.mipt.alexandergornostaev.collections;

import java.util.Objects;

public class Student {
  private int id;
  private String name;
  private double grade;

  public Student(int id, String name, double grade) {
    this.id = id;
    this.name = name;
    this.grade = grade;
  }

  public int getId() { return id; }
  public String getName() { return name; }
  public double getGrade() { return grade; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Student student = (Student) o;
    return id == student.id &&
        Double.compare(student.grade, grade) == 0 &&
        Objects.equals(name, student.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, grade);
  }

  @Override
  public String toString() {
    return "Student{id=" + id + ", name='" + name + "', grade=" + grade + '}';
  }
}
