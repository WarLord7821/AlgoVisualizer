package com.algovisualizer.algorithms;

import com.algovisualizer.SortingCanvas;


public class BubbleSort extends BaseAlgorithm {

    private int[] array;

    public BubbleSort(int[] array) {
        this.array = array;
    }

    @Override
    public void execute() {
        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                if (array[j] > array[j + 1]) {
                    swap(j, j + 1);
                }

                updateUI(j,j+1);
                sleep();
            }
        }
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void updateUI(int activeIndex1, int activeIndex2) {
        // Cast the generic Pane to our custom SortingCanvas and pass the data
        ((SortingCanvas) canvas).drawArray(array, activeIndex1, activeIndex2);
    }

    @Override
    public void run() {
        execute();
    }
}