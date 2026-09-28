import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main.java
 *
 * Entry point of the application.
 * It simply opens the Login window.
 */
public class Main {

    public static void main(String[] args) {

        // Make Swing look like the operating system's own windows.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            // If it fails we just keep the default look. Not a problem.
        }

        UITheme.install();

        // All Swing windows must be created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            // Check the database first so the user gets a clear message
            // instead of a confusing error later.
            if (DatabaseConnection.testConnection()) {
                new LoginFrame().setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
