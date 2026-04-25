package com.algovisualizer.algorithms;

import com.algovisualizer.SortingCanvas;

public class SelectionSort extends BaseAlgorithm {

    private int[] array;

    public SelectionSort(int[] array) {
        this.array = array;
    }

    @Override
    public void execute() {
        if (array == null || array.length == 0) return;
        selectionSort(array);
        updateAndSleep(-1, -1);
    }

    private void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                updateAndSleep(j, min_idx); // Highlight comparison
                if (arr[j] < arr[min_idx]) {
                    min_idx = j;
                }
            }
            if (min_idx != i) {
                swap(i, min_idx);
                updateAndSleep(i, min_idx); // Highlight swap
            }
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
