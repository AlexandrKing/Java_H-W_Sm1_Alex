package fileprocessing;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

  public List<Path> splitFile(String sourcePath, String outputDir, int partSize) throws IOException {
    List<Path> partPaths = new ArrayList<>();
    Path sourceFile = Paths.get(sourcePath);
    String fileName = sourceFile.getFileName().toString();

    try (FileChannel sourceChannel = FileChannel.open(sourceFile, StandardOpenOption.READ)) {
      long fileSize = sourceChannel.size();
      int partNumber = 1;
      long position = 0;

      while (position < fileSize) {
        String partFileName = fileName + ".part" + partNumber;
        Path partPath = Paths.get(outputDir, partFileName);

        try (FileChannel partChannel = FileChannel.open(partPath,
            StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

          long bytesToWrite = Math.min(partSize, fileSize - position);
          long transferred = 0;

          while (transferred < bytesToWrite) {
            transferred += sourceChannel.transferTo(position + transferred,
                bytesToWrite - transferred, partChannel);
          }

          position += transferred;
        }

        partPaths.add(partPath);
        partNumber++;
      }
    }

    return partPaths;
  }

  public void mergeFiles(List<Path> partPaths, String outputPath) throws IOException {
    Path outputFile = Paths.get(outputPath);

    try (FileChannel outputChannel = FileChannel.open(outputFile,
        StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

      for (Path partPath : partPaths) {
        if (!Files.exists(partPath)) {
          throw new IOException("Part file not found: " + partPath);
        }

        try (FileChannel partChannel = FileChannel.open(partPath, StandardOpenOption.READ)) {
          long position = outputChannel.size();
          long bytesToTransfer = partChannel.size();
          long transferred = 0;

          while (transferred < bytesToTransfer) {
            transferred += partChannel.transferTo(transferred,
                bytesToTransfer - transferred, outputChannel);
          }
        }
      }
    }
  }
}
