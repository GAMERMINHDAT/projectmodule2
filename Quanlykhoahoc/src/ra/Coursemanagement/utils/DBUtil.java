package ra.Coursemanagement.utils;

import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres"; // Thay bằng username của bạn
    private static final String PASSWORD = "dat03122005"; // Thay bằng password của bạn
    private static final String SCHEMA = "coursemanagement";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        // Set schema
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET search_path TO " + SCHEMA);
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void handleSQLException(SQLException e, String message) {
        System.err.println(message);
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Message: " + e.getMessage());
        e.printStackTrace();
    }
}