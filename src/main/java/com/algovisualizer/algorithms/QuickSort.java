package com.algovisualizer.algorithms;

import com.algovisualizer.SortingCanvas;

public class QuickSort extends BaseAlgorithm {

    private int[] array;

    public QuickSort(int[] array) {
        this.array = array;
    }

    @Override
    public void execute() {
        if (array == null || array.length == 0) return;
        quickSortRecursive(array, 0, array.length - 1);
        updateAndSleep(-1, -1); // Clear active indices when done
    }

    private void quickSortRecursive(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSortRecursive(arr, low, pi - 1);
            quickSortRecursive(arr, pi + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            updateAndSleep(j, high); // Highlight comparison against pivot
            if (arr[j] < pivot) {
                i++;
                swap(i, j);
                updateAndSleep(i, j); // Highlight the swap
            }
        }

        // Final pivot placement
        swap(i + 1, high);
        updateAndSleep(i + 1, high); // Highlight pivot swap into correct position

        return i + 1;
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void updateAndSleep(int active1, int active2) {
        ((SortingCanvas) canvas).drawArray(array, active1, active2);
        sleep(); // The inherited pause lock
    }

    @Override
    public void run() {
        execute();
    }
}
