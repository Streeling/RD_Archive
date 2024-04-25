package org.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Inspired from
 * https://www.baeldung.com/java-streams-vs-loops and
 * https://github.com/melix/jmh-gradle-plugin/blob/master/samples/simple-java/kotlin-dsl/src/jmh/java/me/champeau/jmh/SampleBenchmark.java
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class OverheadBenchmark {

  private List<Integer> numbers;

  @Setup(Level.Trial)
  public void setUp() {
    numbers = new ArrayList<>();
    for (int i = 0; i < 1_000_000; i++) {
      numbers.add(i);
    }
  }

  @Benchmark
  public void stream(Blackhole bh) {
    bh.consume(Overhead.stream(numbers));
  }

  @Benchmark
  public void forLoop(Blackhole bh) {
    bh.consume(Overhead.forLoop(numbers));
  }

  @Benchmark
  public void parallelStream(Blackhole bh) {
    bh.consume(Overhead.parallelStream(numbers));
  }

  @Benchmark
  public void concurrentForLoop(Blackhole bh) throws ExecutionException, InterruptedException {
    bh.consume(Overhead.concurrentForLoop(numbers));
  }
}
