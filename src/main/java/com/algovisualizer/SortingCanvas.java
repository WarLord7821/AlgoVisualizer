package com.algovisualizer;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class SortingCanvas extends Pane {

    public SortingCanvas() {
        this.setStyle("-fx-background-color: #2c3e50;");
        this.setPrefSize(800, 500);
    }

    public void drawArray(int[] array, int activeIndex1, int activeIndex2) {
        Platform.runLater(() -> {
            this.getChildren().clear();

            if (array == null || array.length == 0) return;

            double width = this.getWidth() / array.length;
            double maxHeight = this.getHeight();

            // Find max value in array to scale heights proportionally
            int maxVal = 1;
            for (int val : array) {
                if (val > maxVal) maxVal = val;
            }

            for (int i = 0; i < array.length; i++) {
                // Scale height based on canvas size
                double barHeight = ((double) array[i] / maxVal) * (maxHeight - 20);

                Rectangle bar = new Rectangle(
                        i * width,
                        maxHeight - barHeight,
                        width - 2,
                        barHeight
                );

                if (i == activeIndex1 || i == activeIndex2) {
                    bar.setFill(Color.web("#e74c3c")); // Red for active/swapping
                } else {
                    bar.setFill(Color.web("#3498db")); // Blue for idle
                }

                this.getChildren().add(bar);
            }
        });
    }
}
