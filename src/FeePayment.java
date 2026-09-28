import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * FeePayment.java
 *
 * Complete CRUD screen for fee payments.
 *
 * Balance = Total Fee - Paid Amount
 *
 * The student's name, course and semester are filled in automatically
 * from the "students" table once an admission number is typed.
 */
public class FeePayment extends JFrame implements ActionListener {

    // ---- Form fields ----
    private JTextField txtPaymentId, txtReceiptNo, txtAdmissionNo, txtStudentName;
    private JTextField txtCourse, txtSemester, txtTotalFee, txtPaidAmount, txtBalance, txtDate;
    private JComboBox<String> cmbFeeType, cmbMethod;

    // ---- Buttons ----
    private final JButton btnNew, btnSave, btnView, btnUpdate, btnDelete,
                    btnSearch, btnCalculate, btnClear, btnReceipt, btnBack;

    private JTextField txtSearch;

    // ---- Table ----
    private final JTable table;
    private final DefaultTableModel model;

    // Date format used everywhere in this screen.
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public FeePayment() {
        setTitle("Fee Payment");
        setSize(1200, 780);
        setMinimumSize(new Dimension(1050, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        UITheme.stylePanel(panel);
        add(panel);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel lblTitle = new JLabel("Fee Payment");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(UITheme.TEXT);
        JLabel subtitle = new JLabel("Process student fee payments and generate receipts.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(UITheme.MUTED);
        header.add(lblTitle);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        panel.add(header, BorderLayout.NORTH);

        JPanel studentCard = new JPanel(new BorderLayout(0, 12));
        UITheme.styleCard(studentCard);
        JPanel studentHeader = new JPanel();
        studentHeader.setOpaque(false);
        studentHeader.setLayout(new BoxLayout(studentHeader, BoxLayout.Y_AXIS));
        JLabel studentTitle = new JLabel("Student Search");
        studentTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        studentTitle.setForeground(UITheme.TEXT);
        JPanel studentSearch = new JPanel(new BorderLayout(10, 0));
        studentSearch.setOpaque(false);
        txtSearch = new JTextField();
        UITheme.styleSearchField(txtSearch);
        txtSearch.setToolTipText("Search payments by receipt, admission number, or name");
        studentSearch.add(txtSearch, BorderLayout.CENTER);
        btnSearch = new JButton("Search");
        UITheme.styleButton(btnSearch, UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        studentSearch.add(btnSearch, BorderLayout.EAST);
        studentHeader.add(studentTitle);
        studentHeader.add(Box.createVerticalStrut(10));
        studentHeader.add(studentSearch);
        studentCard.add(studentHeader, BorderLayout.NORTH);

        JPanel studentInfo = new JPanel(new GridLayout(4, 2, 14, 8));
        studentInfo.setOpaque(false);
        studentInfo.add(makeFormLabel("Admission Number"));
        studentInfo.add(makeFormLabel("Student Name"));
        txtAdmissionNo = new JTextField();
        UITheme.styleTextField(txtAdmissionNo);
        studentInfo.add(txtAdmissionNo);
        txtStudentName = new JTextField();
        UITheme.styleTextField(txtStudentName);
        studentInfo.add(txtStudentName);
        studentInfo.add(makeFormLabel("Course"));
        studentInfo.add(makeFormLabel("Semester"));
        txtCourse = new JTextField();
        UITheme.styleTextField(txtCourse);
        studentInfo.add(txtCourse);
        txtSemester = new JTextField();
        UITheme.styleTextField(txtSemester);
        studentInfo.add(txtSemester);
        studentCard.add(studentInfo, BorderLayout.CENTER);

        JPanel paymentCard = new JPanel(new BorderLayout(0, 12));
        UITheme.styleCard(paymentCard);
        JLabel paymentTitle = new JLabel("Payment Details");
        paymentTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        paymentTitle.setForeground(UITheme.TEXT);
        paymentCard.add(paymentTitle, BorderLayout.NORTH);
        JPanel paymentForm = new JPanel(new GridLayout(5, 4, 14, 8));
        paymentForm.setOpaque(false);
        paymentForm.add(makeFormLabel("Fee Type"));
        paymentForm.add(makeFormLabel("Total Fee"));
        paymentForm.add(makeFormLabel("Paid Amount"));
        paymentForm.add(makeFormLabel("Balance"));
        cmbFeeType = new JComboBox<>(new String[]{
                "Tuition Fee", "Exam Fee", "Hostel Fee", "Library Fee", "Other Fee"});
        UITheme.styleComboBox(cmbFeeType);
        paymentForm.add(cmbFeeType);
        txtTotalFee = new JTextField();
        UITheme.styleTextField(txtTotalFee);
        paymentForm.add(txtTotalFee);
        txtPaidAmount = new JTextField();
        UITheme.styleTextField(txtPaidAmount);
        paymentForm.add(txtPaidAmount);
        txtBalance = new JTextField();
        txtBalance.setEditable(false);
        UITheme.styleTextField(txtBalance);
        paymentForm.add(txtBalance);
        paymentForm.add(makeFormLabel("Payment Method"));
        paymentForm.add(makeFormLabel("Payment Date"));
        paymentForm.add(makeFormLabel("Receipt Number"));
        paymentForm.add(makeFormLabel("Payment ID"));
        cmbMethod = new JComboBox<>(new String[]{"Cash", "UPI", "Card", "Bank Transfer"});
        UITheme.styleComboBox(cmbMethod);
        paymentForm.add(cmbMethod);
        txtDate = new JTextField(dateFormat.format(new java.util.Date()));
        UITheme.styleTextField(txtDate);
        paymentForm.add(txtDate);
        txtReceiptNo = new JTextField();
        UITheme.styleTextField(txtReceiptNo);
        paymentForm.add(txtReceiptNo);
        txtPaymentId = new JTextField();
        txtPaymentId.setEditable(false);
        UITheme.styleTextField(txtPaymentId);
        paymentForm.add(txtPaymentId);
        paymentCard.add(paymentForm, BorderLayout.CENTER);

        txtAdmissionNo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                loadStudentDetails();
            }
        });

        JPanel topCards = new JPanel(new GridLayout(1, 2, 16, 0));
        topCards.setOpaque(false);
        topCards.add(studentCard);
        topCards.add(paymentCard);

        String[] columns = {"Payment ID", "Receipt No", "Admission No", "Student Name",
                            "Course", "Semester", "Fee Type", "Total Fee",
                            "Paid Amount", "Balance", "Method", "Date"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        JPanel historyCard = new JPanel(new BorderLayout(0, 10));
        UITheme.styleCard(historyCard);
        JLabel historyTitle = new JLabel("Payment History");
        historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        historyTitle.setForeground(UITheme.TEXT);
        historyCard.add(historyTitle, BorderLayout.NORTH);
        historyCard.add(scroll, BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowIntoForm();
            }
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        btnNew = new JButton("New Payment");
        UITheme.styleButton(btnNew, UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnSave = new JButton("Pay / Save");
        UITheme.styleButton(btnSave, UITheme.SECONDARY, new Color(21, 128, 58));
        btnView = new JButton("View");
        UITheme.styleButton(btnView, new Color(59, 130, 246), new Color(37, 99, 235));
        btnUpdate = new JButton("Update");
        UITheme.styleButton(btnUpdate, UITheme.WARNING, new Color(217, 119, 6));
        btnDelete = new JButton("Delete");
        UITheme.styleButton(btnDelete, UITheme.DANGER, new Color(185, 28, 28));
        btnCalculate = new JButton("Calculate");
        UITheme.styleButton(btnCalculate, UITheme.ACCENT, new Color(79, 70, 229));
        btnClear = new JButton("Clear");
        UITheme.styleButton(btnClear, new Color(100, 116, 139), new Color(71, 85, 105));
        btnReceipt = new JButton("Generate Receipt");
        UITheme.styleButton(btnReceipt, UITheme.WARNING, new Color(217, 119, 6));
        btnBack = new JButton("Back");
        UITheme.styleButton(btnBack, new Color(100, 116, 139), new Color(71, 85, 105));
        actions.add(btnNew);
        actions.add(btnSave);
        actions.add(btnView);
        actions.add(btnUpdate);
        actions.add(btnDelete);
        actions.add(btnCalculate);
        actions.add(btnClear);
        actions.add(btnReceipt);
        actions.add(btnBack);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);
        content.add(topCards, BorderLayout.NORTH);
        content.add(historyCard, BorderLayout.CENTER);
        JPanel south = new JPanel(new BorderLayout(0, 12));
        south.setOpaque(false);
        south.add(actions, BorderLayout.NORTH);
        content.add(south, BorderLayout.SOUTH);
        panel.add(content, BorderLayout.CENTER);

        btnNew.addActionListener(event -> actionPerformed(event));
        btnSave.addActionListener(event -> actionPerformed(event));
        btnView.addActionListener(event -> actionPerformed(event));
        btnUpdate.addActionListener(event -> actionPerformed(event));
        btnDelete.addActionListener(event -> actionPerformed(event));
        btnSearch.addActionListener(event -> performStudentSearch());
        btnCalculate.addActionListener(event -> actionPerformed(event));
        btnClear.addActionListener(event -> actionPerformed(event));
        btnReceipt.addActionListener(event -> actionPerformed(event));
        btnBack.addActionListener(event -> actionPerformed(event));
        txtSearch.addActionListener(event -> performStudentSearch());

        // Existing payment listeners are kept unchanged.
        
        /*
        panel.add(makeLabel("Payment ID :", labelX, y));
        txtPaymentId = new JTextField();
        txtPaymentId.setBounds(fieldX, y, 190, 30);
        txtPaymentId.setEditable(false);
        txtPaymentId.setBackground(new Color(239, 246, 255));
        UITheme.styleTextField(txtPaymentId);
        panel.add(txtPaymentId);

        y += gap;
        panel.add(makeLabel("Receipt No * :", labelX, y));
        txtReceiptNo = new JTextField();
        txtReceiptNo.setBounds(fieldX, y, 190, 30);
        UITheme.styleTextField(txtReceiptNo);
        panel.add(txtReceiptNo);
        txtReceiptNo = new JTextField();
        txtReceiptNo.setBounds(fieldX, y, 190, 30);
        UITheme.styleTextField(txtReceiptNo);
        panel.add(txtReceiptNo);

        y += gap;
        panel.add(makeLabel("Admission No * :", labelX, y));
        txtAdmissionNo = new JTextField();
        txtAdmissionNo.setBounds(fieldX, y, 190, 30);
        UITheme.styleTextField(txtAdmissionNo);
        panel.add(txtAdmissionNo);

        y += gap;
        panel.add(makeLabel("Student Name :", labelX, y));
        txtStudentName = new JTextField();
        txtStudentName.setBounds(fieldX, y, 190, 30);
        UITheme.styleTextField(txtStudentName);
        panel.add(txtStudentName);

        y += gap;
        panel.add(makeLabel("Course :", labelX, y));
        txtCourse = new JTextField();
        txtCourse.setBounds(fieldX, y, 190, 30);
        UITheme.styleTextField(txtCourse);
        panel.add(txtCourse);

        y += gap;
        panel.add(makeLabel("Semester :", labelX, y));
        txtSemester = new JTextField();
        txtSemester.setBounds(fieldX, y, 190, 30);
        UITheme.styleTextField(txtSemester);
        panel.add(txtSemester);

        y += gap;
        panel.add(makeLabel("Fee Type :", labelX, y));
        cmbFeeType = new JComboBox<>(new String[]{
                "Tuition Fee", "Exam Fee", "Hostel Fee", "Library Fee", "Other Fee"});
        cmbFeeType.setBounds(fieldX, y, 190, 30);
        UITheme.styleComboBox(cmbFeeType);
        panel.add(cmbFeeType);

        y += gap;
        panel.add(makeLabel("Total Fee * :", labelX, y));
        txtTotalFee = new JTextField();
        txtTotalFee.setBounds(fieldX, y, 190, 30);
        UITheme.styleTextField(txtTotalFee);
        panel.add(txtTotalFee);

        y += gap;
        panel.add(makeLabel("Paid Amount * :", labelX, y));
        txtPaidAmount = new JTextField();
        txtPaidAmount.setBounds(fieldX, y, 190, 30);
        UITheme.styleTextField(txtPaidAmount);
        panel.add(txtPaidAmount);

        y += gap;
        panel.add(makeLabel("Balance :", labelX, y));
        txtBalance = new JTextField();
        txtBalance.setBounds(fieldX, y, 190, 30);
        txtBalance.setEditable(false);
        txtBalance.setBackground(new Color(239, 246, 255));
        UITheme.styleTextField(txtBalance);
        panel.add(txtBalance);

        y += gap;
        panel.add(makeLabel("Payment Method :", labelX, y));
        cmbMethod = new JComboBox<>(new String[]{"Cash", "UPI", "Card", "Bank Transfer"});
        cmbMethod.setBounds(fieldX, y, 190, 30);
        UITheme.styleComboBox(cmbMethod);
        panel.add(cmbMethod);

        y += gap;
        panel.add(makeLabel("Payment Date :", labelX, y));
        txtDate = new JTextField(dateFormat.format(new java.util.Date()));
        txtDate.setBounds(fieldX, y, 190, 30);
        UITheme.styleTextField(txtDate);
        panel.add(txtDate);

        JLabel lblHint = new JLabel("(date format: yyyy-MM-dd)");
        lblHint.setFont(new Font("Arial", Font.ITALIC, 11));
        lblHint.setBounds(fieldX, y + 26, 190, 18);
        panel.add(lblHint);

        // When the user leaves the admission-number box, look the student up.
        txtAdmissionNo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                loadStudentDetails();
            }
        });

        // ================= SEARCH =================
        JLabel lblSearch = new JLabel("Search by Receipt No / Admission No / Name :");
        lblSearch.setBounds(380, 55, 290, 26);
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(670, 55, 220, 30);
        UITheme.styleSearchField(txtSearch);
        panel.add(txtSearch);

        btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(900, 55, 130, 30);
        UITheme.styleButton(btnSearch, new Color(59, 130, 246), new Color(37, 99, 235));
        panel.add(btnSearch);

        // ================= TABLE =================
        String[] columns = {"Payment ID", "Receipt No", "Admission No", "Student Name",
                            "Course", "Semester", "Fee Type", "Total Fee",
                            "Paid Amount", "Balance", "Method", "Date"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);   // allows horizontal scrolling

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(380, 95, 650, 440);
        panel.add(scroll);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowIntoForm();
            }
        });

        // ================= BUTTONS =================
        int by = 560, bw = 150, bh = 34;

        btnNew = new JButton("NEW");
        btnNew.setBounds(25, by, bw, bh);
        UITheme.styleButton(btnNew, new Color(14, 165, 233), new Color(2, 132, 199));
        panel.add(btnNew);

        btnSave = new JButton("PAY / SAVE");
        btnSave.setBounds(185, by, bw, bh);
        UITheme.styleButton(btnSave, new Color(16, 185, 129), new Color(5, 150, 105));
        panel.add(btnSave);

        btnView = new JButton("VIEW");
        btnView.setBounds(345, by, bw, bh);
        UITheme.styleButton(btnView, new Color(59, 130, 246), new Color(37, 99, 235));
        panel.add(btnView);

        btnUpdate = new JButton("UPDATE");
        btnUpdate.setBounds(505, by, bw, bh);
        UITheme.styleButton(btnUpdate, new Color(245, 158, 11), new Color(217, 119, 6));
        panel.add(btnUpdate);

        btnDelete = new JButton("DELETE");
        btnDelete.setBounds(665, by, bw, bh);
        UITheme.styleButton(btnDelete, new Color(239, 68, 68), new Color(220, 38, 38));
        panel.add(btnDelete);

        by += 44;
        btnCalculate = new JButton("CALCULATE");
        btnCalculate.setBounds(25, by, bw, bh);
        UITheme.styleButton(btnCalculate, new Color(168, 85, 247), new Color(147, 51, 234));
        panel.add(btnCalculate);

        btnClear = new JButton("CLEAR");
        btnClear.setBounds(185, by, bw, bh);
        UITheme.styleButton(btnClear, new Color(100, 116, 139), new Color(71, 85, 105));
        panel.add(btnClear);

        btnReceipt = new JButton("GENERATE RECEIPT");
        btnReceipt.setBounds(345, by, 310, bh);
        UITheme.styleButton(btnReceipt, new Color(245, 158, 11), new Color(217, 119, 6));
        panel.add(btnReceipt);

        btnBack = new JButton("BACK");
        btnBack.setBounds(665, by, bw, bh);
        UITheme.styleButton(btnBack, new Color(168, 85, 247), new Color(147, 51, 234));
        panel.add(btnBack);

        // ---- Event handling ----
        btnNew.addActionListener(event -> actionPerformed(event));
        btnSave.addActionListener(event -> actionPerformed(event));
        btnView.addActionListener(event -> actionPerformed(event));
        btnUpdate.addActionListener(event -> actionPerformed(event));
        btnDelete.addActionListener(event -> actionPerformed(event));
        btnSearch.addActionListener(event -> performStudentSearch());
        btnCalculate.addActionListener(event -> actionPerformed(event));
        btnClear.addActionListener(event -> actionPerformed(event));
        btnReceipt.addActionListener(event -> actionPerformed(event));
        btnBack.addActionListener(event -> actionPerformed(event));
        txtSearch.addActionListener(event -> performStudentSearch());

        JPanel formCard = new JPanel();
        formCard.setBounds(15, 48, 340, 495);
        UITheme.styleCard(formCard);
        JPanel tableCard = new JPanel();
        tableCard.setBounds(365, 48, 680, 500);
        UITheme.styleCard(tableCard);
        panel.add(formCard, 0);
        panel.add(tableCard, 0);

        */
        // Load all payments when the window opens (READ).
        viewAllPayments();
    }

    private JLabel makeFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UITheme.TEXT);
        return label;
    }

