package fileprocessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class TextFileAnalyzerTest {

  public static void main(String[] args) {
    try {
      testAnalyzeFile();
      testSaveAnalysisResult();
      System.out.println("✅ Все тесты TextFileAnalyzer прошли успешно!");
    } catch (Exception e) {
      System.out.println("❌ Тест провален: " + e.getMessage());
      e.printStackTrace();
    }
  }

  static void testAnalyzeFile() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = Path.of("test.txt");
    Files.write(testFile, Arrays.asList("Hello world!", "This is test."));

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    if (result.getLineCount() != 2) {
      throw new AssertionError("Ожидалось 2 строки, но получилось: " + result.getLineCount());
    }
    if (result.getWordCount() != 5) {
      throw new AssertionError("Ожидалось 5 слов, но получилось: " + result.getWordCount());
    }

    System.out.println("✅ testAnalyzeFile пройден успешно!");
  }

  static void testSaveAnalysisResult() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    java.util.Map<Character, Integer> frequency = new java.util.HashMap<>();
    frequency.put('T', 1);
    frequency.put('e', 1);
    frequency.put('s', 3);
    frequency.put('t', 1);

    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(2, 5, 20, frequency);

    Path outputFile = Path.of("analysis_result.txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    if (!Files.exists(outputFile)) {
      throw new AssertionError("Файл результатов не создан");
    }

    String content = Files.readString(outputFile);
    if (!content.contains("Line count: 2")) {
      throw new AssertionError("Не найдена информация о количестве строк");
    }

    Files.deleteIfExists(outputFile);
    System.out.println("✅ testSaveAnalysisResult пройден успешно!");
  }
}
