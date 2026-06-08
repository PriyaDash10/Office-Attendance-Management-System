package attendancesystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddEmployee extends JFrame {

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtUsername;
    private JTextField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new AddEmployee().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AddEmployee() {

        setTitle("Add Employee");
        setSize(420, 470);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        // Title
        JLabel lblTitle = new JLabel("ADD EMPLOYEE");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.CYAN);
        lblTitle.setBounds(105, 20, 220, 30);
        add(lblTitle);

        // Employee ID
        JLabel lblId = new JLabel("Employee ID:");
        lblId.setForeground(Color.WHITE);
        lblId.setBounds(40, 80, 100, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(160, 80, 180, 25);
        add(txtId);

        // Name
        JLabel lblName = new JLabel("Full Name:");
        lblName.setForeground(Color.WHITE);
        lblName.setBounds(40, 120, 100, 25);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(160, 120, 180, 25);
        add(txtName);

        // Username
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(40, 160, 100, 25);
        add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(160, 160, 180, 25);
        add(txtUsername);

        // Password
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(40, 200, 100, 25);
        add(lblPass);

        txtPassword = new JTextField();
        txtPassword.setBounds(160, 200, 180, 25);
        add(txtPassword);

        // Add Button
        JButton btnAdd = new JButton("Add Employee");
        btnAdd.setBounds(120, 270, 160, 35);
        btnAdd.setBackground(new Color(0, 153, 76));
        btnAdd.setForeground(Color.WHITE);
        add(btnAdd);

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(120, 325, 160, 35);
        btnBack.setBackground(new Color(0, 102, 204));
        btnBack.setForeground(Color.WHITE);
        add(btnBack);

        // Add Employee Action
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String id = txtId.getText().trim();
                String name = txtName.getText().trim();
                String username = txtUsername.getText().trim();
                String password = txtPassword.getText().trim();

                if (id.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                    return;
                }

                try {
                    Connection con = DBConnection.getConnection();

                    String query = "INSERT INTO users(id,name,username,password,role) VALUES (?,?,?,?,?)";

                    PreparedStatement pst = con.prepareStatement(query);

                    pst.setInt(1, Integer.parseInt(id));
                    pst.setString(2, name);
                    pst.setString(3, username);
                    pst.setString(4, password);  
                    pst.setString(5, "employee");

                    int i = pst.executeUpdate();

                    if (i > 0) {
                        JOptionPane.showMessageDialog(null, "Employee Added Successfully!");
                    }

                    txtId.setText("");
                    txtName.setText("");
                    txtUsername.setText("");
                    txtPassword.setText("");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error Adding Employee!");
                }
            }
        });

        // Back Action
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AdminDashboard().setVisible(true);
                dispose();
            }
        });
    }
}