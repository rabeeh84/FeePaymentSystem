import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
 * PaymentHistory.java
 *
 * Shows every payment record in a JTable.
 * The user can select a row, edit it in the small form below,
 * update it, delete it, search it, or open its receipt.
 */
public class PaymentHistory extends JFrame implements ActionListener {

    // ---- Edit form ----
    private JTextField txtPaymentId, txtReceiptNo, txtAdmissionNo, txtStudentName;
    private JTextField txtCourse, txtSemester, txtTotalFee, txtPaidAmount, txtBalance, txtDate;
    private JComboBox<String> cmbFeeType, cmbMethod;

    // ---- Buttons ----
    private JButton btnRefresh, btnUpdate, btnDelete, btnClear, btnSearch, btnReceipt, btnBack;
    private JTextField txtSearch;
    private JComboBox<String> cmbFeeTypeFilter, cmbMethodFilter;

    // ---- Table ----
    private JTable table;
    private DefaultTableModel model;

    public PaymentHistory() {
        buildModernLayout();
        /*
        setTitle("Payment History");
        setSize(1120, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        UITheme.stylePanel(panel);
        add(panel);

        JLabel lblTitle = new JLabel("Payment History", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(UITheme.TEXT);
        lblTitle.setBounds(0, 10, 1120, 28);
        panel.add(lblTitle);

        // ================= SEARCH =================
        JLabel lblSearch = new JLabel("Search (Receipt No / Admission No / Name / Fee Type) :");
        lblSearch.setBounds(25, 50, 330, 26);
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(360, 50, 280, 30);
        UITheme.styleSearchField(txtSearch);
        panel.add(txtSearch);

        btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(655, 50, 120, 30);
        UITheme.styleButton(btnSearch, new Color(59, 130, 246), new Color(37, 99, 235));
        panel.add(btnSearch);

        btnRefresh = new JButton("SHOW ALL");
        btnRefresh.setBounds(785, 50, 120, 30);
        UITheme.styleButton(btnRefresh, new Color(16, 185, 129), new Color(5, 150, 105));
        panel.add(btnRefresh);

        // ================= TABLE =================
        String[] columns = {"Payment ID", "Receipt No", "Admission No", "Student Name",
                            "Course", "Semester", "Fee Type", "Total Fee",
                            "Paid Amount", "Balance", "Payment Method", "Payment Date"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        UITheme.styleTable(table);
        table.setRowHeight(36);
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(25, 90, 1060, 250);
        panel.add(scroll);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowIntoForm();
            }
        });

        // ================= EDIT FORM (two columns) =================
        int y = 360, gap = 32;

        panel.add(makeLabel("Payment ID :", 25, y));
        txtPaymentId = new JTextField();
        txtPaymentId.setBounds(150, y, 160, 30);
        txtPaymentId.setEditable(false);
        txtPaymentId.setBackground(new Color(239, 246, 255));
        UITheme.styleTextField(txtPaymentId);
        panel.add(txtPaymentId);

        panel.add(makeLabel("Fee Type :", 580, y));
        cmbFeeType = new JComboBox<>(new String[]{
                "Tuition Fee", "Exam Fee", "Hostel Fee", "Library Fee", "Other Fee"});
        cmbFeeType.setBounds(705, y, 160, 30);
        UITheme.styleComboBox(cmbFeeType);
        panel.add(cmbFeeType);

        y += gap;
        panel.add(makeLabel("Receipt No :", 25, y));
        txtReceiptNo = new JTextField();
        txtReceiptNo.setBounds(150, y, 160, 30);
        UITheme.styleTextField(txtReceiptNo);
        panel.add(txtReceiptNo);

        panel.add(makeLabel("Total Fee :", 580, y));
        txtTotalFee = new JTextField();
        txtTotalFee.setBounds(705, y, 160, 30);
        UITheme.styleTextField(txtTotalFee);
        panel.add(txtTotalFee);

        y += gap;
        panel.add(makeLabel("Admission No :", 25, y));
        txtAdmissionNo = new JTextField();
        txtAdmissionNo.setBounds(150, y, 160, 30);
        UITheme.styleTextField(txtAdmissionNo);
        panel.add(txtAdmissionNo);

        panel.add(makeLabel("Paid Amount :", 580, y));
        txtPaidAmount = new JTextField();
        txtPaidAmount.setBounds(705, y, 160, 30);
        UITheme.styleTextField(txtPaidAmount);
        panel.add(txtPaidAmount);

        y += gap;
        panel.add(makeLabel("Student Name :", 25, y));
        txtStudentName = new JTextField();
        txtStudentName.setBounds(150, y, 160, 30);
        UITheme.styleTextField(txtStudentName);
        panel.add(txtStudentName);

        panel.add(makeLabel("Balance :", 580, y));
        txtBalance = new JTextField();
        txtBalance.setBounds(705, y, 160, 30);
        txtBalance.setEditable(false);
        txtBalance.setBackground(new Color(239, 246, 255));
        UITheme.styleTextField(txtBalance);
        panel.add(txtBalance);

        y += gap;
        panel.add(makeLabel("Course :", 25, y));
        txtCourse = new JTextField();
        txtCourse.setBounds(150, y, 160, 30);
        UITheme.styleTextField(txtCourse);
        panel.add(txtCourse);

        panel.add(makeLabel("Payment Method :", 580, y));
        cmbMethod = new JComboBox<>(new String[]{"Cash", "UPI", "Card", "Bank Transfer"});
        cmbMethod.setBounds(705, y, 160, 30);
        UITheme.styleComboBox(cmbMethod);
        panel.add(cmbMethod);

        y += gap;
        panel.add(makeLabel("Semester :", 25, y));
        txtSemester = new JTextField();
        txtSemester.setBounds(150, y, 160, 30);
        UITheme.styleTextField(txtSemester);
        panel.add(txtSemester);

        panel.add(makeLabel("Payment Date :", 580, y));
        txtDate = new JTextField();
        txtDate.setBounds(705, y, 160, 30);
        UITheme.styleTextField(txtDate);
        panel.add(txtDate);

        // ================= BUTTONS =================
        int by = 545, bw = 150, bh = 34;

        btnUpdate = new JButton("UPDATE");
        btnUpdate.setBounds(25, by, bw, bh);
        UITheme.styleButton(btnUpdate, new Color(245, 158, 11), new Color(217, 119, 6));
        panel.add(btnUpdate);

        btnDelete = new JButton("DELETE");
        btnDelete.setBounds(185, by, bw, bh);
        UITheme.styleButton(btnDelete, new Color(239, 68, 68), new Color(220, 38, 38));
        panel.add(btnDelete);

        btnClear = new JButton("CLEAR");
        btnClear.setBounds(345, by, bw, bh);
        UITheme.styleButton(btnClear, new Color(100, 116, 139), new Color(71, 85, 105));
        panel.add(btnClear);

        btnReceipt = new JButton("VIEW RECEIPT");
        btnReceipt.setBounds(505, by, bw, bh);
        UITheme.styleButton(btnReceipt, new Color(168, 85, 247), new Color(147, 51, 234));
        panel.add(btnReceipt);

        btnBack = new JButton("BACK");
        btnBack.setBounds(665, by, bw, bh);
        UITheme.styleButton(btnBack, new Color(59, 130, 246), new Color(37, 99, 235));
        panel.add(btnBack);

        // ---- Event handling ----
        btnRefresh.addActionListener(e -> viewAllPayments());
        btnSearch.addActionListener(e -> searchPayments());
        btnUpdate.addActionListener(e -> updatePayment());
        btnDelete.addActionListener(e -> deletePayment());
        btnClear.addActionListener(e -> clearForm());
        btnReceipt.addActionListener(e -> viewReceipt());
        btnBack.addActionListener(e -> dispose());
        txtSearch.addActionListener(e -> searchPayments());

        JPanel tableCard = new JPanel();
        tableCard.setBounds(15, 48, 1090, 310);
        UITheme.styleCard(tableCard);
        JPanel formCard = new JPanel();
        formCard.setBounds(15, 350, 1090, 225);
        UITheme.styleCard(formCard);
        panel.add(tableCard, 0);
        panel.add(formCard, 0);

        // READ - load everything when the window opens.
        viewAllPayments();
        */
    }

