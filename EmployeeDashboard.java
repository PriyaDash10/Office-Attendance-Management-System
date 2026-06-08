package attendancesystem;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import javax.swing.*;
import java.sql.*;

public class EmployeeDashboard extends JFrame {

    private String username;
    private JLabel lblTodayStatus;

    public EmployeeDashboard(String username) {

        this.username = username;

        setTitle("Employee Dashboard");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(Color.BLACK);

        JLabel lblTitle = new JLabel("EMPLOYEE DASHBOARD");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitle.setForeground(Color.CYAN);
        lblTitle.setBounds(250, 20, 420, 35);
        add(lblTitle);

        JLabel lblWelcome = new JLabel("Welcome : " + username);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBounds(340, 65, 300, 25);
        add(lblWelcome);

        // ✅ TODAY STATUS
        lblTodayStatus = new JLabel("Today's Status: Checking...");
        lblTodayStatus.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTodayStatus.setForeground(Color.YELLOW);
        lblTodayStatus.setBounds(300, 100, 300, 25);
        add(lblTodayStatus);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBackground(new Color(40, 40, 40));
        menuPanel.setBounds(80, 140, 720, 200);
        add(menuPanel);

        JButton btnMark = new JButton("Mark Attendance");
        btnMark.setBounds(40, 30, 200, 40);
        menuPanel.add(btnMark);

        JButton btnView = new JButton("My Attendance");
        btnView.setBounds(260, 30, 200, 40);
        menuPanel.add(btnView);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(480, 30, 180, 40);
        menuPanel.add(btnLogout);

        JButton btnProfile = new JButton("My Profile");
        btnProfile.setBounds(40, 120, 200, 40);
        menuPanel.add(btnProfile);

        // ACTIONS
        btnMark.addActionListener(e -> {
            new MarkAttendance(username).setVisible(true);
            dispose();
        });

        btnView.addActionListener(e -> {
            new ViewAttendance(username).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new Loginpage().setVisible(true);
            dispose();
        });

        btnProfile.addActionListener(e -> {
            new EmployeeProfile(username).setVisible(true);
            dispose();
        });

        loadTodayStatus();
    }

    private void loadTodayStatus() {
        try {
            Connection con = DBConnection.getConnection();

            String q = "SELECT status FROM attendance WHERE username=? AND date=CURDATE()";
            PreparedStatement pst = con.prepareStatement(q);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                lblTodayStatus.setText("Today's Status: " + rs.getString("status"));
                lblTodayStatus.setForeground(Color.GREEN);
            } else {
                lblTodayStatus.setText("Today's Status: Not Marked");
                lblTodayStatus.setForeground(Color.RED);
            }

        } catch (Exception e) {
            lblTodayStatus.setText("Error");
        }
    }
}