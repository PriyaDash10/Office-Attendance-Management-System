package attendancesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageEmployee extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ManageEmployee() {

        setTitle("Manage Employees");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        // Table
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Username");

        table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(50, 50, 700, 250);
        add(pane);

        // Buttons
        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(200, 330, 120, 35);
        add(btnEdit);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(350, 330, 120, 35);
        add(btnDelete);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(500, 330, 120, 35);
        add(btnBack);

        // Load data
        loadEmployees();

        // Edit action
        btnEdit.addActionListener(e -> editEmployee());

        // Delete action
        btnDelete.addActionListener(e -> deleteEmployee());

        // Back
        btnBack.addActionListener(e -> {
            new AdminDashboard().setVisible(true);
            dispose();
        });
    }

    // LOAD EMPLOYEES
    private void loadEmployees() {
        try {
            Connection con = DBConnection.getConnection();

            model.setRowCount(0);

            String query = "SELECT id, name, username FROM users WHERE role='employee'";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("username")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EDIT
    private void editEmployee() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }

        String id = model.getValueAt(row, 0).toString();
        String name = model.getValueAt(row, 1).toString();
        String username = model.getValueAt(row, 2).toString();

        String newName = JOptionPane.showInputDialog("Edit Name:", name);
        String newUsername = JOptionPane.showInputDialog("Edit Username:", username);

        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE users SET name=?, username=? WHERE id=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, newName);
            pst.setString(2, newUsername);
            pst.setString(3, id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Updated Successfully!");

            loadEmployees();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    private void deleteEmployee() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }

        String id = model.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this employee?");

        if (confirm == 0) {

            try {
                Connection con = DBConnection.getConnection();

                String query = "DELETE FROM users WHERE id=?";
                PreparedStatement pst = con.prepareStatement(query);

                pst.setString(1, id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Deleted Successfully!");

                loadEmployees();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}