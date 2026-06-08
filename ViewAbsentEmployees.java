package attendancesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewAbsentEmployees extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewAbsentEmployees() {

        setTitle("Absent Employees Today");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(Color.BLACK);

        // Title
        JLabel lblTitle = new JLabel("ABSENT EMPLOYEES (TODAY)");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitle.setForeground(Color.RED);
        lblTitle.setBounds(150, 20, 400, 30);
        add(lblTitle);

        // Table
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Username");

        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(50, 80, 580, 250);
        add(pane);

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(280, 350, 120, 35);
        btnBack.setBackground(new Color(0, 102, 204));
        btnBack.setForeground(Color.WHITE);
        add(btnBack);

        btnBack.addActionListener(e -> {
            new AdminDashboard().setVisible(true);
            dispose();
        });

        // Load data
        loadAbsentEmployees();
    }

    private void loadAbsentEmployees() {

        try {
            Connection con = DBConnection.getConnection();

            String query =
                    "SELECT id, name, username FROM users " +
                    "WHERE role='employee' AND username NOT IN " +
                    "(SELECT username FROM attendance WHERE date=CURDATE())";

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
}