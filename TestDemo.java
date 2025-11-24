import fileprocessing.TextFileAnalyzer;
import fileprocessing.FileProcessor;
import java.nio.file.*;
import java.util.*;

public class TestDemo {
  public static void main(String[] args) throws Exception {
    System.out.println("=== ДЕМО-ТЕСТ ПРОЕКТА ===\n");

    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = Path.of("demo_test.txt");
    Files.write(testFile, Arrays.asList("Hello Java!", "File processing demo.", "Third line."));

    System.out.println("1. Анализ файла...");
    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile("demo_test.txt");
    System.out.println("Результат: " + result);

    System.out.println("2. Сохранение результатов...");
    analyzer.saveAnalysisResult(result, "demo_analysis.txt");
    System.out.println("Результаты сохранены в demo_analysis.txt");

    FileProcessor processor = new FileProcessor();

    System.out.println("3. Разделение файла на части...");
    List<Path> parts = processor.splitFile("demo_test.txt", "demo_parts", 20);
    System.out.println("Создано частей: " + parts.size());

    System.out.println("4. Объединение частей...");
    processor.mergeFiles(parts, "demo_merged.txt");
    System.out.println("Файл объединен в demo_merged.txt");

    Files.deleteIfExists(testFile);
    Files.deleteIfExists(Path.of("demo_analysis.txt"));
    Files.deleteIfExists(Path.of("demo_merged.txt"));
    for (Path part : parts) {
      Files.deleteIfExists(part);
    }
    Files.deleteIfExists(Path.of("demo_parts"));

    System.out.println("\n=== ДЕМО-ТЕСТ ЗАВЕРШЕН УСПЕШНО ===");
  }
}
