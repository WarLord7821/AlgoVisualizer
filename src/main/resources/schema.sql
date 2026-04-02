CREATE DATABASE IF NOT EXISTS algo_visualizer;
USE algo_visualizer;

CREATE TABLE IF NOT EXISTS ExecutionHistory (
                                                id INT PRIMARY KEY AUTO_INCREMENT,
                                                algorithm_name VARCHAR(100),
    input_size INT,
    time_taken_ms BIGINT
    );