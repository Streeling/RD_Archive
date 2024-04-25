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
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class BuildingBlocksParallelReductionBenchmark {

  private int[] numbers;

  @Setup(Level.Trial)
  public void setUp() {
//      numbers = new int[1_000_000];
    numbers = new int[600_000];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = i;
    }
  }

  @Benchmark
  public void functionalSum(Blackhole bh) {
    bh.consume(BuildingBlocksParallelReduction.functionalSum(numbers));
  }

  @Benchmark
  public void imperativeSum(Blackhole bh) {
    bh.consume(BuildingBlocksParallelReduction.imperativeSum(numbers));
  }

}
