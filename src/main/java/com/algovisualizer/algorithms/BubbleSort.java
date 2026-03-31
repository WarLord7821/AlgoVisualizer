package com.algovisualizer.algorithms;

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
        Platform.runLater(() -> {
            canvas.getChildren().clear();

            double width = canvas.getWidth() / array.length;

            for (int i = 0; i < array.length; i++) {
                Rectangle bar = new Rectangle(
                        i * width,
                        canvas.getHeight() - array[i],
                        width - 2,
                        array[i]
                );

                if (i == activeIndex1 || i == activeIndex2) {
                    bar.setStyle("-fx-fill: red;");
                } else {
                    bar.setStyle("-fx-fill: blue;");
                }

                canvas.getChildren().add(bar);
            }
        });
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