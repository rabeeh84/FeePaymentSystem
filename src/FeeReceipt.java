import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * FeeReceipt.java
 *
 * Shows the printable fee receipt for one payment.
 * The payment is read from the database using its receipt number.
 *
 * PRINT - uses Java's built-in printing (JTextArea.print()).
 * SAVE  - writes the receipt to a .txt file chosen by the user.
 * BACK  - closes this window.
 */
public class FeeReceipt extends JFrame {

    private final JTextArea area;           // holds the receipt text
    private final JButton btnPrint, btnSave, btnBack;
    private final String receiptNo;

    public FeeReceipt(String receiptNo) {
        this.receiptNo = receiptNo;

        setTitle("Fee Receipt - " + receiptNo);
        setSize(620, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // A monospaced font keeps the dashed lines and columns aligned.
        area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 15));
        area.setEditable(false);
        area.setBackground(new Color(255, 255, 255));
        area.setForeground(new Color(31, 41, 55));
        area.setMargin(new java.awt.Insets(15, 15, 15, 15));
        UITheme.styleTextArea(area);

        JPanel receiptCard = new JPanel(new BorderLayout());
        UITheme.styleCard(receiptCard);
        receiptCard.add(new JScrollPane(area), BorderLayout.CENTER);
        add(receiptCard, BorderLayout.CENTER);

        // ---- Buttons at the bottom ----
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.Y_AXIS));
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 28, 14, 28));
        headerPanel.setBackground(UITheme.BG);
        JLabel title = new JLabel("Fee Payment Receipt");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT);
        JLabel subtitle = new JLabel("MEA ENGINEERING COLLEGE  |  " + receiptNo);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(UITheme.MUTED);
        headerPanel.add(title);
        headerPanel.add(javax.swing.Box.createVerticalStrut(4));
        headerPanel.add(subtitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        UITheme.stylePanel(buttonPanel);
        buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 18, 4, 18));

        btnPrint = new JButton("PRINT");
        btnSave = new JButton("SAVE");
        btnBack = new JButton("BACK");
        UITheme.styleButton(btnPrint, new Color(59, 130, 246), new Color(37, 99, 235));
        UITheme.styleButton(btnSave, new Color(16, 185, 129), new Color(5, 150, 105));
        UITheme.styleButton(btnBack, new Color(168, 85, 247), new Color(147, 51, 234));

        buttonPanel.add(btnPrint);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);

        btnPrint.addActionListener(e -> printReceipt());
        btnSave.addActionListener(e -> saveReceipt());
        btnBack.addActionListener(e -> dispose());

        // Fetch the payment and build the receipt text.
        loadReceipt();
    }

    // =====================================================
    // Read the payment from MySQL and format the receipt
    // =====================================================
    private void loadReceipt() {
        String sql = "SELECT * FROM payments WHERE receipt_no = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, receiptNo);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this,
                            "No payment found with receipt number: " + receiptNo,
                            "Not Found", JOptionPane.WARNING_MESSAGE);
                    area.setText("Receipt not found.");
                    return;
                }

                // StringBuilder joins all the lines of the receipt together.
                StringBuilder sb = new StringBuilder();
                String line = "----------------------------------------";

                sb.append(line).append("\n");
                sb.append("        MEA ENGINEERING COLLEGE\n");
                sb.append("\n");
                sb.append("          FEE PAYMENT RECEIPT\n");
                sb.append(line).append("\n\n");

                sb.append(pad("Receipt No")).append(": ").append(rs.getString("receipt_no")).append("\n");
                sb.append(pad("Admission No")).append(": ").append(rs.getString("admission_no")).append("\n");
                sb.append(pad("Student Name")).append(": ").append(text(rs.getString("student_name"))).append("\n");
                sb.append(pad("Course")).append(": ").append(text(rs.getString("course"))).append("\n");
                sb.append(pad("Semester")).append(": ").append(text(rs.getString("semester"))).append("\n");
                sb.append(pad("Fee Type")).append(": ").append(text(rs.getString("fee_type"))).append("\n\n");

                sb.append(pad("Total Fee")).append(": ").append(money(rs.getDouble("total_fee"))).append("\n");
                sb.append(pad("Paid Amount")).append(": ").append(money(rs.getDouble("paid_amount"))).append("\n");
                sb.append(pad("Balance")).append(": ").append(money(rs.getDouble("balance"))).append("\n\n");

                sb.append(pad("Payment Method")).append(": ").append(text(rs.getString("payment_method"))).append("\n");
                sb.append(pad("Payment Date")).append(": ").append(text(String.valueOf(rs.getDate("payment_date")))).append("\n\n");

                sb.append(line).append("\n");
                sb.append("          PAYMENT SUCCESSFUL\n");
                sb.append(line).append("\n");

                area.setText(sb.toString());
                area.setCaretPosition(0);   // scroll back to the top
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Receipt could not be loaded.");
        }
    }

    /** Pads a label to 15 characters so all the colons line up. */
    private String pad(String label) {
        StringBuilder sb = new StringBuilder(label);
        while (sb.length() < 15) {
            sb.append(' ');
        }
        return sb.toString();
    }

    /** Formats an amount with two decimal places and a rupee prefix. */
    private String money(double amount) {
        return "Rs. " + String.format("%.2f", amount);
    }

    /** Replaces a null database value with a dash. */
    private String text(String value) {
        return (value == null || value.equals("null")) ? "-" : value;
    }

    /**
     * PRINT
     * JTextArea.print() opens the standard print dialog of the operating
     * system and sends the text to the chosen printer (or to a PDF printer).
     */
    private void printReceipt() {
        try {
            boolean printed = area.print();

            if (printed) {
                JOptionPane.showMessageDialog(this, "Receipt sent to the printer.",
                        "Print", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Printing was cancelled.",
                        "Print", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (java.awt.print.PrinterException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not print the receipt.\n" + ex.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * SAVE
     * Writes the receipt text into a .txt file chosen by the user.
     */
    private void saveReceipt() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Receipt");
        chooser.setSelectedFile(new File(receiptNo + ".txt"));

        int result = chooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;     // the user pressed Cancel
        }

        File file = chooser.getSelectedFile();

        // try-with-resources closes the writer automatically.
        try (FileWriter writer = new FileWriter(file)) {

            writer.write(area.getText());
            JOptionPane.showMessageDialog(this,
                    "Receipt saved to:\n" + file.getAbsolutePath(),
                    "Saved", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save the file.\n" + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
