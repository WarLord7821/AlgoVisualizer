package com.algovisualizer;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.LinkedList;
import java.util.Queue;

public class TreeCanvas extends Pane {

    public static class TreeNode {
        public double x, y;
        public int val;
        public TreeNode left, right;
        public int state; // 0=Unvisited, 1=Discovered, 2=Processed

        public TreeNode(int val) {
            this.val = val;
            this.state = 0;
        }
    }

    private TreeNode root;
    private final double canvasWidth = 800;
    private final double canvasHeight = 500;

    public TreeCanvas() {
        this.setPrefSize(canvasWidth, canvasHeight);
    }

    /**
     * Builds a binary tree from a comma-separated input string in
     * standard LeetCode level-order format.
     * Example: "1,2,3,null,5,6,null"
     *
     * "null" or empty tokens are treated as absent nodes.
     */
    public void buildTreeFromInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            root = null;
            drawTree();
            return;
        }

        String[] tokens = input.split(",");

        // Parse root
        String rootToken = tokens[0].trim();
        if (rootToken.isEmpty() || rootToken.equalsIgnoreCase("null")) {
            root = null;
            drawTree();
            return;
        }
        root = new TreeNode(Integer.parseInt(rootToken));

        // Level-order construction using a queue
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int i = 1;
        while (!queue.isEmpty() && i < tokens.length) {
            TreeNode current = queue.poll();

            // Left child
            if (i < tokens.length) {
                String leftToken = tokens[i].trim();
                if (!leftToken.isEmpty() && !leftToken.equalsIgnoreCase("null")) {
                    current.left = new TreeNode(Integer.parseInt(leftToken));
                    queue.offer(current.left);
                }
                i++;
            }

            // Right child
            if (i < tokens.length) {
                String rightToken = tokens[i].trim();
                if (!rightToken.isEmpty() && !rightToken.equalsIgnoreCase("null")) {
                    current.right = new TreeNode(Integer.parseInt(rightToken));
                    queue.offer(current.right);
                }
                i++;
            }
        }

        // Assign (x, y) coordinates for rendering
        double horizontalOffset = canvasWidth / 4;
        calculateCoordinates(root, canvasWidth / 2, 50, horizontalOffset);

        drawTree();
    }

    /**
     * Recursively assigns X and Y coordinates to each node so the tree
     * renders as a balanced triangle. The horizontal offset halves at
     * each depth level.
     */
    private void calculateCoordinates(TreeNode node, double x, double y, double horizontalOffset) {
        if (node == null) return;

        node.x = x;
        node.y = y;

        double childY = y + 100; // vertical gap between levels

        calculateCoordinates(node.left, x - horizontalOffset, childY, horizontalOffset / 2);
        calculateCoordinates(node.right, x + horizontalOffset, childY, horizontalOffset / 2);
    }

    public void drawTree() {
        Platform.runLater(() -> {
            this.getChildren().clear();
            drawLines(root);
            drawNodes(root);
        });
    }

    private void drawLines(TreeNode node) {
        if (node == null) return;
        
        if (node.left != null) {
            Line line = new Line(node.x, node.y, node.left.x, node.left.y);
            line.setStrokeWidth(2);
            this.getChildren().add(line);
            drawLines(node.left);
        }
        
        if (node.right != null) {
            Line line = new Line(node.x, node.y, node.right.x, node.right.y);
            line.setStrokeWidth(2);
            this.getChildren().add(line);
            drawLines(node.right);
        }
    }

    private void drawNodes(TreeNode node) {
        if (node == null) return;
        
        Circle circle = new Circle(node.x, node.y, 20);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        
        switch (node.state) {
            case 0:
                circle.setFill(Color.WHITE);
                break;
            case 1:
                circle.setFill(Color.YELLOW);
                break;
            case 2:
                circle.setFill(Color.GREEN);
                break;
            default:
                circle.setFill(Color.WHITE);
                break;
        }
        
        Text text = new Text(String.valueOf(node.val));
        text.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        // Center text in circle
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();
        text.setX(node.x - textWidth / 2);
        text.setY(node.y + textHeight / 4);
        
        this.getChildren().addAll(circle, text);
        
        drawNodes(node.left);
        drawNodes(node.right);
    }

    public TreeNode getRoot() {
        return root;
    }
}
