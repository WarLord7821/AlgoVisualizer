package com.algovisualizer.algorithms;

import com.algovisualizer.SortingCanvas;

public class HeapSort extends BaseAlgorithm {

    private int[] array;

    public HeapSort(int[] array) {
        this.array = array;
    }

    @Override
    public void execute() {
        if (array == null || array.length == 0) return;
        heapSort(array);
        updateAndSleep(-1, -1);
    }

    private void heapSort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            swap(0, i);
            updateAndSleep(0, i); // Highlight swapping max element to the end
            heapify(arr, i, 0);
        }
    }

    private void heapify(int[] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n) {
            updateAndSleep(l, largest); // Highlight comparison
            if (arr[l] > arr[largest]) {
                largest = l;
            }
        }

        if (r < n) {
            updateAndSleep(r, largest); // Highlight comparison
            if (arr[r] > arr[largest]) {
                largest = r;
            }
        }

        if (largest != i) {
            swap(i, largest);
            updateAndSleep(i, largest); // Highlight swap in heap
            heapify(arr, n, largest);
        }
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void updateAndSleep(int active1, int active2) {
        ((SortingCanvas) canvas).drawArray(array, active1, active2);
        sleep();
    }

    @Override
    public void run() {
        execute();
    }
}
