package com.algovisualizer.algorithms;

import com.algovisualizer.SortingCanvas;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class BubbleSort implements Algorithm {

    private int[] array;
    private int delay = 100;
    private Pane canvas;

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

    private void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void setSpeed(int delayMs) {
        this.delay = delayMs;
    }

    @Override
    public void setCanvas(Pane canvas) {
        this.canvas = canvas;
    }

    @Override
    public void run() {
        execute();
    }
}