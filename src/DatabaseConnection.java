import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/**
 * DatabaseConnection.java
 *
 * This is the only class that knows the database username, password and URL.
 * Every other screen calls DatabaseConnection.getConnection() to talk to MySQL.
 *
 * IMPORTANT: change USER and PASSWORD below to match your own MySQL setup.
 */
public class DatabaseConnection {

    // ---- Database settings (change these if needed) ----
    private static final String URL =
            "jdbc:mysql://localhost:3306/fee_payment_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "username";   // <-- your MySQL username
    private static final String PASSWORD = "your sql password";   // <-- your MySQL password

    /**
     * Opens and returns a new connection to the MySQL database.
     * The caller must close the connection when finished
     * (try-with-resources does this automatically).
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver.
            // (Not strictly required in modern Java, but kept for clarity.)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC driver not found. Add mysql-connector-j.jar to the classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Small helper used at program start-up to check that the database
     * is reachable. Shows a message box if something is wrong.
     */
    public static boolean testConnection() {
        try (Connection con = getConnection()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Cannot connect to the database.\n\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Reusable helper: shows a friendly error box for any SQL problem.
     * Every screen uses this so error handling looks the same everywhere.
     */
    public static void showError(SQLException e, String whatFailed) {
        JOptionPane.showMessageDialog(null,
                whatFailed + "\n\nSQL Error: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Reusable helper: safely closes a ResultSet, Statement and Connection.
     * (Used only where try-with-resources is not convenient.)
     */
    public static void close(ResultSet rs, Statement st, Connection con) {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) { }
        try { if (st != null) st.close(); } catch (SQLException ignored) { }
        try { if (con != null) con.close(); } catch (SQLException ignored) { }
    }
}
