package com.algovisualizer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class ControlPanel extends VBox {

    private ComboBox<String> algoSelector;
    private Button generateBtn;
    private Button stepBackBtn;
    private Button playPauseBtn;
    private Button stepForwardBtn;
    private Slider speedSlider;
    private TextField customArrayField;
    private TextArea codeEditor;

    public ControlPanel() {
        // Layout Properties
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #1e1e1e;");

        // Algorithm Selector
        algoSelector = new ComboBox<>();
        algoSelector.getItems().addAll("Bubble Sort", "Quick Sort", "Merge Sort", "Dijkstra", "Custom Sandbox");
        algoSelector.getSelectionModel().selectFirst();
        algoSelector.setStyle("-fx-background-radius: 5;");

        // Buttons
        generateBtn = createStyledButton("Generate Random", "#3498db");
        stepBackBtn = createStyledButton("Step Back", "#7f8c8d");
        playPauseBtn = createStyledButton("Play/Pause", "#2ecc71");
        stepForwardBtn = createStyledButton("Step Forward", "#7f8c8d");

        // Custom Array Input
        customArrayField = new TextField();
        customArrayField.setPromptText("Custom Array (comma-separated)");

        // Speed Slider
        speedSlider = new Slider(1, 100, 50);
        speedSlider.setShowTickLabels(true);
        speedSlider.setPrefWidth(150);

        // Assemble Components
        HBox topRow = new HBox(15);
        topRow.setAlignment(Pos.CENTER);
        topRow.getChildren().addAll(
                algoSelector, generateBtn, customArrayField, stepBackBtn,
                playPauseBtn, stepForwardBtn, speedSlider
        );

        codeEditor = new TextArea();
        codeEditor.setPrefHeight(150);
        codeEditor.setPromptText("Write custom algorithm logic here...");

        this.getChildren().addAll(topRow, codeEditor);
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
    public TextField getCustomArrayField() { return customArrayField; }
    public TextArea getCodeEditor() { return codeEditor; }
}