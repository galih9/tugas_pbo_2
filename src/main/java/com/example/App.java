package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/test_java";
        String user = "postgres";
        String password = "postgres";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            System.out.println("Connecting to PostgreSQL database...");

            connection = DriverManager.getConnection(jdbcUrl, user, password);
            System.out.println("Connected to PostgreSQL database successfully!");

            statement = connection.createStatement();

            String sql = "SELECT id, nama FROM murid";
            resultSet = statement.executeQuery(sql);

            System.out.println("\nData from murid:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nama = resultSet.getString("nama");
                System.out.println("ID: " + id + ", Name: " + nama);
            }

        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());

        } finally {

            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
                System.out.println("Database resources closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());

            }
        }
    }
}
