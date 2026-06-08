package attendancesystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EmployeeProfile extends JFrame {

    private String username;
    private JTextField txtName;
    private JPasswordField txtOld, txtNew;

    public EmployeeProfile(String username) {

        this.username = username;

        setTitle("Employee Profile");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(Color.BLACK);

        JLabel title = new JLabel("MY PROFILE");
        title.setFont(new Font("Tahoma", Font.BOLD, 22));
        title.setForeground(Color.CYAN);
        title.setBounds(120, 20, 200, 30);
        add(title);

        // 👤 PROFILE ICON
        ImageIcon icon = new ImageIcon(getClass().getResource("/attendancesystem/profileimg.png"));
        Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel lblIcon = new JLabel(new ImageIcon(img));
        lblIcon.setBounds(150, 60, 80, 80);
        add(lblIcon);

        JLabel lblId = new JLabel();
        lblId.setForeground(Color.WHITE);
        lblId.setBounds(50, 150, 300, 25);
        add(lblId);

        txtName = new JTextField();
        txtName.setBounds(50, 180, 250, 25);
        add(txtName);

        JLabel lblUser = new JLabel();
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(50, 220, 300, 25);
        add(lblUser);

        JLabel lblRole = new JLabel();
        lblRole.setForeground(Color.WHITE);
        lblRole.setBounds(50, 250, 300, 25);
        add(lblRole);

        JButton btnUpdate = new JButton("Update Name");
        btnUpdate.setBounds(120, 280, 150, 30);
        add(btnUpdate);

        // 🔐 OLD PASSWORD
        JLabel lblOld = new JLabel("Enter Old Password:");
        lblOld.setForeground(Color.WHITE);
        lblOld.setBounds(50, 320, 200, 20);
        add(lblOld);

        txtOld = new JPasswordField();
        txtOld.setBounds(50, 340, 200, 25);
        add(txtOld);

        JCheckBox showOld = new JCheckBox("Show");
        showOld.setBounds(260, 340, 70, 25);
        showOld.setBackground(Color.BLACK);
        showOld.setForeground(Color.WHITE);
        add(showOld);

        // 🔐 NEW PASSWORD
        JLabel lblNew = new JLabel("Enter New Password:");
        lblNew.setForeground(Color.WHITE);
        lblNew.setBounds(50, 370, 200, 20);
        add(lblNew);

        txtNew = new JPasswordField();
        txtNew.setBounds(50, 390, 200, 25);
        add(txtNew);

        JCheckBox showNew = new JCheckBox("Show");
        showNew.setBounds(260, 390, 70, 25);
        showNew.setBackground(Color.BLACK);
        showNew.setForeground(Color.WHITE);
        add(showNew);

        JButton btnPass = new JButton("Change Password");
        btnPass.setBounds(100, 430, 180, 30);
        add(btnPass);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(120, 470, 150, 30);
        add(btnBack);

        // 👁️ SHOW/HIDE LOGIC
        showOld.addActionListener(e -> {
            txtOld.setEchoChar(showOld.isSelected() ? (char) 0 : '•');
        });

        showNew.addActionListener(e -> {
            txtNew.setEchoChar(showNew.isSelected() ? (char) 0 : '•');
        });

        // ACTIONS
        btnBack.addActionListener(e -> {
            new EmployeeDashboard(username).setVisible(true);
            dispose();
        });

        btnUpdate.addActionListener(e -> updateName());
        btnPass.addActionListener(e -> changePassword());

        // LOAD DATA
        try {
            Connection con = DBConnection.getConnection();

            String q = "SELECT * FROM users WHERE username=?";
            PreparedStatement pst = con.prepareStatement(q);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                lblId.setText("ID: " + rs.getString("id"));
                txtName.setText(rs.getString("name"));
                lblUser.setText("Username: " + rs.getString("username"));
                lblRole.setText("Role: " + rs.getString("role"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateName() {
        try {
            Connection con = DBConnection.getConnection();

            String q = "UPDATE users SET name=? WHERE username=?";
            PreparedStatement pst = con.prepareStatement(q);

            pst.setString(1, txtName.getText());
            pst.setString(2, username);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Name Updated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePassword() {
        try {
            Connection con = DBConnection.getConnection();

            String oldPass = new String(txtOld.getPassword());
            String newPass = new String(txtNew.getPassword());

            String check = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pst = con.prepareStatement(check);

            pst.setString(1, username);
            pst.setString(2, oldPass);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                String update = "UPDATE users SET password=? WHERE username=?";
                PreparedStatement pst2 = con.prepareStatement(update);

                pst2.setString(1, newPass);
                pst2.setString(2, username);
                pst2.executeUpdate();

                JOptionPane.showMessageDialog(this, "Password Changed!");

            } else {
                JOptionPane.showMessageDialog(this, "Wrong Old Password!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}