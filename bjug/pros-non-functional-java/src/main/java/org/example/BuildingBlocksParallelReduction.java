package org.example;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Copied from ChatGPT
 */
class BuildingBlocksParallelReduction {

  static int functionalSum(int[] numbers) {
    int[] array = numbers;
    AtomicInteger sum = new AtomicInteger(0);

    // Parallel reduction using AtomicInteger for accumulation
    IntStream.range(0, array.length)
        .parallel()
        .forEach(i -> sum.addAndGet(array[i]));

    return sum.get();
  }

  static int imperativeSum(int[] numbers) {
    int[] array = numbers;

    // Using a ForkJoinPool for parallel reduction
    ForkJoinPool pool = new ForkJoinPool();
    return pool.invoke(new SumTask(array, 0, array.length));
  }

  static class SumTask extends RecursiveTask<Integer> {
    private final int[] array;
    private final int start;
    private final int end;
    private static final int THRESHOLD = 2;

    public SumTask(int[] array, int start, int end) {
      this.array = array;
      this.start = start;
      this.end = end;
    }

    @Override
    protected Integer compute() {
      if (end - start <= THRESHOLD) {
        return Arrays.stream(array, start, end).sum();
      } else {
        int mid = start + (end - start) / 2;
        SumTask leftTask = new SumTask(array, start, mid);
        SumTask rightTask = new SumTask(array, mid, end);

        leftTask.fork();
        int rightResult = rightTask.compute();
        int leftResult = leftTask.join();

        return leftResult + rightResult;
      }
    }
  }
}