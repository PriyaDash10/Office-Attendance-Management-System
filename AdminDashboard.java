package attendancesystem;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import javax.swing.*;
import java.sql.*;

public class AdminDashboard extends JFrame {

    private JLabel lblTotalEmp;
    private JLabel lblToday;
    private JLabel lblAbsent;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AdminDashboard frame = new AdminDashboard();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        // Background
        getContentPane().setBackground(Color.BLACK);

        // Title
        JLabel lblTitle = new JLabel("ADMIN DASHBOARD");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitle.setForeground(Color.CYAN);
        lblTitle.setBounds(260, 20, 380, 35);
        getContentPane().add(lblTitle);

        // Welcome
        JLabel lblWelcome = new JLabel("Welcome Admin");
        lblWelcome.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBounds(355, 65, 180, 25);
        getContentPane().add(lblWelcome);

        // ===== STATS =====
        lblTotalEmp = new JLabel("Total Employees: Loading...");
        lblTotalEmp.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTotalEmp.setForeground(Color.YELLOW);
        lblTotalEmp.setBounds(60, 100, 250, 25);
        getContentPane().add(lblTotalEmp);

        lblToday = new JLabel("Today's Attendance: Loading...");
        lblToday.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblToday.setForeground(Color.GREEN);
        lblToday.setBounds(320, 100, 300, 25);
        getContentPane().add(lblToday);

        lblAbsent = new JLabel("Absent Employees: Loading...");
        lblAbsent.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblAbsent.setForeground(Color.RED);
        lblAbsent.setBounds(620, 100, 250, 25);
        getContentPane().add(lblAbsent);

        // ===== MENU PANEL =====
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBackground(new Color(40, 40, 40));
        menuPanel.setBounds(46, 149, 800, 220); // increased height
        getContentPane().add(menuPanel);

        // ===== ROW 1 =====

        JButton btnAdd = new JButton("Add Employee");
        btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAdd.setBounds(30, 20, 180, 40);
        btnAdd.setBackground(new Color(255, 128, 0));
        btnAdd.setForeground(Color.WHITE);
        menuPanel.add(btnAdd);

        JButton btnView = new JButton("View Attendance");
        btnView.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnView.setBounds(300, 20, 180, 40);
        btnView.setBackground(new Color(0, 102, 204));
        btnView.setForeground(Color.WHITE);
        menuPanel.add(btnView);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogout.setBounds(570, 20, 180, 40);
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.WHITE);
        menuPanel.add(btnLogout);

        // ===== ROW 2 =====

        JButton btnAbsentList = new JButton("Absent List");
        btnAbsentList.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAbsentList.setBounds(30, 110, 180, 40);
        btnAbsentList.setBackground(new Color(153, 0, 0));
        btnAbsentList.setForeground(Color.WHITE);
        menuPanel.add(btnAbsentList);

        JButton btnChart = new JButton("View Chart");
        btnChart.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnChart.setBounds(300, 110, 180, 40);
        btnChart.setBackground(new Color(102, 0, 153));
        btnChart.setForeground(Color.WHITE);
        menuPanel.add(btnChart);

        JButton btnManage = new JButton("Manage Employees");
        btnManage.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnManage.setBounds(570, 110, 180, 40);
        btnManage.setBackground(new Color(0, 153, 153));
        btnManage.setForeground(Color.WHITE);
        menuPanel.add(btnManage);

        // ===== ACTIONS =====

        btnAdd.addActionListener(e -> {
            new AddEmployee().setVisible(true);
            dispose();
        });

        btnView.addActionListener(e -> {
            new ViewAttendance().setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new Loginpage().setVisible(true);
            dispose();
        });

        btnAbsentList.addActionListener(e -> {
            new ViewAbsentEmployees().setVisible(true);
            dispose();
        });

        btnChart.addActionListener(e -> {
            new AttendanceChart().setVisible(true);
        });

        btnManage.addActionListener(e -> {
            new ManageEmployee().setVisible(true);
            dispose();
        });

        // Load stats
        loadAdminStats();
    }

    // ===== STATS METHOD =====
    private void loadAdminStats() {

        try {
            Connection con = DBConnection.getConnection();

            // Total employees
            String empQuery = "SELECT COUNT(*) FROM users WHERE role='employee'";
            PreparedStatement pst1 = con.prepareStatement(empQuery);
            ResultSet rs1 = pst1.executeQuery();

            int totalEmp = 0;
            if (rs1.next()) {
                totalEmp = rs1.getInt(1);
            }

            // Today's attendance
            String todayQuery = "SELECT COUNT(DISTINCT username) FROM attendance WHERE date=CURDATE()";
            PreparedStatement pst2 = con.prepareStatement(todayQuery);
            ResultSet rs2 = pst2.executeQuery();

            int todayCount = 0;
            if (rs2.next()) {
                todayCount = rs2.getInt(1);
            }

            int absent = totalEmp - todayCount;

            lblTotalEmp.setText("Total Employees: " + totalEmp);
            lblToday.setText("Today's Attendance: " + todayCount);
            lblAbsent.setText("Absent Employees: " + absent);

        } catch (Exception e) {
            e.printStackTrace();
            lblTotalEmp.setText("Error");
            lblToday.setText("Error");
            lblAbsent.setText("Error");
        }
    }
}