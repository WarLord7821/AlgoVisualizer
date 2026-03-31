package com.algovisualizer.algorithms;

import javafx.scene.layout.Pane;

public interface Algorithm extends Runnable {

    void execute();

    void setSpeed(int delayMs);

    void setCanvas(Pane canvas);
}


/* Note:
Every ALgorithm Implemetation must include:
@Override
public void run() {
    execute();
}
 */