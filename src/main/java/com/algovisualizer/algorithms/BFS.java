package com.algovisualizer.algorithms;

import com.algovisualizer.GridCanvas;
import java.util.LinkedList;
import java.util.Queue;

public class BFS extends BaseAlgorithm {

    private static class Node {
        int r, c;
        Node parent;
        Node(int r, int c, Node parent) {
            this.r = r; this.c = c; this.parent = parent;
        }
    }

    @Override
    public void execute() {
        GridCanvas gridCanvas = (GridCanvas) canvas;
        int[][] grid = gridCanvas.getGridState();
        int rows = gridCanvas.getRows();
        int cols = gridCanvas.getCols();

        Node startNode = null;
        Node endNode = null;

        // 1. Find Start (4) and End (5) nodes
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 4) startNode = new Node(r, c, null);
                if (grid[r][c] == 5) endNode = new Node(r, c, null);
            }
        }

        if (startNode == null || endNode == null) return;

        // 2. Setup BFS Data Structures
        Queue<Node> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];

        queue.add(startNode);
        visited[startNode.r][startNode.c] = true;
        
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

        // 3. Execution Loop
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // If we hit the target, draw the final path!
            if (current.r == endNode.r && current.c == endNode.c) {
                drawFinalPath(current, gridCanvas);
                return;
            }

            // Mark as visited on UI (Color = Blue)
            if (grid[current.r][current.c] != 4 && grid[current.r][current.c] != 5) {
                gridCanvas.updateCell(current.r, current.c, 2);
                sleep(); // Pause so the user can watch the search expand
            }

            // Check neighbors
            for (int[] d : dirs) {
                int nr = current.r + d[0];
                int nc = current.c + d[1];

                // Boundary check and Wall (1) check
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc] && grid[nr][nc] != 1) {
                    visited[nr][nc] = true; // Mark visited as soon as it's added to queue
                    queue.add(new Node(nr, nc, current));
                }
            }
        }
    }

    private void drawFinalPath(Node endNode, GridCanvas gridCanvas) {
        Node current = endNode.parent; // Skip the actual end node
        while (current != null && current.parent != null) { // Stop before start node
            gridCanvas.updateCell(current.r, current.c, 3); // 3 = Path (Yellow)
            sleep(); // Animate the path drawing backwards
            current = current.parent;
        }
    }

    @Override
    public void run() { execute(); }
}
