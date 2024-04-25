package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Copied from https://www.baeldung.com/java-streams-vs-loops
 */
class Overhead {

    static int forLoop(List<Integer> numbers) {
        int sum = 0;
        for (int number : numbers) {
            if (number % 2 == 0) {
                sum = sum + (number * number);
            }
        }
        return sum;
    }

    static int stream(List<Integer> numbers) {
        return numbers.stream()
            .filter(number -> number % 2 == 0)
            .map(number -> number * number)
            .reduce(0, Integer::sum);
    }

    static int parallelStream(List<Integer> numbers) {
        return numbers.parallelStream()
          .filter(number -> number % 2 == 0)
          .map(number -> number * number)
          .reduce(0, Integer::sum);
    }

    static int concurrentForLoop(List<Integer> numbers) throws InterruptedException, ExecutionException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Callable<Integer>> tasks = new ArrayList<>();
        int chunkSize = numbers.size() / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize;
            final int end = (i == numThreads - 1) ? numbers.size() : (i + 1) * chunkSize;
            tasks.add(() -> {
                int sum = 0;
                for (int j = start; j < end; j++) {
                    int number = numbers.get(j);
                    if (number % 2 == 0) {
                        sum = sum + (number * number);
                    }
                }
                return sum;
            });
        }

        int totalSum = 0;
        for (Future<Integer> result : executorService.invokeAll(tasks)) {
            totalSum += result.get();
        }

        executorService.shutdown();
        return totalSum;
    }
}