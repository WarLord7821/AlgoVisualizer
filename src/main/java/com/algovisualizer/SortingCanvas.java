package com.algovisualizer;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class SortingCanvas extends Pane {

    public void drawArray(int[] array, int activeIndex1, int activeIndex2) {
        // Clear the canvas for the next frame
        this.getChildren().clear();

        if (array == null || array.length == 0) return;

        double canvasWidth = this.getWidth();
        double canvasHeight = this.getHeight();
        double barWidth = canvasWidth / array.length;

        for (int i = 0; i < array.length; i++) {
            // Height corresponds to array value
            double barHeight = array[i];

            Rectangle rect = new Rectangle(barWidth, barHeight);

            // Positioning: X based on index, Y so it rests at the bottom
            rect.setX(i * barWidth);
            rect.setY(canvasHeight - barHeight);

            // Color logic: #00ff88 for active indices, #3b82f6 for others
            if (i == activeIndex1 || i == activeIndex2) {
                rect.setFill(Color.web("#00ff88"));
            } else {
                rect.setFill(Color.web("#3b82f6"));
            }

            this.getChildren().add(rect);
        }
    }
}
