package com.algovisualizer.algorithms;

import com.algovisualizer.GridCanvas;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Dijkstra implements Algorithm {

    private GridCanvas canvas;
    private int delay = 20; // Default speed

    private static class Node {
        int r, c, dist;
        Node parent;
        Node(int r, int c, int dist, Node parent) {
            this.r = r; this.c = c; this.dist = dist; this.parent = parent;
        }
    }

    @Override
    public void execute() {
        int[][] grid = canvas.getGridState();
        int rows = canvas.getRows();
        int cols = canvas.getCols();

        Node startNode = null;
        Node endNode = null;

        // 1. Find Start (4) and End (5) nodes
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 4) startNode = new Node(r, c, 0, null);
                if (grid[r][c] == 5) endNode = new Node(r, c, 0, null);
            }
        }

        if (startNode == null || endNode == null) return;

        // 2. Setup Dijkstra Data Structures
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));
        boolean[][] visited = new boolean[rows][cols];
        Node[][] parents = new Node[rows][cols];

        pq.add(startNode);
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

        // 3. Execution Loop
        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (visited[current.r][current.c]) continue;
            visited[current.r][current.c] = true;

            // If we hit the target, draw the final path!
            if (current.r == endNode.r && current.c == endNode.c) {
                drawFinalPath(current);
                return;
            }

            // Mark as visited on UI (Color = Blue)
            if (grid[current.r][current.c] != 4) {
                canvas.updateCell(current.r, current.c, 2);
                sleep(); // Pause so the user can watch the search expand
            }

            // Check neighbors
            for (int[] d : dirs) {
                int nr = current.r + d[0];
                int nc = current.c + d[1];

                // Boundary check and Wall (1) check
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc] && grid[nr][nc] != 1) {
                    pq.add(new Node(nr, nc, current.dist + 1, current));
                }
            }
        }
    }

    private void drawFinalPath(Node endNode) {
        Node current = endNode.parent; // Skip the actual end node
        while (current != null && current.parent != null) { // Stop before start node
            canvas.updateCell(current.r, current.c, 3); // 3 = Path (Yellow)
            sleep(); // Animate the path drawing backwards
            current = current.parent;
        }
    }

    private void sleep() {
        try { Thread.sleep(delay); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    @Override
    public void setSpeed(int delayMs) { this.delay = delayMs; }

    @Override
    public void setCanvas(Pane canvas) { this.canvas = (GridCanvas) canvas; }

    @Override
    public void run() { execute(); }
}