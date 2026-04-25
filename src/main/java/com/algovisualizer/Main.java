package com.algovisualizer;

import com.algovisualizer.algorithms.Dijkstra;
import com.algovisualizer.algorithms.BFS;
import com.algovisualizer.algorithms.TreeBFS;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.algovisualizer.algorithms.BubbleSort;
import com.algovisualizer.algorithms.QuickSort;
import com.algovisualizer.algorithms.MergeSort;
import com.algovisualizer.algorithms.SelectionSort;
import com.algovisualizer.algorithms.InsertionSort;
import com.algovisualizer.algorithms.HeapSort;
import com.algovisualizer.algorithms.PrimsMST;
import com.algovisualizer.algorithms.Algorithm;
import com.algovisualizer.algorithms.BaseAlgorithm;

public class Main extends Application {

    // Core Layout
    private BorderPane root;

    // View Components
    private SortingCanvas sortingCanvas;
    private GridCanvas gridCanvas;
    private TreeCanvas treeCanvas;
    private ControlPanel controlPanel;

    // Backend/Data
    private DatabaseHelper dbHelper;
    private int[] currentArray;

    // Thread Control
    private Thread currentThread;
    private Algorithm currentAlgorithm;

    @Override
    public void start(Stage stage) {
        // Initialize Database
        dbHelper = new DatabaseHelper();
        dbHelper.createTablesIfNotExist();

        root = new BorderPane();

        // Initialize Components
        sortingCanvas = new SortingCanvas();
        gridCanvas = new GridCanvas();
        treeCanvas = new TreeCanvas();
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

            if (selectedAlgo.contains("Sort") || selectedAlgo.equals("Custom Sandbox")) {
                root.setCenter(sortingCanvas);
                controlPanel.getGenerateBtn().setDisable(false); // Enable random array button
                gridCanvas.setWallDrawingEnabled(true);
            }
            else if (selectedAlgo.equals("Dijkstra") || selectedAlgo.equals("BFS")) {
                root.setCenter(gridCanvas);
                controlPanel.getGenerateBtn().setDisable(true); // Disable for grids (users draw walls)
                gridCanvas.setWallDrawingEnabled(true);
            }
            else if (selectedAlgo.equals("Prim MST")) {
                root.setCenter(gridCanvas);
                controlPanel.getGenerateBtn().setDisable(true);
                gridCanvas.setWallDrawingEnabled(false); // Disable wall drawing for Prim's
            }
            else if (selectedAlgo.equals("Tree BFS")) {
                root.setCenter(treeCanvas);
                controlPanel.getGenerateBtn().setDisable(true);
                gridCanvas.setWallDrawingEnabled(true);
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
            // Pause/Resume if an algorithm is already running
            if (currentThread != null && currentThread.isAlive()) {
                ((BaseAlgorithm) currentAlgorithm).togglePause();
                return;
            }

            String customArrayText = controlPanel.getCustomArrayField().getText();
            if (customArrayText != null && !customArrayText.trim().isEmpty()) {
                try {
                    String[] parts = customArrayText.split(",");
                    int[] parsedArray = new int[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        parsedArray[i] = Integer.parseInt(parts[i].trim());
                    }
                    currentArray = parsedArray;
                    sortingCanvas.drawArray(currentArray, -1, -1);
                } catch (NumberFormatException ex) {
                    System.err.println("Invalid custom array format.");
                    return;
                }
            }

            String selectedAlgo = controlPanel.getAlgoSelector().getValue();

            // --- SORTING LOGIC ---
            if (selectedAlgo.contains("Sort") || selectedAlgo.equals("Custom Sandbox")) {
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
                    currentAlgorithm = sort;
                    currentThread = new Thread(sort);
                    currentThread.setDaemon(true);
                    currentThread.start();
                } else if (selectedAlgo.equals("Custom Sandbox")) {
                    String userLogic = controlPanel.getCodeEditor().getText();
                    if (userLogic != null && !userLogic.trim().isEmpty()) {
                        try {
                            Algorithm customAlgo = new DynamicCompiler().compileAndLoad(userLogic, currentArray);
                            customAlgo.setCanvas(sortingCanvas);
                            customAlgo.setSpeed(currentSpeed);

                            // Dynamically update speed while running
                            controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                                customAlgo.setSpeed(101 - newVal.intValue());
                            });

                            // Start the algorithm engine
                            currentAlgorithm = customAlgo;
                            currentThread = new Thread(customAlgo);
                            currentThread.setDaemon(true);
                            currentThread.start();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } else if (selectedAlgo.equals("Quick Sort")) {
                    QuickSort sort = new QuickSort(currentArray);
                    sort.setCanvas(sortingCanvas);
                    sort.setSpeed(currentSpeed);

                    // Dynamically update speed while running
                    controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                        sort.setSpeed(101 - newVal.intValue());
                    });

                    // Log to database asynchronously
                    new Thread(() -> {
                        dbHelper.logExecution("Quick Sort", currentArray.length, 0);
                    }).start();

                    // Start the algorithm engine
                    currentAlgorithm = sort;
                    currentThread = new Thread(sort);
                    currentThread.setDaemon(true);
                    currentThread.start();
                } else if (selectedAlgo.equals("Merge Sort")) {
                    MergeSort sort = new MergeSort(currentArray);
                    sort.setCanvas(sortingCanvas);
                    sort.setSpeed(currentSpeed);

                    // Dynamically update speed while running
                    controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                        sort.setSpeed(101 - newVal.intValue());
                    });

                    // Log to database asynchronously
                    new Thread(() -> {
                        dbHelper.logExecution("Merge Sort", currentArray.length, 0);
                    }).start();

                    // Start the algorithm engine
                    currentAlgorithm = sort;
                    currentThread = new Thread(sort);
                    currentThread.setDaemon(true);
                    currentThread.start();
                } else if (selectedAlgo.equals("Selection Sort")) {
                    SelectionSort sort = new SelectionSort(currentArray);
                    sort.setCanvas(sortingCanvas);
                    sort.setSpeed(currentSpeed);

                    // Dynamically update speed while running
                    controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                        sort.setSpeed(101 - newVal.intValue());
                    });

                    // Log to database asynchronously
                    new Thread(() -> {
                        dbHelper.logExecution("Selection Sort", currentArray.length, 0);
                    }).start();

                    // Start the algorithm engine
                    currentAlgorithm = sort;
                    currentThread = new Thread(sort);
                    currentThread.setDaemon(true);
                    currentThread.start();
                } else if (selectedAlgo.equals("Insertion Sort")) {
                    InsertionSort sort = new InsertionSort(currentArray);
                    sort.setCanvas(sortingCanvas);
                    sort.setSpeed(currentSpeed);

                    // Dynamically update speed while running
                    controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                        sort.setSpeed(101 - newVal.intValue());
                    });

                    // Log to database asynchronously
                    new Thread(() -> {
                        dbHelper.logExecution("Insertion Sort", currentArray.length, 0);
                    }).start();

                    // Start the algorithm engine
                    currentAlgorithm = sort;
                    currentThread = new Thread(sort);
                    currentThread.setDaemon(true);
                    currentThread.start();
                } else if (selectedAlgo.equals("Heap Sort")) {
                    HeapSort sort = new HeapSort(currentArray);
                    sort.setCanvas(sortingCanvas);
                    sort.setSpeed(currentSpeed);

                    // Dynamically update speed while running
                    controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                        sort.setSpeed(101 - newVal.intValue());
                    });

                    // Log to database asynchronously
                    new Thread(() -> {
                        dbHelper.logExecution("Heap Sort", currentArray.length, 0);
                    }).start();

                    // Start the algorithm engine
                    currentAlgorithm = sort;
                    currentThread = new Thread(sort);
                    currentThread.setDaemon(true);
                    currentThread.start();
                }
            }


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
                currentAlgorithm = dijkstra;
                currentThread = new Thread(dijkstra);
                currentThread.setDaemon(true);
                currentThread.start();
            } else if (selectedAlgo.equals("BFS")) {
                int currentSpeed = 101 - (int) controlPanel.getSpeedSlider().getValue();

                BFS bfs = new BFS();
                bfs.setCanvas(gridCanvas);
                bfs.setSpeed(currentSpeed);

                // Dynamically update speed
                controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                    bfs.setSpeed(101 - newVal.intValue());
                });

                // Log execution (Size = total grid cells)
                new Thread(() -> {
                    dbHelper.logExecution("BFS", gridCanvas.getRows() * gridCanvas.getCols(), 0);
                }).start();

                // Run in background thread
                currentAlgorithm = bfs;
                currentThread = new Thread(bfs);
                currentThread.setDaemon(true);
                currentThread.start();
            } else if (selectedAlgo.equals("Prim MST")) {
                int currentSpeed = 101 - (int) controlPanel.getSpeedSlider().getValue();

                PrimsMST primsMST = new PrimsMST();
                primsMST.setCanvas(gridCanvas);
                primsMST.setSpeed(currentSpeed);

                // Dynamically update speed
                controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                    primsMST.setSpeed(101 - newVal.intValue());
                });

                // Log execution
                new Thread(() -> {
                    dbHelper.logExecution("Prim MST", gridCanvas.getRows() * gridCanvas.getCols(), 0);
                }).start();

                // Run in background thread
                currentAlgorithm = primsMST;
                currentThread = new Thread(primsMST);
                currentThread.setDaemon(true);
                currentThread.start();
            } else if (selectedAlgo.equals("Tree BFS")) {
                int currentSpeed = 101 - (int) controlPanel.getSpeedSlider().getValue();

                String input = controlPanel.getCustomInputText();
                if (input != null && !input.trim().isEmpty()) {
                    treeCanvas.buildTreeFromInput(input);
                } else {
                    treeCanvas.buildTreeFromInput("1,2,3,4,5,6,7");
                }

                TreeBFS treeBFS = new TreeBFS();
                treeBFS.setCanvas(treeCanvas);
                treeBFS.setSpeed(currentSpeed);

                // Dynamically update speed
                controlPanel.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
                    treeBFS.setSpeed(101 - newVal.intValue());
                });

                // Run in background thread
                currentAlgorithm = treeBFS;
                currentThread = new Thread(treeBFS);
                currentThread.setDaemon(true);
                currentThread.start();
            }
        });

        // 4. Reset Button Logic
        controlPanel.getResetBtn().setOnAction(e -> {
            if (currentAlgorithm != null) {
                ((BaseAlgorithm) currentAlgorithm).stopExecution();
            }
            if (currentThread != null && currentThread.isAlive()) {
                currentThread.interrupt();
            }

            String selectedAlgo = controlPanel.getAlgoSelector().getValue();

            if (selectedAlgo.contains("Sort") || selectedAlgo.equals("Custom Sandbox")) {
                currentArray = new int[50];
                for (int i = 0; i < currentArray.length; i++) {
                    currentArray[i] = (int) (Math.random() * 400) + 10;
                }
                sortingCanvas.drawArray(currentArray, -1, -1);
            } else if (selectedAlgo.equals("Dijkstra") || selectedAlgo.equals("BFS") || selectedAlgo.equals("Prim MST")) {
                gridCanvas = new GridCanvas();
                root.setCenter(gridCanvas);
            } else if (selectedAlgo.equals("Tree BFS")) {
                treeCanvas.buildTreeFromInput("1,2,3,4,5,6,7");
            }

            controlPanel.getPlayPauseBtn().setText("Play");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}