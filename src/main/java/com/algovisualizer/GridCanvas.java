package com.algovisualizer;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

public class GridCanvas extends Pane {
    private final int ROWS = 20;
    private final int COLS = 40;
    private final double CELL_SIZE = 20.0;

    // 0 = Empty, 1 = Wall, 2 = Visited, 3 = Path, 4 = Start, 5 = End
    private int[][] gridState;
    private Rectangle[][] cells;

    public GridCanvas() {
        this.setStyle("-fx-background-color: #ecf0f1;");
        this.setPrefSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);
        gridState = new int[ROWS][COLS];
        cells = new Rectangle[ROWS][COLS];
        initializeGrid();
        setupMouseEvents();
    }

    private void initializeGrid() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Rectangle rect = new Rectangle(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.LIGHTGRAY);

                cells[r][c] = rect;
                this.getChildren().add(rect);
            }
        }

        // Set default Start (Green) and End (Red) points
        gridState[10][5] = 4;
        cells[10][5].setFill(Color.web("#2ecc71"));

        gridState[10][35] = 5;
        cells[10][35].setFill(Color.web("#e74c3c"));
    }

    private void setupMouseEvents() {
        this.setOnMouseDragged(this::handleDrawWall);
        this.setOnMouseClicked(this::handleDrawWall);
    }

    private void handleDrawWall(MouseEvent e) {
        int c = (int) (e.getX() / CELL_SIZE);
        int r = (int) (e.getY() / CELL_SIZE);

        if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
            // Don't overwrite Start or End nodes
            if (gridState[r][c] != 4 && gridState[r][c] != 5) {
                gridState[r][c] = 1; // Set as wall
                cells[r][c].setFill(Color.web("#34495e")); // Dark grey wall
            }
        }
    }

    // Method for the algorithm thread to call to update cell colors
    public void updateCell(int r, int c, int state) {
        Platform.runLater(() -> {
            if (gridState[r][c] != 4 && gridState[r][c] != 5) {
                gridState[r][c] = state;
                if (state == 2) cells[r][c].setFill(Color.web("#3498db")); // Visited
                if (state == 3) cells[r][c].setFill(Color.web("#f1c40f")); // Final Path
            }
        });
    }

    public int[][] getGridState() { return gridState; }
    public int getRows() { return ROWS; }
    public int getCols() { return COLS; }
}