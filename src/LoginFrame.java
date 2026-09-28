import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * LoginFrame.java
 *
 * First screen of the application.
 * The username and password are checked against the MySQL "users" table
 * using a PreparedStatement (safe against SQL injection).
 */
public class LoginFrame extends JFrame {

    // ---- Components ----
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JButton btnLogin, btnClear, btnExit;
    private final JLabel lblError;

    public LoginFrame() {
        setTitle("MEA Engineering College - Fee Payment & Student Management System");
        setSize(560, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        UITheme.stylePanel(panel);
        add(panel);

        JPanel card = new JPanel(new GridBagLayout());
        UITheme.styleCard(card);

        JLabel logo = new JLabel("MEAC", SwingConstants.CENTER);
        logo.setOpaque(true);
        logo.setBackground(UITheme.PRIMARY);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setPreferredSize(new java.awt.Dimension(72, 46));

        JLabel heading = new JLabel("MEA ENGINEERING COLLEGE", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(UITheme.TEXT);

        JLabel subtitle = new JLabel("Fee Payment & Student Management System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(UITheme.MUTED);

        txtUsername = new JTextField();
        UITheme.styleTextField(txtUsername);
        txtUsername.setPreferredSize(new java.awt.Dimension(320, 42));

        txtPassword = new JPasswordField();
        UITheme.styleTextField(txtPassword);
        txtPassword.setPreferredSize(new java.awt.Dimension(320, 42));

        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPassword.setForeground(UITheme.MUTED);
        showPassword.setBackground(UITheme.PANEL);
        char passwordEcho = txtPassword.getEchoChar();
        showPassword.addActionListener(e -> txtPassword.setEchoChar(
                showPassword.isSelected() ? (char) 0 : passwordEcho));

        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(UITheme.DANGER);

        btnLogin = new JButton("Login");
        styleLoginButton(btnLogin, new Color(21, 101, 192), new Color(13, 71, 161), Color.WHITE);

        btnClear = new JButton("Clear");
        styleLoginButton(btnClear, new Color(226, 232, 240), new Color(203, 213, 225), UITheme.TEXT);

        btnExit = new JButton("Exit");
        styleLoginButton(btnExit, new Color(211, 47, 47), new Color(183, 28, 28), Color.WHITE);

        addToCard(card, logo, 0, 0, 2, 1, 8);
        addToCard(card, heading, 0, 1, 2, 1, 5);
        addToCard(card, subtitle, 0, 2, 2, 1, 22);
        addToCard(card, fieldLabel("Username"), 0, 3, 2, 1, 5);
        addToCard(card, txtUsername, 0, 4, 2, 1, 12);
        addToCard(card, fieldLabel("Password"), 0, 5, 2, 1, 5);
        addToCard(card, txtPassword, 0, 6, 2, 1, 4);
        addToCard(card, showPassword, 0, 7, 2, 1, 4);
        addToCard(card, lblError, 0, 8, 2, 1, 10);
        addToCard(card, btnLogin, 0, 9, 2, 1, 8);
        addToCard(card, btnClear, 0, 10, 1, 1, 0);
        addToCard(card, btnExit, 1, 10, 1, 1, 0);

        GridBagConstraints cardConstraints = new GridBagConstraints();
        cardConstraints.gridx = 0;
        cardConstraints.gridy = 0;
        cardConstraints.weightx = 1;
        cardConstraints.weighty = 1;
        cardConstraints.fill = GridBagConstraints.NONE;
        panel.add(card, cardConstraints);

        btnLogin.addActionListener(e -> doLogin());
        btnClear.addActionListener(e -> clearForm());
        btnExit.addActionListener(e -> exitApplication());

        // Pressing Enter inside the password box also logs in.
        txtPassword.addActionListener(e -> doLogin());

        getRootPane().setDefaultButton(btnLogin);
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UITheme.TEXT);
        return label;
    }

    private void addToCard(JPanel card, java.awt.Component component, int gridx, int gridy,
                           int gridwidth, int gridheight, int bottomInset) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, bottomInset, 0);
        card.add(component, constraints);
    }

    private void styleLoginButton(JButton button, Color background, Color hoverBackground,
                                  Color foreground) {
        UITheme.styleButton(button, background, hoverBackground);
        button.setUI(new BasicButtonUI());
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setEnabled(true);
    }

    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Do you really want to exit?", "Exit",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /** Validates the fields and checks the user in the database. */
    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // ---- Validation ----
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the username.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the password.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        // ---- Database check ----
        String sql = "SELECT user_id FROM users WHERE username = ? AND password = ?";

        // try-with-resources automatically closes the connection and statement.
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Login successful -> open the Dashboard and close this window.
                    JOptionPane.showMessageDialog(this, "Login successful. Welcome, " + username + "!",
                            "Login", JOptionPane.INFORMATION_MESSAGE);
                    new Dashboard(username).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                }
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Login could not be completed.");
        }
    }

    /** Empties both text boxes. */
    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }
}
