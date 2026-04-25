package com.algovisualizer.algorithms;

import com.algovisualizer.GridCanvas;
import java.util.PriorityQueue;
import java.util.Comparator;

public class PrimsMST extends BaseAlgorithm {

    private static class Node {
        int r, c, weight;
        Node(int r, int c, int weight) {
            this.r = r;
            this.c = c;
            this.weight = weight;
        }
    }

    @Override
    public void execute() {
        GridCanvas gridCanvas = (GridCanvas) canvas;
        int[][] grid = gridCanvas.getGridState();
        int[][] weights = gridCanvas.getWeights();
        int rows = gridCanvas.getRows();
        int cols = gridCanvas.getCols();

        boolean[][] inMST = new boolean[rows][cols];
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.weight));

        // Start from top-left corner (0,0) or the first non-wall cell
        int startR = 0, startC = 0;
        boolean found = false;
        for (int r = 0; r < rows && !found; r++) {
            for (int c = 0; c < cols && !found; c++) {
                if (grid[r][c] != 1) { // Not a wall
                    startR = r;
                    startC = c;
                    found = true;
                }
            }
        }

        if (!found) return; // Entire grid is walls

        // Seed the PQ with the starting node
        pq.add(new Node(startR, startC, weights[startR][startC]));

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (inMST[current.r][current.c]) continue;
            inMST[current.r][current.c] = true;

            // Mark as MST node (state 3 = Path/Yellow)
            gridCanvas.updateCell(current.r, current.c, 3);
            sleep();

            // Add all adjacent non-wall, non-MST neighbors to the PQ
            for (int[] d : dirs) {
                int nr = current.r + d[0];
                int nc = current.c + d[1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && !inMST[nr][nc] && grid[nr][nc] != 1) {
                    // Mark neighbor as queued (state 2 = Visited/Blue)
                    gridCanvas.updateCell(nr, nc, 2);
                    pq.add(new Node(nr, nc, weights[nr][nc]));
                }
            }
        }
    }

    @Override
    public void run() {
        execute();
    }
}
