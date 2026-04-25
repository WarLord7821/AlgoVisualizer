package com.algovisualizer.algorithms;

import javafx.scene.layout.Pane;

public abstract class BaseAlgorithm implements Algorithm {

    protected int delay = 50;
    protected Pane canvas;

    protected volatile boolean isPaused = false;
    protected volatile boolean isStopped = false;
    protected final Object pauseLock = new Object();

    @Override
    public void setSpeed(int delayMs) {
        this.delay = delayMs;
    }

    @Override
    public void setCanvas(Pane canvas) {
        this.canvas = canvas;
    }

    public void togglePause() {
        if (isPaused) {
            isPaused = false;
            synchronized (pauseLock) {
                pauseLock.notify();
            }
        } else {
            isPaused = true;
        }
    }

    public void stopExecution() {
        isStopped = true;
        synchronized (pauseLock) {
            isPaused = false;
            pauseLock.notifyAll();
        }
    }

    protected void sleep() {
        if (isStopped) {
            Thread.currentThread().interrupt();
        }
        synchronized (pauseLock) {
            while (isPaused) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    if (isStopped) return;
                }
            }
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (isStopped) return;
        }
    }
}