    private void buildModernLayout() {
        setTitle("Payment History");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1200, 750));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        UITheme.stylePanel(mainPanel);
        add(mainPanel);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Payment History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(UITheme.TEXT);
        JLabel subtitle = new JLabel("View, search and manage student fee payments.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(UITheme.MUTED);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel filterCard = new JPanel(new BorderLayout(0, 12));
        UITheme.styleCard(filterCard);
        JLabel filterTitle = new JLabel("Search Payments");
        filterTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        filterTitle.setForeground(UITheme.TEXT);
        filterCard.add(filterTitle, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);
        txtSearch = new JTextField();
        UITheme.styleSearchField(txtSearch);
        txtSearch.setToolTipText("Search by admission number or student name");
        searchRow.add(txtSearch, BorderLayout.CENTER);
        btnSearch = new JButton("Search");
        UITheme.styleButton(btnSearch, UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        searchRow.add(btnSearch, BorderLayout.EAST);
        filterCard.add(searchRow, BorderLayout.CENTER);

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        filterRow.setOpaque(false);
        filterRow.add(filterLabel("Fee Type"));
        cmbFeeTypeFilter = new JComboBox<>(new String[]{
                "All Fee Types", "Tuition Fee", "Exam Fee", "Hostel Fee", "Library Fee", "Other Fee"});
        UITheme.styleComboBox(cmbFeeTypeFilter);
        filterRow.add(cmbFeeTypeFilter);
        filterRow.add(filterLabel("Payment Method"));
        cmbMethodFilter = new JComboBox<>(new String[]{
                "All Methods", "Cash", "UPI", "Card", "Bank Transfer"});
        UITheme.styleComboBox(cmbMethodFilter);
        filterRow.add(cmbMethodFilter);
        filterCard.add(filterRow, BorderLayout.SOUTH);

        String[] columns = {"Payment ID", "Receipt No", "Admission No", "Student Name",
                "Course", "Semester", "Fee Type", "Total Fee", "Paid Amount", "Balance",
                "Payment Method", "Payment Date"};
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
        JPanel tableCard = new JPanel(new BorderLayout(0, 10));
        UITheme.styleCard(tableCard);
        JLabel tableTitle = new JLabel("Payment Records");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(UITheme.TEXT);
        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        JPanel editorCard = new JPanel(new BorderLayout(0, 8));
        UITheme.styleCard(editorCard);
        JLabel editorTitle = new JLabel("Selected Payment");
        editorTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editorTitle.setForeground(UITheme.TEXT);
        editorCard.add(editorTitle, BorderLayout.NORTH);
        JPanel editorGrid = new JPanel(new GridLayout(2, 8, 10, 6));
        editorGrid.setOpaque(false);
        txtPaymentId = field(editorGrid, "Payment ID");
        txtReceiptNo = field(editorGrid, "Receipt No");
        txtAdmissionNo = field(editorGrid, "Admission No");
        txtStudentName = field(editorGrid, "Student Name");
        txtCourse = field(editorGrid, "Course");
        txtSemester = field(editorGrid, "Semester");
        txtTotalFee = field(editorGrid, "Total Fee");
        txtPaidAmount = field(editorGrid, "Paid Amount");
        txtBalance = field(editorGrid, "Balance");
        txtDate = field(editorGrid, "Payment Date");
        cmbFeeType = new JComboBox<>(new String[]{
                "Tuition Fee", "Exam Fee", "Hostel Fee", "Library Fee", "Other Fee"});
        UITheme.styleComboBox(cmbFeeType);
        editorGrid.add(cmbFeeType);
        cmbMethod = new JComboBox<>(new String[]{"Cash", "UPI", "Card", "Bank Transfer"});
        UITheme.styleComboBox(cmbMethod);
        editorGrid.add(cmbMethod);
        editorCard.add(editorGrid, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        btnRefresh = new JButton("Show All");
        UITheme.styleButton(btnRefresh, UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnReceipt = new JButton("View");
        UITheme.styleButton(btnReceipt, new Color(59, 130, 246), new Color(37, 99, 235));
        btnUpdate = new JButton("Update");
        UITheme.styleButton(btnUpdate, UITheme.WARNING, new Color(217, 119, 6));
        btnDelete = new JButton("Delete");
        UITheme.styleButton(btnDelete, UITheme.DANGER, new Color(185, 28, 28));
        btnClear = new JButton("Clear");
        UITheme.styleButton(btnClear, new Color(100, 116, 139), new Color(71, 85, 105));
        btnBack = new JButton("Back");
        UITheme.styleButton(btnBack, UITheme.ACCENT, new Color(79, 70, 229));
        actions.add(btnRefresh);
        actions.add(btnReceipt);
        actions.add(btnUpdate);
        actions.add(btnDelete);
        actions.add(btnClear);
        actions.add(btnBack);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowIntoForm();
            }
        });
        btnRefresh.addActionListener(e -> loadPaymentHistory());
        btnSearch.addActionListener(e -> performPaymentSearch());
        txtSearch.addActionListener(e -> performPaymentSearch());
        cmbFeeTypeFilter.addActionListener(e -> performPaymentSearch());
        cmbMethodFilter.addActionListener(e -> performPaymentSearch());
        btnReceipt.addActionListener(e -> viewSelectedPayment());
        btnUpdate.addActionListener(e -> updatePayment());
        btnDelete.addActionListener(e -> deletePayment());
        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> dispose());

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setOpaque(false);
        center.add(filterCard, BorderLayout.NORTH);
        center.add(tableCard, BorderLayout.CENTER);
        center.add(editorCard, BorderLayout.SOUTH);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(actions, BorderLayout.SOUTH);

        loadPaymentHistory();
    }

    private JLabel filterLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(UITheme.TEXT);
        return label;
    }

    private JTextField field(JPanel panel, String labelText) {
        JLabel label = filterLabel(labelText);
        panel.add(label);
        JTextField field = new JTextField();
        UITheme.styleTextField(field);
        panel.add(field);
        return field;
    }

    private void viewSelectedPayment() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a payment first.",
                    "View Payment", JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadSelectedRowIntoForm();
    }

    // =====================================================
    // EVENT HANDLING
    // =====================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnRefresh) {
            txtSearch.setText("");
            viewAllPayments();
        } else if (src == btnSearch || src == txtSearch) {
            searchPayments();
        } else if (src == btnUpdate) {
            updatePayment();
        } else if (src == btnDelete) {
            deletePayment();
        } else if (src == btnClear) {
            clearForm();
        } else if (src == btnReceipt) {
            String receiptNo = txtReceiptNo.getText().trim();
            if (receiptNo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select a payment from the table first.",
                        "Receipt", JOptionPane.WARNING_MESSAGE);
            } else {
                new FeeReceipt(receiptNo).setVisible(true);
            }
        } else if (src == btnBack) {
            dispose();
        }
    }

    // =====================================================
    // READ
    // =====================================================
    private void loadPaymentHistory() {
        String sql = "SELECT * FROM payments ORDER BY payment_id DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            fillTable(rs);

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Payment history could not be loaded.");
        }
    }

    private void viewAllPayments() {
        loadPaymentHistory();
    }

    // =====================================================
    // SEARCH
    // =====================================================
    private void performPaymentSearch() {
        String keyword = txtSearch.getText().trim();

        StringBuilder sql = new StringBuilder("SELECT * FROM payments WHERE 1 = 1");
        boolean hasKeyword = !keyword.isEmpty();
        if (hasKeyword) {
            sql.append(" AND (admission_no LIKE ? OR student_name LIKE ?)");
        }
        if (cmbFeeTypeFilter != null && cmbFeeTypeFilter.getSelectedIndex() > 0) {
            sql.append(" AND fee_type = ?");
        }
        if (cmbMethodFilter != null && cmbMethodFilter.getSelectedIndex() > 0) {
            sql.append(" AND payment_method = ?");
        }
        sql.append(" ORDER BY payment_id DESC");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int parameter = 1;
            if (hasKeyword) {
                String pattern = "%" + keyword + "%";
                ps.setString(parameter++, pattern);
                ps.setString(parameter++, pattern);
            }
            if (cmbFeeTypeFilter != null && cmbFeeTypeFilter.getSelectedIndex() > 0) {
                ps.setString(parameter++, (String) cmbFeeTypeFilter.getSelectedItem());
            }
            if (cmbMethodFilter != null && cmbMethodFilter.getSelectedIndex() > 0) {
                ps.setString(parameter, (String) cmbMethodFilter.getSelectedItem());
            }

            try (ResultSet rs = ps.executeQuery()) {
                fillTable(rs);
            }

            if (model.getRowCount() == 0 && (hasKeyword
                    || cmbFeeTypeFilter.getSelectedIndex() > 0
                    || cmbMethodFilter.getSelectedIndex() > 0)) {
                JOptionPane.showMessageDialog(this, "No matching payment found.",
                        "Search", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Search could not be completed.");
        }
    }

    private void searchPayments() {
        performPaymentSearch();
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

        double totalFee, paid;
        try {
            totalFee = Double.parseDouble(txtTotalFee.getText().trim());
            paid = Double.parseDouble(txtPaidAmount.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Total fee and paid amount must be numbers.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (paid > totalFee) {
            JOptionPane.showMessageDialog(this,
                    "Paid amount cannot be more than the total fee.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Recalculate the balance before saving.
        double balance = totalFee - paid;
        txtBalance.setText(String.format("%.2f", balance));

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
            ps.setDouble(7, totalFee);
            ps.setDouble(8, paid);
            ps.setDouble(9, balance);
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
    // Helper methods
    // =====================================================
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

    private void clearForm() {
        txtPaymentId.setText("");
        txtReceiptNo.setText("");
        txtAdmissionNo.setText("");
        txtStudentName.setText("");
        txtCourse.setText("");
        txtSemester.setText("");
        cmbFeeType.setSelectedIndex(0);
        txtTotalFee.setText("");
        txtPaidAmount.setText("");
        txtBalance.setText("");
        cmbMethod.setSelectedIndex(0);
        txtDate.setText("");
        txtSearch.setText("");
        cmbFeeTypeFilter.setSelectedIndex(0);
        cmbMethodFilter.setSelectedIndex(0);
        table.clearSelection();
        loadPaymentHistory();
    }
}
