package attendancesystem;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewAttendance extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private String username;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ViewAttendance frame = new ViewAttendance();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Admin
    public ViewAttendance() {
        this.username = null;
        initialize();
        loadAllAttendance();
    }

    // Employee
    public ViewAttendance(String username) {
        this.username = username;
        initialize();
        loadEmployeeAttendance();
    }

    private void initialize() {

        setTitle("View Attendance");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(Color.BLACK);

        // Title
        JLabel lblTitle = new JLabel("ATTENDANCE DETAILS");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 26));
        lblTitle.setForeground(Color.CYAN);
        lblTitle.setBounds(250, 20, 400, 35);
        add(lblTitle);

        // Table Model
        model = new DefaultTableModel();

        model.addColumn("Username");
        model.addColumn("Date");
        model.addColumn("Time");
        model.addColumn("Status");

        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(70, 90, 760, 320);
        add(pane);

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnBack.setBounds(380, 440, 120, 35);
        btnBack.setBackground(new Color(0, 102, 204));
        btnBack.setForeground(Color.WHITE);
        add(btnBack);

        btnBack.addActionListener(e -> {
            if (username == null) {
                new AdminDashboard().setVisible(true);
            } else {
                new EmployeeDashboard(username).setVisible(true);
            }
            dispose();
        });
        //Export to Excel
        
        JButton btnExport = new JButton("Export to Excel");
        btnExport.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnExport.setBounds(520, 440, 180, 35);
        btnExport.setBackground(new Color(0, 153, 76));
        btnExport.setForeground(Color.WHITE);
        add(btnExport);
        btnExport.addActionListener(e -> exportToCSV());
       
    }
      
    private void exportToCSV() {

        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save File");

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {

                java.io.File fileToSave = fileChooser.getSelectedFile();

                java.io.FileWriter writer = new java.io.FileWriter(fileToSave + ".csv");

                // Column headers
                for (int i = 0; i < table.getColumnCount(); i++) {
                    writer.append(table.getColumnName(i));
                    if (i < table.getColumnCount() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");

                // Table data
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        writer.append(table.getValueAt(i, j).toString());
                        if (j < table.getColumnCount() - 1) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }

                writer.flush();
                writer.close();

                JOptionPane.showMessageDialog(this, "Exported Successfully!");

            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error exporting file!");
        }
    }

	// Admin sees all attendance
    private void loadAllAttendance() {

        try {
            Connection con = DBConnection.getConnection();

            String query = "select * from attendance";
            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getString("username"),
                    rs.getDate("date"),
                    rs.getTime("time"),
                    rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Employee sees own attendance
    private void loadEmployeeAttendance() {

        try {
            Connection con = DBConnection.getConnection();

            String query = "select * from attendance where username=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getString("username"),
                    rs.getDate("date"),
                    rs.getTime("time"),
                    rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}