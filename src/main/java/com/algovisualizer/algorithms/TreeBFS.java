package com.algovisualizer.algorithms;

import com.algovisualizer.TreeCanvas;
import com.algovisualizer.TreeCanvas.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

public class TreeBFS extends BaseAlgorithm {

    @Override
    public void execute() {
        TreeCanvas treeCanvas = (TreeCanvas) canvas;
        if (treeCanvas == null) return;
        TreeNode root = treeCanvas.getRoot();
        if (root == null) return;

        Queue<TreeNode> queue = new LinkedList<>();
        
        // Add root to the queue
        root.state = 1; // Discovered
        queue.add(root);
        treeCanvas.drawTree();
        sleep();

        while (!queue.isEmpty()) {
            // Pop from the queue
            TreeNode current = queue.poll();
            current.state = 2; // Processed
            treeCanvas.drawTree();
            sleep();

            // Process left child
            if (current.left != null) {
                current.left.state = 1; // Discovered
                queue.add(current.left);
                treeCanvas.drawTree();
                sleep();
            }

            // Process right child
            if (current.right != null) {
                current.right.state = 1; // Discovered
                queue.add(current.right);
                treeCanvas.drawTree();
                sleep();
            }
        }
    }

    @Override
    public void run() {
        execute();
    }
}
