package com.algovisualizer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/algo_visualizer";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void createTablesIfNotExist() {
        String query = "CREATE TABLE IF NOT EXISTS ExecutionHistory (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "algorithm_name VARCHAR(100)," +
                "input_size INT," +
                "time_taken_ms BIGINT" +
                ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logExecution(String algoName, int size, long timeMs) {
        String query = "INSERT INTO ExecutionHistory (algorithm_name, input_size, time_taken_ms) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, algoName);
            ps.setInt(2, size);
            ps.setLong(3, timeMs);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}