package com.algovisualizer.algorithms;

import com.algovisualizer.SortingCanvas;

public class InsertionSort extends BaseAlgorithm {

    private int[] array;

    public InsertionSort(int[] array) {
        this.array = array;
    }

    @Override
    public void execute() {
        if (array == null || array.length == 0) return;
        insertionSort(array);
        updateAndSleep(-1, -1);
    }

    private void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
            
            updateAndSleep(i, j); // Highlight initial comparison

            while (j >= 0 && arr[j] > key) {
                updateAndSleep(j, j + 1); // Highlight elements being shifted
                arr[j + 1] = arr[j];
                j = j - 1;
                
                if (j >= 0) {
                    updateAndSleep(i, j); // Highlight comparison with previous element
                }
            }
            arr[j + 1] = key;
            updateAndSleep(j + 1, -1); // Highlight element placed in correct position
        }
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
