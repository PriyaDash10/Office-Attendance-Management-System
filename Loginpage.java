package attendancesystem;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.Font;
import java.sql.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.GridBagLayout;
import java.net.URL;


public class Loginpage extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> roleBox;
    private JLabel lblMessage;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Loginpage frame = new Loginpage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Loginpage() {
        setTitle("Office Attendance System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(857, 540);
        setLocationRelativeTo(null);

        // Background Image
        JLabel background;
        URL location = getClass().getResource("/attendancesystem/pic1.jpeg");

        if (location != null) {
            ImageIcon icon = new ImageIcon(location);
            Image img = icon.getImage().getScaledInstance(857, 540, Image.SCALE_SMOOTH);
            background = new JLabel(new ImageIcon(img));
        } else {
            background = new JLabel();
        }

        background.setLayout(new GridBagLayout());
        setContentPane(background);

        // Panel
        JPanel panel = new JPanel();
        panel.setPreferredSize(new java.awt.Dimension(320, 260));
        panel.setBackground(new Color(255, 255, 255, 200));
        panel.setLayout(null);
        background.add(panel);

        // Username
        JLabel nameLbl = new JLabel("Enter Username:");
        nameLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
        nameLbl.setBounds(20, 20, 130, 25);
        panel.add(nameLbl);

        txtUsername = new JTextField();
        txtUsername.setBounds(160, 20, 130, 25);
        panel.add(txtUsername);

        // Password
        JLabel passLbl = new JLabel("Enter Password:");
        passLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
        passLbl.setBounds(20, 60, 130, 25);
        panel.add(passLbl);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 60, 130, 25);
        panel.add(txtPassword);

        // Role
        JLabel roleLbl = new JLabel("Select Role:");
        roleLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
        roleLbl.setBounds(20, 100, 130, 25);
        panel.add(roleLbl);

        roleBox = new JComboBox<>(new String[]{"admin", "employee"});
        roleBox.setBounds(160, 100, 130, 25);
        panel.add(roleBox);

        // Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.GREEN);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(100, 140, 100, 30);
        panel.add(btnLogin);

        // Message Label
        lblMessage = new JLabel("");
        lblMessage.setBounds(60, 180, 200, 20);
        panel.add(lblMessage);

        // Action
        btnLogin.addActionListener(e -> login());
    }

    private void login() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String role = roleBox.getSelectedItem().toString();

        lblMessage.setText("");

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setForeground(Color.RED);
            lblMessage.setText("Please fill all fields");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            // ✅ SIMPLE LOGIN (NO HASHING)
            String query = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, username);
            pst.setString(2, password);   // ✅ plain password
            pst.setString(3, role);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                lblMessage.setForeground(new Color(0, 153, 0));
                lblMessage.setText("Login Successful!");

                JOptionPane.showMessageDialog(this, "Welcome " + username);

                if (role.equalsIgnoreCase("admin")) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new EmployeeDashboard(username).setVisible(true);
                }

                dispose();

            } else {
                lblMessage.setForeground(Color.RED);
                lblMessage.setText("Invalid Username or Password");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setForeground(Color.RED);
            lblMessage.setText("Database Error!");
        }
    }
}