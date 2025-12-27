package com.gamehub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class DatabaseManager {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    static {
        if (URL == null || URL.isEmpty()) {
            throw new RuntimeException("FATAL: DB_URL missing. Check .env file!");
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found.");
            throw new SQLException("Driver not found.", e);
        }

        if (USER != null && !USER.isEmpty()) {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } else {
            return DriverManager.getConnection(URL);
        }
    }

    public static void initializeDatabase() {
        String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id SERIAL PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password_hash VARCHAR(100) NOT NULL," +
                "date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        String createHighscoresTableSQL = "CREATE TABLE IF NOT EXISTS highscores (" +
                "score_id SERIAL PRIMARY KEY," +
                "user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE," +
                "game_name VARCHAR(50) NOT NULL," +
                "score INTEGER NOT NULL," +
                "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE(user_id, game_name)" +
                ");";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(createUserTableSQL);
            stmt.execute(createHighscoresTableSQL);

            System.out.println("Database schema initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database Initialization Error (Check host status & credentials): " + e.getMessage());
        }
    }

    public static int signUp(String username, String password) {
        String checkSQL = "SELECT user_id FROM users WHERE username = ?";
        String insertSQL = "INSERT INTO users (username, password_hash) VALUES (?, ?) RETURNING user_id";

        try (Connection conn = getConnection()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Username already exists.");
                    return -1;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, hashPassword(password));
                ResultSet rs = insertStmt.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    System.out.println("User registered successfully with ID: " + userId);
                    return userId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Signup Error: " + e.getMessage());
        }
        return -2;
    }

    public static int login(String username, String password) {
        String SQL = "SELECT user_id, password_hash FROM users WHERE username = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String inputHash = hashPassword(password);

                if (storedHash.equals(inputHash)) {
                    int userId = rs.getInt("user_id");
                    System.out.println("Login successful! User ID: " + userId);
                    return userId;
                }
            }
            System.out.println("Invalid username or password.");
            return -1;

        } catch (SQLException e) {
            System.err.println("Login Error: " + e.getMessage());
            return -1;
        }
    }

    public static boolean saveScore(int userId, String gameName, int score) {
        String SQL = "INSERT INTO highscores (user_id, game_name, score) VALUES (?, ?, ?) " +
                "ON CONFLICT (user_id, game_name) " +
                "DO UPDATE SET score = EXCLUDED.score " +
                "WHERE EXCLUDED.score > highscores.score";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, gameName);
            pstmt.setInt(3, score);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Score saved: " + score + " for " + gameName);
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error saving score: " + e.getMessage());
            return false;
        }
    }

    public static int getUserHighScore(int userId, String gameName) {
        String SQL = "SELECT score FROM highscores WHERE user_id = ? AND game_name = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, gameName);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int highScore = rs.getInt("score");
                System.out.println("User high score for " + gameName + ": " + highScore);
                return highScore;
            }
            return 0;

        } catch (SQLException e) {
            System.err.println("Error fetching user high score: " + e.getMessage());
            return 0;
        }
    }

    public static String[][] getLeaderboard(String gameName, int limit) {
        String SQL = "SELECT u.username, h.score " +
                "FROM highscores h " +
                "JOIN users u ON h.user_id = u.user_id " +
                "WHERE h.game_name = ? " +
                "ORDER BY h.score DESC " +
                "LIMIT ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, gameName);
            pstmt.setInt(2, limit);

            ResultSet rs = pstmt.executeQuery();

            java.util.List<String[]> leaderboard = new java.util.ArrayList<>();
            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                leaderboard.add(new String[] { username, String.valueOf(score) });
            }

            String[][] result = new String[leaderboard.size()][2];
            for (int i = 0; i < leaderboard.size(); i++) {
                result[i] = leaderboard.get(i);
            }

            System.out.println("🏆 Leaderboard loaded for " + gameName + " (" + result.length + " entries)");
            return result;

        } catch (SQLException e) {
            System.err.println("Error fetching leaderboard: " + e.getMessage());
            return new String[0][0];
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}