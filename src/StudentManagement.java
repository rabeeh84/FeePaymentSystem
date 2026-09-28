import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
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
 * StudentManagement.java
 *
 * Complete CRUD screen for students.
 *
 *   Swing UI  ->  Event handling  ->  CRUD methods  ->  JDBC  ->  MySQL
 *
 * Every database call uses a PreparedStatement, and the JTable is
 * refreshed after every Add, Update or Delete.
 */
public class StudentManagement extends JFrame implements ActionListener {

    // ---- Form fields ----
    private final JTextField txtStudentId, txtAdmissionNo, txtName, txtDepartment, txtPhone, txtEmail;
    private final JComboBox<String> cmbCourse, cmbSemester;

    // ---- Buttons ----
    private final JButton btnAdd, btnView, btnUpdate, btnDelete, btnClear, btnSearch, btnBack;

    // ---- Search box ----
    private final JTextField txtSearch;

    // ---- Table ----
    private final JTable table;
    private final DefaultTableModel model;

    public StudentManagement() {
        setTitle("Student Management");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);   // closes only this window

        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        UITheme.stylePanel(panel);
        add(panel);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel lblTitle = new JLabel("Student Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(UITheme.TEXT);
        JLabel subtitle = new JLabel("Manage student records, academic details and contact information.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(UITheme.MUTED);
        header.add(lblTitle);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        panel.add(header, BorderLayout.NORTH);

        JPanel searchCard = new JPanel(new BorderLayout(14, 0));
        UITheme.styleCard(searchCard);
        JLabel searchLabel = new JLabel("Search student records");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(UITheme.TEXT);
        searchCard.add(searchLabel, BorderLayout.WEST);
        JPanel searchControls = new JPanel(new BorderLayout(10, 0));
        searchControls.setOpaque(false);
        txtSearch = new JTextField();
        UITheme.styleSearchField(txtSearch);
        txtSearch.setToolTipText("Search by admission number or student name");
        searchControls.add(txtSearch, BorderLayout.CENTER);
        btnSearch = new JButton("Search");
        UITheme.styleButton(btnSearch, UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        searchControls.add(btnSearch, BorderLayout.EAST);
        searchCard.add(searchControls, BorderLayout.CENTER);

        JPanel formCard = new JPanel(new BorderLayout(0, 14));
        UITheme.styleCard(formCard);
        JLabel formTitle = new JLabel("Student Information");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(UITheme.TEXT);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formGrid = new JPanel(new GridLayout(4, 4, 18, 10));
        formGrid.setOpaque(false);
        formGrid.add(makeFormLabel("Student ID"));
        txtStudentId = new JTextField();
        txtStudentId.setEditable(false);
        UITheme.styleTextField(txtStudentId);
        formGrid.add(txtStudentId);
        formGrid.add(makeFormLabel("Admission Number *"));
        txtAdmissionNo = new JTextField();
        UITheme.styleTextField(txtAdmissionNo);
        formGrid.add(txtAdmissionNo);
        formGrid.add(makeFormLabel("Student Name *"));
        txtName = new JTextField();
        UITheme.styleTextField(txtName);
        formGrid.add(txtName);
        formGrid.add(makeFormLabel("Course *"));
        cmbCourse = new JComboBox<>(new String[]{
                "-- Select --", "B.Tech", "M.Tech", "MCA", "MBA", "B.Sc", "B.Com"});
        UITheme.styleComboBox(cmbCourse);
        formGrid.add(cmbCourse);
        formGrid.add(makeFormLabel("Semester *"));
        cmbSemester = new JComboBox<>(new String[]{
                "-- Select --", "S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8"});
        UITheme.styleComboBox(cmbSemester);
        formGrid.add(cmbSemester);
        formGrid.add(makeFormLabel("Department"));
        txtDepartment = new JTextField();
        UITheme.styleTextField(txtDepartment);
        formGrid.add(txtDepartment);
        formGrid.add(makeFormLabel("Phone"));
        txtPhone = new JTextField();
        UITheme.styleTextField(txtPhone);
        formGrid.add(txtPhone);
        formGrid.add(makeFormLabel("Email"));
        txtEmail = new JTextField();
        UITheme.styleTextField(txtEmail);
        formGrid.add(txtEmail);
        formCard.add(formGrid, BorderLayout.CENTER);

        JPanel formActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        formActions.setOpaque(false);
        btnAdd = new JButton("Add Student");
        UITheme.styleButton(btnAdd, UITheme.PRIMARY, UITheme.PRIMARY_DARK);
        btnView = new JButton("View");
        UITheme.styleButton(btnView, new Color(59, 130, 246), new Color(37, 99, 235));
        btnUpdate = new JButton("Update");
        UITheme.styleButton(btnUpdate, UITheme.WARNING, new Color(217, 119, 6));
        btnDelete = new JButton("Delete");
        UITheme.styleButton(btnDelete, UITheme.DANGER, new Color(185, 28, 28));
        btnClear = new JButton("Clear");
        UITheme.styleButton(btnClear, new Color(100, 116, 139), new Color(71, 85, 105));
        btnBack = new JButton("Back");
        UITheme.styleButton(btnBack, new Color(100, 116, 139), new Color(71, 85, 105));
        formActions.add(btnAdd);
        formActions.add(btnView);
        formActions.add(btnUpdate);
        formActions.add(btnDelete);
        formActions.add(btnClear);
        formActions.add(btnBack);
        formCard.add(formActions, BorderLayout.SOUTH);

        String[] columns = {"ID", "Admission No", "Name", "Course",
                            "Semester", "Department", "Phone", "Email"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        JPanel tableCard = new JPanel(new BorderLayout(0, 10));
        UITheme.styleCard(tableCard);
        JLabel recordsTitle = new JLabel("Student Records");
        recordsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        recordsTitle.setForeground(UITheme.TEXT);
        tableCard.add(recordsTitle, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);

        // ---- When a row is selected, load that row into the form ----
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowIntoForm();
            }
        });

        // ---- Event handling ----
        btnAdd.addActionListener(e -> addStudent());
        btnView.addActionListener(e -> viewAllStudents());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> performStudentSearch());
        btnBack.addActionListener(e -> dispose());
        txtSearch.addActionListener(e -> performStudentSearch());   // Enter key inside the search box

