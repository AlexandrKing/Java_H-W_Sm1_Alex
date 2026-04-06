package fileprocessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class FileProcessorTest {

  public static void main(String[] args) {
    try {
      testSplitAndMergeFile();
      System.out.println("Все тесты прошли успешно!");
    } catch (Exception e) {
      System.out.println("Тест провален: " + e.getMessage());
      e.printStackTrace();
    }
  }

  static void testSplitAndMergeFile() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = Path.of("test.dat");
    byte[] testData = new byte[1500];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    Path outputDir = Path.of("parts");
    Files.createDirectories(outputDir);
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir.toString(), 500);

    if (parts.size() != 3) {
      throw new AssertionError("Ожидалось 3 части, но получилось: " + parts.size());
    }

    Path mergedFile = Path.of("merged.dat");
    processor.mergeFiles(parts, mergedFile.toString());

    byte[] original = Files.readAllBytes(testFile);
    byte[] merged = Files.readAllBytes(mergedFile);

    if (original.length != merged.length) {
      throw new AssertionError("Размеры файлов не совпадают");
    }

    Files.deleteIfExists(mergedFile);
    for (Path part : parts) {
      Files.deleteIfExists(part);
    }
    Files.deleteIfExists(outputDir);
    Files.deleteIfExists(testFile);

    System.out.println("testSplitAndMergeFile пройден успешно!");
  }
}
