import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Dashboard.java
 *
 * The main menu of the application.
 * Each button opens one of the other screens.
 */
public class Dashboard extends JFrame implements ActionListener {

    private final JButton btnStudents, btnPayment, btnHistory, btnReceipt, btnLogout, btnExit;
    private final String loggedInUser;   // remembered only so we can greet the user

    public Dashboard(String username) {
        this.loggedInUser = username;

        setTitle("MEA Engineering College - Fee Payment & Student Management System");
        setSize(1080, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout(24, 0));
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        UITheme.stylePanel(root);
        add(root);

        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(UITheme.SIDEBAR);
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 18, 18, 18));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        JLabel brandName = new JLabel("MEA ENGINEERING");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandName.setForeground(Color.WHITE);
        JLabel brandSub = new JLabel("COLLEGE");
        brandSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        brandSub.setForeground(new Color(148, 163, 184));
        brand.add(brandName);
        brand.add(brandSub);
        sidebar.add(brand, BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.add(Box.createVerticalStrut(40));
        nav.add(sidebarLabel("WORKSPACE"));
        nav.add(sidebarLabel("Dashboard"));
        nav.add(sidebarLabel("Student Management"));
        nav.add(sidebarLabel("Fee Payment"));
        nav.add(sidebarLabel("Payment History"));
        nav.add(sidebarLabel("Fee Receipts"));
        sidebar.add(nav, BorderLayout.CENTER);

        JPanel sidebarBottom = new JPanel(new GridLayout(2, 1, 0, 10));
        sidebarBottom.setOpaque(false);
        btnLogout = new JButton("Logout");
        UITheme.styleButton(btnLogout, new Color(51, 65, 85), new Color(71, 85, 105));
        btnExit = new JButton("Exit");
        UITheme.styleButton(btnExit, UITheme.DANGER, new Color(185, 28, 28));
        sidebarBottom.add(btnLogout);
        sidebarBottom.add(btnExit);
        sidebar.add(sidebarBottom, BorderLayout.SOUTH);
        root.add(sidebar, BorderLayout.WEST);

        JPanel content = new JPanel(new BorderLayout(0, 24));
        content.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(UITheme.TEXT);
        JLabel welcome = new JLabel("Welcome back, " + loggedInUser);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcome.setForeground(UITheme.MUTED);
        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(4));
        titleBlock.add(welcome);
        header.add(titleBlock, BorderLayout.WEST);
        JLabel date = new JLabel(java.time.LocalDate.now().toString(), SwingConstants.RIGHT);
        date.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        date.setForeground(UITheme.MUTED);
        header.add(date, BorderLayout.EAST);
        content.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);
        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 0));
        stats.setOpaque(false);
        stats.add(statCard("Student records", "Manage profiles", UITheme.PRIMARY));
        stats.add(statCard("Fee collection", "Process payments", UITheme.SECONDARY));
        stats.add(statCard("Pending fees", "Review balances", UITheme.WARNING));
        stats.add(statCard("Receipts", "Print and save", UITheme.ACCENT));
        body.add(stats, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(2, 2, 16, 16));
        actions.setOpaque(false);
        btnStudents = new JButton("Student Management");
        UITheme.styleButton(btnStudents, UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnPayment = new JButton("Fee Payment");
        UITheme.styleButton(btnPayment, UITheme.SECONDARY, new Color(21, 128, 58));
        btnHistory = new JButton("Payment History");
        UITheme.styleButton(btnHistory, UITheme.ACCENT, new Color(79, 70, 229));
        btnReceipt = new JButton("Fee Receipt");
        UITheme.styleButton(btnReceipt, UITheme.WARNING, new Color(217, 119, 6));
        actions.add(actionCard("Students", "Add, edit, and search student records.", btnStudents));
        actions.add(actionCard("Payments", "Record fees and calculate balances.", btnPayment));
        actions.add(actionCard("History", "Review and update payment records.", btnHistory));
        actions.add(actionCard("Receipts", "Open, print, or save a fee receipt.", btnReceipt));
        body.add(actions, BorderLayout.CENTER);
        content.add(body, BorderLayout.CENTER);
        root.add(content, BorderLayout.CENTER);

        // ---- Event handling ----
        btnStudents.addActionListener(event -> actionPerformed(event));
        btnPayment.addActionListener(event -> actionPerformed(event));
        btnHistory.addActionListener(event -> actionPerformed(event));
        btnReceipt.addActionListener(event -> actionPerformed(event));
        btnLogout.addActionListener(event -> actionPerformed(event));
        btnExit.addActionListener(event -> actionPerformed(event));
    }

    private JLabel sidebarLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(7, 4, 7, 4));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(text.equals("WORKSPACE")
                ? new Color(100, 116, 139) : new Color(203, 213, 225));
        return label;
    }

    private JPanel statCard(String title, String detail, Color accent) {
        JPanel card = new JPanel();
        UITheme.styleCard(card);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(UITheme.TEXT);
        JLabel detailLabel = new JLabel(detail);
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailLabel.setForeground(accent);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(detailLabel);
        return card;
    }

    private JPanel actionCard(String title, String detail, JButton button) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        UITheme.styleCard(card);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(UITheme.TEXT);
        JLabel detailLabel = new JLabel("<html>" + detail + "</html>");
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailLabel.setForeground(UITheme.MUTED);
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(titleLabel);
        text.add(Box.createVerticalStrut(6));
        text.add(detailLabel);
        card.add(text, BorderLayout.CENTER);
        card.add(button, BorderLayout.SOUTH);
        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnStudents) {
            new StudentManagement().setVisible(true);

        } else if (src == btnPayment) {
            new FeePayment().setVisible(true);

        } else if (src == btnHistory) {
            new PaymentHistory().setVisible(true);

        } else if (src == btnReceipt) {
            // Ask which receipt to show, then open the receipt window.
            String receiptNo = JOptionPane.showInputDialog(this,
                    "Enter the Receipt Number to view:", "Fee Receipt",
                    JOptionPane.QUESTION_MESSAGE);

            if (receiptNo != null && !receiptNo.trim().isEmpty()) {
                new FeeReceipt(receiptNo.trim()).setVisible(true);
            }

        } else if (src == btnLogout) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }

        } else if (src == btnExit) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Do you really want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }
}