    // =====================================================
    // EVENT HANDLING
    // =====================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnNew) {
            newPayment();
        } else if (src == btnSave) {
            savePayment();
        } else if (src == btnView) {
            viewSelectedPayment();
        } else if (src == btnUpdate) {
            updatePayment();
        } else if (src == btnDelete) {
            deletePayment();
        } else if (src == btnSearch || src == txtSearch) {
            performStudentSearch();
        } else if (src == btnCalculate) {
            calculateBalance(true);
        } else if (src == btnClear) {
            clearForm();
        } else if (src == btnReceipt) {
            generateReceipt();
        } else if (src == btnBack) {
            dispose();
        }
    }

    // =====================================================
    // NEW - clears the form and suggests a fresh receipt number
    // =====================================================
    private void newPayment() {
        clearForm();
        txtReceiptNo.setText(generateReceiptNumber());
        txtDate.setText(dateFormat.format(new java.util.Date()));
        txtAdmissionNo.requestFocus();
    }

    /** Builds the next receipt number, for example RCPT1004. */
    private String generateReceiptNumber() {
        String sql = "SELECT MAX(payment_id) AS last_id FROM payments";
        int next = 1001;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                next = 1001 + rs.getInt("last_id");
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Receipt number could not be generated.");
        }
        return "RCPT" + next;
    }

    // =====================================================
    // CALCULATE : Balance = Total Fee - Paid Amount
    // =====================================================
    private boolean calculateBalance(boolean showMessage) {
        try {
            double totalFee = Double.parseDouble(txtTotalFee.getText().trim());
            double paid = Double.parseDouble(txtPaidAmount.getText().trim());

            if (totalFee < 0 || paid < 0) {
                JOptionPane.showMessageDialog(this, "Amounts cannot be negative.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (paid > totalFee) {
                JOptionPane.showMessageDialog(this,
                        "Paid amount cannot be more than the total fee.",
                        "Validation", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            double balance = totalFee - paid;
            txtBalance.setText(String.format("%.2f", balance));

            if (showMessage) {
                JOptionPane.showMessageDialog(this,
                        "Balance calculated : " + String.format("%.2f", balance),
                        "Calculate", JOptionPane.INFORMATION_MESSAGE);
            }
            return true;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers in Total Fee and Paid Amount.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    // =====================================================
    // CREATE - save a new payment
    // =====================================================
    private void savePayment() {
        if (txtReceiptNo.getText().trim().isEmpty()) {
            txtReceiptNo.setText(generateReceiptNumber());
        }
        if (!validateForm()) {
            return;
        }

        String sql = "INSERT INTO payments " +
                "(receipt_no, admission_no, student_name, course, semester, fee_type, " +
                " total_fee, paid_amount, balance, payment_method, payment_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

           try (Connection con = DatabaseConnection.getConnection();
               PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, txtReceiptNo.getText().trim());
            ps.setString(2, txtAdmissionNo.getText().trim());
            ps.setString(3, txtStudentName.getText().trim());
            ps.setString(4, txtCourse.getText().trim());
            ps.setString(5, txtSemester.getText().trim());
            ps.setString(6, (String) cmbFeeType.getSelectedItem());
            ps.setDouble(7, Double.parseDouble(txtTotalFee.getText().trim()));
            ps.setDouble(8, Double.parseDouble(txtPaidAmount.getText().trim()));
            ps.setDouble(9, Double.parseDouble(txtBalance.getText().trim()));
            ps.setString(10, (String) cmbMethod.getSelectedItem());
            ps.setDate(11, Date.valueOf(txtDate.getText().trim()));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        txtPaymentId.setText(String.valueOf(generatedKeys.getInt(1)));
                    }
                }
                viewAllPayments();      // refresh the JTable

                int choice = JOptionPane.showConfirmDialog(this,
                        "Payment saved successfully.\n\nDo you want to generate the receipt now?",
                        "Success", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (choice == JOptionPane.YES_OPTION) {
                    new FeeReceipt(txtReceiptNo.getText().trim()).setVisible(true);
                }
            }

        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this,
                        "This receipt number already exists. Click NEW for a fresh one.",
                        "Duplicate", JOptionPane.ERROR_MESSAGE);
            } else {
                DatabaseConnection.showError(ex, "Payment could not be saved.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Payment date must look like 2026-09-21.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
        }
    }

    // =====================================================
    // READ
    // =====================================================
    private void viewAllPayments() {
        String sql = "SELECT * FROM payments ORDER BY payment_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            fillTable(rs);

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Payment list could not be loaded.");
        }
    }

    private void performStudentSearch() {
        String admissionNo = txtSearch.getText().trim();
        if (admissionNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an admission number.",
                    "Student Search", JOptionPane.WARNING_MESSAGE);
            txtSearch.requestFocus();
            return;
        }

        String sql = "SELECT admission_no, student_name, course, semester "
                + "FROM students WHERE admission_no LIKE ? ORDER BY admission_no";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + admissionNo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtAdmissionNo.setText(rs.getString("admission_no"));
                    txtStudentName.setText(rs.getString("student_name"));
                    txtCourse.setText(rs.getString("course"));
                    txtSemester.setText(rs.getString("semester"));
                } else {
                    txtStudentName.setText("");
                    txtCourse.setText("");
                    txtSemester.setText("");
                    JOptionPane.showMessageDialog(this, "Student not found.",
                            "Student Search", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Student search could not be completed.");
        }
    }

    private void viewSelectedPayment() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Please select a payment first.",
                    "View Payment", JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadSelectedRowIntoForm();
    }

    // =====================================================
    // UPDATE
    // =====================================================
    private void updatePayment() {
        String id = txtPaymentId.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a payment from the table first.",
                    "Update", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm()) {
            return;
        }

        String sql = "UPDATE payments SET receipt_no = ?, admission_no = ?, student_name = ?, " +
                "course = ?, semester = ?, fee_type = ?, total_fee = ?, paid_amount = ?, " +
                "balance = ?, payment_method = ?, payment_date = ? WHERE payment_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtReceiptNo.getText().trim());
            ps.setString(2, txtAdmissionNo.getText().trim());
            ps.setString(3, txtStudentName.getText().trim());
            ps.setString(4, txtCourse.getText().trim());
            ps.setString(5, txtSemester.getText().trim());
            ps.setString(6, (String) cmbFeeType.getSelectedItem());
            ps.setDouble(7, Double.parseDouble(txtTotalFee.getText().trim()));
            ps.setDouble(8, Double.parseDouble(txtPaidAmount.getText().trim()));
            ps.setDouble(9, Double.parseDouble(txtBalance.getText().trim()));
            ps.setString(10, (String) cmbMethod.getSelectedItem());
            ps.setDate(11, Date.valueOf(txtDate.getText().trim()));
            ps.setInt(12, Integer.parseInt(id));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Payment updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                viewAllPayments();
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Payment could not be updated.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Payment date must look like 2026-09-21.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
        }
    }

    // =====================================================
    // DELETE
    // =====================================================
    private void deletePayment() {
        String id = txtPaymentId.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a payment from the table first.",
                    "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this payment record?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM payments WHERE payment_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));
            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Payment deleted successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                viewAllPayments();
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Payment could not be deleted.");
        }
    }

    // =====================================================
    // GENERATE RECEIPT
    // =====================================================
    private void generateReceipt() {
        String receiptNo = txtReceiptNo.getText().trim();

        if (receiptNo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Select a payment (or save one) before generating a receipt.",
                    "Receipt", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new FeeReceipt(receiptNo).setVisible(true);
    }

    // =====================================================
    // Helper methods
    // =====================================================

    /** Looks up the student by admission number and fills name/course/semester. */
    private void loadStudentDetails() {
        String admissionNo = txtAdmissionNo.getText().trim();
        if (admissionNo.isEmpty()) {
            return;
        }

        String sql = "SELECT student_name, course, semester FROM students WHERE admission_no = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admissionNo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtStudentName.setText(rs.getString("student_name"));
                    txtCourse.setText(rs.getString("course"));
                    txtSemester.setText(rs.getString("semester"));
                }
                // If the student is not found we simply leave the boxes as they are,
                // so the user can still type the details by hand.
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Student details could not be loaded.");
        }
    }

    /** Copies the ResultSet rows into the JTable. */
    private void fillTable(ResultSet rs) throws SQLException {
        model.setRowCount(0);

        while (rs.next()) {
            Object[] row = {
                rs.getInt("payment_id"),
                rs.getString("receipt_no"),
                rs.getString("admission_no"),
                rs.getString("student_name"),
                rs.getString("course"),
                rs.getString("semester"),
                rs.getString("fee_type"),
                rs.getBigDecimal("total_fee"),
                rs.getBigDecimal("paid_amount"),
                rs.getBigDecimal("balance"),
                rs.getString("payment_method"),
                rs.getDate("payment_date")
            };
            model.addRow(row);
        }
    }

    /** Loads the selected table row into the form so it can be edited. */
    private void loadSelectedRowIntoForm() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        txtPaymentId.setText(safe(model.getValueAt(modelRow, 0)));
        txtReceiptNo.setText(safe(model.getValueAt(modelRow, 1)));
        txtAdmissionNo.setText(safe(model.getValueAt(modelRow, 2)));
        txtStudentName.setText(safe(model.getValueAt(modelRow, 3)));
        txtCourse.setText(safe(model.getValueAt(modelRow, 4)));
        txtSemester.setText(safe(model.getValueAt(modelRow, 5)));
        cmbFeeType.setSelectedItem(safe(model.getValueAt(modelRow, 6)));
        txtTotalFee.setText(safe(model.getValueAt(modelRow, 7)));
        txtPaidAmount.setText(safe(model.getValueAt(modelRow, 8)));
        txtBalance.setText(safe(model.getValueAt(modelRow, 9)));
        cmbMethod.setSelectedItem(safe(model.getValueAt(modelRow, 10)));
        txtDate.setText(safe(model.getValueAt(modelRow, 11)));
    }

    private String safe(Object value) {
        return (value == null) ? "" : value.toString();
    }

    /** Checks every required field before saving or updating. */
    private boolean validateForm() {
        if (txtReceiptNo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Receipt number is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            txtReceiptNo.requestFocus();
            return false;
        }
        if (txtAdmissionNo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Admission number is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            txtAdmissionNo.requestFocus();
            return false;
        }
        if (!studentExists(txtAdmissionNo.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Student not found.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            txtAdmissionNo.requestFocus();
            return false;
        }
        if (txtStudentName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student name is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            txtStudentName.requestFocus();
            return false;
        }
        if (txtTotalFee.getText().trim().isEmpty() || txtPaidAmount.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Total fee and paid amount are required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Payment date is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Always recalculate so the stored balance is correct.
        return calculateBalance(false);
    }

    private boolean studentExists(String admissionNo) {
        String sql = "SELECT 1 FROM students WHERE admission_no = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, admissionNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Student could not be verified.");
            return false;
        }
    }

    private void clearForm() {
        txtPaymentId.setText("");
        txtReceiptNo.setText("");
        txtSearch.setText("");
        txtAdmissionNo.setText("");
        txtStudentName.setText("");
        txtCourse.setText("");
        txtSemester.setText("");
        cmbFeeType.setSelectedIndex(0);
        txtTotalFee.setText("");
        txtPaidAmount.setText("");
        txtBalance.setText("");
        cmbMethod.setSelectedIndex(0);
        txtDate.setText(dateFormat.format(new java.util.Date()));
        table.clearSelection();
    }
}
