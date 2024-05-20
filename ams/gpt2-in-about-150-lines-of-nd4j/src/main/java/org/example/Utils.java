package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

public class Utils {

  private Utils() {

  }

  /**
   * Copied from https://github.com/jaymody/picoGPT/blob/817292baea75f194fb0bb8ba2aa5f947af4e45ee/utils.py#L68
   */
  static Map<String, Object> loadHparamsJson(Path modelDir) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Path hparamsPath = modelDir.resolve("hparams.json");
    File hparamsFile = hparamsPath.toFile();
    Map<String, Object> hparams = objectMapper.readValue(hparamsFile, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
    return hparams;
  }

  /**
   * Copied and adapted from ChatGPT answer to "Implement a visually appealing progress display in a Java console application.
   * Inspired from python tqdm".
   */
  static void updateProgress(String msg, long progress, long total) {
    final int barLength = 40;
    float percent = (float) progress / total;
    int numChars = (int) (percent * barLength);

    StringBuilder progressBar = new StringBuilder(msg + ": [");
    for (int i = 0; i < barLength; i++) {
      if (i < numChars) {
        progressBar.append("=");
      } else {
        progressBar.append(" ");
      }
    }
    progressBar.append("] " + (int) (percent * 100) + "%");
    System.out.print("\r" + progressBar);
  }

  /**
   * Copied and adapted from Chat GPT answer to the question "I have a large file that I'm loading using the Jackson library in Java, and it takes some time to load.
   * Is there a way to display the progress of reading the file while it's being loaded?"
   */
  public static class ProgressTrackingInputStream extends InputStream {

    private final InputStream inputStream;
    private final long totalBytes;
    private long bytesRead;
    private final Consumer<Long> progressCallback;

    public ProgressTrackingInputStream(InputStream inputStream, long totalBytes, Consumer<Long> progressCallback) {
      this.inputStream = inputStream;
      this.totalBytes = totalBytes;
      this.progressCallback = progressCallback;
    }

    @Override
    public int read() throws IOException {
      int byteRead = inputStream.read();
      if (byteRead != -1) {
        bytesRead++;
        updateProgress();
      }
      return byteRead;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      int bytesRead = inputStream.read(b, off, len);
      if (bytesRead > 0) {
        this.bytesRead += bytesRead;
        updateProgress();
      }
      return bytesRead;
    }

    private void updateProgress() {
      if (progressCallback != null && totalBytes > 0) {
        progressCallback.accept(bytesRead);
      }
    }

    @Override
    public void close() throws IOException {
      inputStream.close();
    }
  }
}