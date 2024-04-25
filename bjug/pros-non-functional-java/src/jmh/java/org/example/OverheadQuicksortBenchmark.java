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

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Thread)
public class OverheadQuicksortBenchmark {

  private int[] numbers;

  @Setup(Level.Trial)
  public void setUp() {
    numbers = new int[1000];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = numbers.length - i;
    }
  }

  @Benchmark
  public void stream(Blackhole bh) {
    bh.consume(OverheadQuicksort.functionalQuickSort(numbers));
  }

  @Benchmark
  public void forLoop(Blackhole bh) {
    bh.consume(OverheadQuicksort.imperativeQuickSort(numbers));
  }
}