        JPanel upper = new JPanel(new BorderLayout(0, 14));
        upper.setOpaque(false);
        upper.add(searchCard, BorderLayout.NORTH);
        upper.add(formCard, BorderLayout.CENTER);
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setOpaque(false);
        content.add(upper, BorderLayout.NORTH);
        content.add(tableCard, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);

        // Show all students as soon as the window opens (READ).
        viewAllStudents();
    }

    /** Small helper so we do not repeat the same three lines for every label. */
    private JLabel makeFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(UITheme.TEXT);
        return lbl;
    }

    // =====================================================
    // EVENT HANDLING - decides which CRUD method to call
    // =====================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnAdd) {
            addStudent();
        } else if (src == btnView) {
            viewAllStudents();
        } else if (src == btnUpdate) {
            updateStudent();
        } else if (src == btnDelete) {
            deleteStudent();
        } else if (src == btnClear) {
            clearForm();
        } else if (src == btnSearch || src == txtSearch) {
            performStudentSearch();
        } else if (src == btnBack) {
            dispose();
        }
    }

    // =====================================================
    // CREATE
    // =====================================================
    private void addStudent() {
        // ---- Validation ----
        if (!validateForm()) {
            return;
        }

        String sql = "INSERT INTO students " +
                     "(admission_no, student_name, course, semester, department, phone, email) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtAdmissionNo.getText().trim());
            ps.setString(2, txtName.getText().trim());
            ps.setString(3, (String) cmbCourse.getSelectedItem());
            ps.setString(4, (String) cmbSemester.getSelectedItem());
            ps.setString(5, txtDepartment.getText().trim());
            ps.setString(6, txtPhone.getText().trim());
            ps.setString(7, txtEmail.getText().trim());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student added successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                viewAllStudents();      // refresh the JTable
            }

        } catch (SQLException ex) {
            // Error 1062 = duplicate value for a UNIQUE column.
            if (ex.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this,
                    "Admission number already exists.",
                        "Duplicate", JOptionPane.ERROR_MESSAGE);
            } else {
                DatabaseConnection.showError(ex, "Student could not be added.");
            }
        }
    }

    // =====================================================
    // READ - show every student in the JTable
    // =====================================================
    private void viewAllStudents() {
        String sql = "SELECT * FROM students ORDER BY student_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            fillTable(rs);

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Student list could not be loaded.");
        }
    }

    // =====================================================
    // SEARCH - by admission number or by name
    // =====================================================
    private void performStudentSearch() {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            viewAllStudents();      // empty box = show everything
            return;
        }

        // LIKE with % lets the user type only part of the name/number.
        String sql = "SELECT * FROM students " +
                     "WHERE admission_no LIKE ? OR student_name LIKE ? " +
                     "ORDER BY student_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                fillTable(rs);
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No student found.",
                        "Search", JOptionPane.INFORMATION_MESSAGE);
            } else if (model.getRowCount() == 1) {
                table.setRowSelectionInterval(0, 0);
                loadSelectedRowIntoForm();
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Search could not be completed.");
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================
    private void updateStudent() {
        String id = txtStudentId.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student from the table first.",
                    "Update", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm()) {
            return;
        }

        String sql = "UPDATE students SET admission_no = ?, student_name = ?, course = ?, " +
                     "semester = ?, department = ?, phone = ?, email = ? " +
                     "WHERE student_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtAdmissionNo.getText().trim());
            ps.setString(2, txtName.getText().trim());
            ps.setString(3, (String) cmbCourse.getSelectedItem());
            ps.setString(4, (String) cmbSemester.getSelectedItem());
            ps.setString(5, txtDepartment.getText().trim());
            ps.setString(6, txtPhone.getText().trim());
            ps.setString(7, txtEmail.getText().trim());
            ps.setInt(8, Integer.parseInt(id));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student updated successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                viewAllStudents();
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Student could not be updated.");
        }
    }

    // =====================================================
    // DELETE
    // =====================================================
    private void deleteStudent() {
        String id = txtStudentId.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student from the table first.",
                    "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ---- Confirmation ----
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));
            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                viewAllStudents();
            }

        } catch (SQLException ex) {
            DatabaseConnection.showError(ex, "Student could not be deleted.");
        }
    }

    // =====================================================
    // Helper methods
    // =====================================================

    /** Copies every row of the ResultSet into the JTable model. */
    private void fillTable(ResultSet rs) throws SQLException {
        model.setRowCount(0);           // remove the old rows first

        while (rs.next()) {
            Object[] row = {
                rs.getInt("student_id"),
                rs.getString("admission_no"),
                rs.getString("student_name"),
                rs.getString("course"),
                rs.getString("semester"),
                rs.getString("department"),
                rs.getString("phone"),
                rs.getString("email")
            };
            model.addRow(row);
        }
    }

    /** Puts the selected table row back into the form so it can be edited. */
    private void loadSelectedRowIntoForm() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        txtStudentId.setText(String.valueOf(model.getValueAt(modelRow, 0)));
        txtAdmissionNo.setText(safe(model.getValueAt(modelRow, 1)));
        txtName.setText(safe(model.getValueAt(modelRow, 2)));
        cmbCourse.setSelectedItem(safe(model.getValueAt(modelRow, 3)));
        cmbSemester.setSelectedItem(safe(model.getValueAt(modelRow, 4)));
        txtDepartment.setText(safe(model.getValueAt(modelRow, 5)));
        txtPhone.setText(safe(model.getValueAt(modelRow, 6)));
        txtEmail.setText(safe(model.getValueAt(modelRow, 7)));
    }

    /** Turns a null database value into an empty string. */
    private String safe(Object value) {
        return (value == null) ? "" : value.toString();
    }

    /** Checks the required fields. Returns true only if everything is fine. */
    private boolean validateForm() {
        if (txtAdmissionNo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Admission number is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            txtAdmissionNo.requestFocus();
            return false;
        }
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student name is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return false;
        }
        if (cmbCourse.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Course is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (cmbSemester.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Semester is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /** Empties the form and unselects the table. */
    private void clearForm() {
        txtStudentId.setText("");
        txtAdmissionNo.setText("");
        txtName.setText("");
        cmbCourse.setSelectedIndex(0);
        cmbSemester.setSelectedIndex(0);
        txtDepartment.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        table.clearSelection();
        txtAdmissionNo.requestFocus();
    }
}
