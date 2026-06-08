package attendancesystem;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;

public class MarkAttendance extends JFrame {

    private String username;
    private JLabel lblMsg;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MarkAttendance frame = new MarkAttendance("employee");
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MarkAttendance(String username) {

        this.username = username;

        setTitle("Mark Attendance");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(245, 245, 245));

        // Title
        JLabel lblTitle = new JLabel("MARK ATTENDANCE");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBounds(180, 20, 320, 35);
        add(lblTitle);

        // Username
        JLabel lblUser = new JLabel("Employee Name:");
        lblUser.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblUser.setBounds(150, 100, 150, 25);
        add(lblUser);

        JLabel lblUsername = new JLabel(username);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblUsername.setBounds(320, 100, 180, 25);
        add(lblUsername);

        // Date
        JLabel lblDateText = new JLabel("Date:");
        lblDateText.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblDateText.setBounds(150, 150, 150, 25);
        add(lblDateText);

        JLabel lblDate = new JLabel(LocalDate.now().toString());
        lblDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDate.setBounds(320, 150, 180, 25);
        add(lblDate);

        // Time
        JLabel lblTimeText = new JLabel("Time:");
        lblTimeText.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTimeText.setBounds(150, 190, 150, 25);
        add(lblTimeText);

        JLabel lblTime = new JLabel(LocalTime.now().withNano(0).toString());
        lblTime.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTime.setBounds(320, 190, 180, 25);
        add(lblTime);

        // Mark Button
        JButton btnMark = new JButton("Mark Attendance");
        btnMark.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnMark.setBounds(240, 250, 170, 40);
        btnMark.setBackground(new Color(0, 153, 76));
        btnMark.setForeground(Color.WHITE);
        add(btnMark);

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnBack.setBounds(240, 305, 170, 40);
        btnBack.setBackground(new Color(0, 102, 204));
        btnBack.setForeground(Color.WHITE);
        add(btnBack);

        // Message
        lblMsg = new JLabel("");
        lblMsg.setBounds(200, 360, 350, 25);
        add(lblMsg);

        // Actions
        btnMark.addActionListener(e -> markAttendance());

        btnBack.addActionListener(e -> {
            new EmployeeDashboard(username).setVisible(true);
            dispose();
        });
    }

    private void markAttendance() {

        try {
            Connection con = DBConnection.getConnection();

            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now().withNano(0);

            // 🔍 STEP 1: Check duplicate attendance
            String checkQuery = "SELECT * FROM attendance WHERE username=? AND date=?";
            PreparedStatement checkPst = con.prepareStatement(checkQuery);

            checkPst.setString(1, username);
            checkPst.setDate(2, java.sql.Date.valueOf(today));

            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                lblMsg.setForeground(Color.RED);
                lblMsg.setText("Attendance already marked today!");
                return;
            }

            // ⏰ STEP 2: Decide status
            String status;

            if (now.isAfter(LocalTime.of(9, 30))) {
                status = "Late";
            } else {
                status = "Present";
            }

            // ✅ STEP 3: Insert attendance
            String query = "INSERT INTO attendance(username,date,time,status) VALUES (?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, username);
            pst.setDate(2, java.sql.Date.valueOf(today));
            pst.setTime(3, java.sql.Time.valueOf(now));
            pst.setString(4, status);

            int i = pst.executeUpdate();

            if (i > 0) {
                lblMsg.setForeground(new Color(0, 153, 0));
                lblMsg.setText("Attendance Marked: " + status);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            lblMsg.setForeground(Color.RED);
            lblMsg.setText("Error marking attendance!");
        }
    }
}