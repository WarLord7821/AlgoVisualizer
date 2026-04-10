package com.algovisualizer;

import com.algovisualizer.algorithms.Dijkstra;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.algovisualizer.algorithms.BubbleSort;

public class Main extends Application {

    // Core Layout
    private BorderPane root;

    // View Components
    private SortingCanvas sortingCanvas;
    private GridCanvas gridCanvas;
    private ControlPanel controlPanel;

    // Backend/Data
    private DatabaseHelper dbHelper;
    private int[] currentArray;

    @Override
    public void start(Stage stage) {
        // Initialize Database
        dbHelper = new DatabaseHelper();
        dbHelper.createTablesIfNotExist();

        root = new BorderPane();

        // Initialize Components
        sortingCanvas = new SortingCanvas();
        gridCanvas = new GridCanvas();
        controlPanel = new ControlPanel();

        // Mount Default Components (Starts on Sorting View)
        root.setCenter(sortingCanvas);
        root.setBottom(controlPanel);

        // Bind Button Logic
        setupEventHandlers();

        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Algorithm Visualizer - Sprint 3");
        stage.setScene(scene);
        stage.show();
    }

    private void setupEventHandlers() {

        // 1. View Router Logic (Dropdown Listener)
        controlPanel.getAlgoSelector().setOnAction(e -> {
            String selectedAlgo = controlPanel.getAlgoSelector().getValue();

            if (selectedAlgo.contains("Sort")) {
                root.setCenter(sortingCanvas);
                controlPanel.getGenerateBtn().setDisable(false); // Enable random array button
            } else if (selectedAlgo.equals("Dijkstra") || selectedAlgo.equals("BFS")) {
                root.setCenter(gridCanvas);
                controlPanel.getGenerateBtn().setDisable(true); // Disable for grids (users draw walls)
            }
        });

        // 2. Generate Random Array
        controlPanel.getGenerateBtn().setOnAction(e -> {
            currentArray = new int[50];
            for (int i = 0; i < currentArray.length; i++) {
                currentArray[i] = (int) (Math.random() * 400) + 10;
            }
            sortingCanvas.drawArray(currentArray, -1, -1);
        });

        // 3. Play Button (Start Sort or Pathfinding)
        controlPanel.getPlayPauseBtn().setOnAction(e -> {
            String selectedAlgo = controlPanel.getAlgoSelector().getValue();

            // --- SORTING LOGIC ---
            if (selectedAlgo.contains("Sort")) {
                if (currentArray == null) return;

                int currentSpeed = 101 - (int) controlPanel.getSpeedSlider().getValue();

                if (selectedAlgo.equals("Bubble Sort")) {
                    BubbleSort sort = new BubbleSort(currentArray);
                    sort.setCanvas(sortingCanvas);
                    sort.setSpeed(currentSpeed);

                    // Dynamically update speed while running
                    controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                        sort.setSpeed(101 - newVal.intValue());
                    });

                    // Log to database asynchronously
                    new Thread(() -> {
                        dbHelper.logExecution("Bubble Sort", currentArray.length, 0);
                    }).start();

                    // Start the algorithm engine
                    Thread engineThread = new Thread(sort);
                    engineThread.setDaemon(true);
                    engineThread.start();
                }
                // (Quick Sort will be added here later)
            }

            // --- PATHFINDING LOGIC ---
            // --- PATHFINDING LOGIC ---
            else if (selectedAlgo.equals("Dijkstra")) {
                int currentSpeed = 101 - (int) controlPanel.getSpeedSlider().getValue();

                Dijkstra dijkstra = new Dijkstra();
                dijkstra.setCanvas(gridCanvas);
                dijkstra.setSpeed(currentSpeed);

                // Dynamically update speed
                controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                    dijkstra.setSpeed(101 - newVal.intValue());
                });

                // Log execution (Size = total grid cells)
                new Thread(() -> {
                    dbHelper.logExecution("Dijkstra", gridCanvas.getRows() * gridCanvas.getCols(), 0);
                }).start();

                // Run in background thread
                Thread pathThread = new Thread(dijkstra);
                pathThread.setDaemon(true);
                pathThread.start();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}