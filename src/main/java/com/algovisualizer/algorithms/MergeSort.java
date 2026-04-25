package com.algovisualizer.algorithms;

import com.algovisualizer.SortingCanvas;

public class MergeSort extends BaseAlgorithm {

    private int[] array;

    public MergeSort(int[] array) {
        this.array = array;
    }

    @Override
    public void execute() {
        if (array == null || array.length == 0) return;
        mergeSortRecursive(array, 0, array.length - 1);
        updateAndSleep(-1); // Clear active index when done
    }

    private void mergeSortRecursive(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortRecursive(arr, left, mid);
            mergeSortRecursive(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        // Merge L and R back into arr
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i++];
            } else {
                arr[k] = R[j++];
            }
            updateAndSleep(k); // Visualize element placed at position k
            k++;
        }

        // Copy remaining elements of L
        while (i < n1) {
            arr[k] = L[i++];
            updateAndSleep(k); // Visualize element placed at position k
            k++;
        }

        // Copy remaining elements of R
        while (j < n2) {
            arr[k] = R[j++];
            updateAndSleep(k); // Visualize element placed at position k
            k++;
        }
    }

    private void updateAndSleep(int activeIndex) {
        ((SortingCanvas) canvas).drawArray(array, activeIndex, -1);
        sleep(); // The inherited pause lock
    }

    @Override
    public void run() {
        execute();
    }
}
