package com.algovisualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import com.algovisualizer.algorithms.BubbleSort;

public class Main extends Application {
    private Pane centerCanvas;
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // Top → Header
        root.setTop(createHeader());

        // Center → Visualization Area (Mazin)
        root.setCenter(createCenter());

        // Bottom → Controls (Pawan)
        root.setBottom(createControls());

        // Right → History Panel (Anushka)
        root.setRight(createSidePanel());

        Scene scene = new Scene(root, 1000, 600);

        stage.setTitle("Algorithm Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    private Pane createHeader() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #2c3e50;");
        pane.setPrefHeight(50);
        return pane;
    }

    private Pane createCenter() {
        centerCanvas = new Pane();
        centerCanvas.setStyle("-fx-background-color: #ecf0f1;");
        centerCanvas.setPrefSize(800, 500);

        return centerCanvas;
    }
    private Pane createControls() {
        HBox controls = new HBox(10);
        controls.setStyle("-fx-background-color: #bdc3c7;");
        controls.setPrefHeight(80);

        Button startBtn = new Button("Start");
        Button resetBtn = new Button("Reset");

        Slider speedSlider = new Slider(10, 500, 100);
        speedSlider.setPrefWidth(200);

        controls.getChildren().addAll(startBtn, resetBtn, speedSlider);

        startBtn.setOnAction(e -> {
            int[] array = new int[50];

            // generate random values
            for (int i = 0; i < array.length; i++) {
                array[i] = (int) (Math.random() * 400);
            }


            BubbleSort sort = new BubbleSort(array);
            sort.setCanvas(centerCanvas);
            speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                sort.setSpeed(newVal.intValue());
            });

            Thread thread = new Thread(sort);
            thread.setDaemon(true);
            thread.start();
        });

        resetBtn.setOnAction(e -> {
            centerCanvas.getChildren().clear();
        });

        return controls;
    }

    private Pane createSidePanel() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #95a5a6;");
        pane.setPrefWidth(200);
        return pane;
    }

    public static void main(String[] args) {
        launch();
    }
}