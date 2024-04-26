package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class Utils {

  /**
   * Copied from ChatGPT answer to "Implement a visually appealing progress display in a Java console application.
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
   * Copied from Chat GPT answer to the question "I have a large file that I'm loading using the Jackson library in Java, and it takes some time to load. Is there a way to display
   * the progress of reading the file while it's being loaded?"
   */
  public static class ProgressTrackingInputStream extends InputStream {

    private final InputStream inputStream;
    private final long totalBytes;
    private long bytesRead;
//    private Consumer<Double> progressCallback;
    private Consumer<Long> progressCallback;

//    public ProgressTrackingInputStream(InputStream inputStream, long totalBytes, Consumer<Double> progressCallback) {
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
//        double progress = (double) bytesRead / totalBytes;
//        progressCallback.accept(progress);
        progressCallback.accept(bytesRead);
      }
    }

    @Override
    public void close() throws IOException {
      inputStream.close();
    }
  }
}