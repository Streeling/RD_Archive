package org.example;

import java.util.Arrays;
import java.util.stream.IntStream;

class OverheadQuicksort {

  static int[] functionalQuickSort(int[] numbers) {
    if (numbers.length <= 1) {
      return numbers;
    } else {
      var pivotIndex = 0;
      return IntStream.concat(
          IntStream.concat(
              Arrays.stream(functionalQuickSort(Arrays.stream(numbers).filter(it -> it < numbers[pivotIndex]).toArray())),
              IntStream.of(numbers[pivotIndex])
          ),
          Arrays.stream(functionalQuickSort(Arrays.stream(numbers).filter(it -> it > numbers[pivotIndex]).toArray()))
      ).toArray();
    }
  }

  static int[] imperativeQuickSort(int[] numbers) {
    quickSort(numbers, 0, numbers.length - 1);
    return numbers;
  }

  private static void quickSort(int[] arr, int startIndex, int endIndex) {
    if (startIndex >= endIndex) {
      return;
    }

    int pivotIndex = partition(arr, startIndex, endIndex);

    quickSort(arr, startIndex, pivotIndex - 1);
    quickSort(arr, pivotIndex + 1, endIndex);
  }

  private static int partition(int[] arr, int startIndex, int endIndex) {
    int pivot = arr[endIndex];

    int newi = startIndex - 1; // we suppose that the new pivot index is here, those we assume that all elements must be greater than pivot here if not, we will swap it
    int i = startIndex;
    while(i < endIndex) {
      if (arr[i] < pivot) {
        newi++;
        sw(arr, i, newi);
      }
      i++;
    }
    sw(arr, newi + 1, endIndex);
    return newi + 1;
  }

  private static void sw(int[] arr, int i, int j) {
    int x = arr[i];
    arr[i] = arr[j];
    arr[j] = x;
  }
}