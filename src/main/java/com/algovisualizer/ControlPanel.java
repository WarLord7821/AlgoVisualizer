package com.algovisualizer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class ControlPanel extends HBox {

    private ComboBox<String> algoSelector;
    private Button generateBtn;
    private Button stepBackBtn;
    private Button playPauseBtn;
    private Button stepForwardBtn;
    private Slider speedSlider;

    public ControlPanel() {
        // Layout Properties
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #1e1e1e;");

        // Algorithm Selector
        algoSelector = new ComboBox<>();
        algoSelector.getItems().addAll("Bubble Sort", "Quick Sort", "Merge Sort", "Dijkstra");
        algoSelector.getSelectionModel().selectFirst();
        algoSelector.setStyle("-fx-background-radius: 5;");

        // Buttons
        generateBtn = createStyledButton("Generate Random", "#3498db");
        stepBackBtn = createStyledButton("Step Back", "#7f8c8d");
        playPauseBtn = createStyledButton("Play/Pause", "#2ecc71");
        stepForwardBtn = createStyledButton("Step Forward", "#7f8c8d");

        // Speed Slider
        speedSlider = new Slider(1, 100, 50);
        speedSlider.setShowTickLabels(true);
        speedSlider.setPrefWidth(150);

        // Assemble Components
        this.getChildren().addAll(
                algoSelector, generateBtn, stepBackBtn,
                playPauseBtn, stepForwardBtn, speedSlider
        );
    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand;"
        );
        return btn;
    }

    // Public Getters
    public ComboBox<String> getAlgoSelector() { return algoSelector; }
    public Button getGenerateBtn() { return generateBtn; }
    public Button getStepBackBtn() { return stepBackBtn; }
    public Button getPlayPauseBtn() { return playPauseBtn; }
    public Button getStepForwardBtn() { return stepForwardBtn; }
    public Slider getSpeedSlider() { return speedSlider; }
}