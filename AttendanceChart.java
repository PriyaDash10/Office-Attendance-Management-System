package attendancesystem;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AttendanceChart extends JFrame {

    public AttendanceChart() {

        setTitle("Attendance Chart");
        setSize(650, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultPieDataset dataset = new DefaultPieDataset();

        int present = 0;
        int total = 0;

        try {
            Connection con = DBConnection.getConnection();

            // ✅ FIXED: ONLY TODAY'S PRESENT
            String presentQuery = "SELECT COUNT(DISTINCT username) FROM attendance WHERE date = CURDATE()";
            PreparedStatement pst1 = con.prepareStatement(presentQuery);
            ResultSet rs1 = pst1.executeQuery();

            if (rs1.next()) {
                present = rs1.getInt(1);
            }

            // ✅ TOTAL EMPLOYEES
            String totalQuery = "SELECT COUNT(*) FROM users WHERE role='employee'";
            PreparedStatement pst2 = con.prepareStatement(totalQuery);
            ResultSet rs2 = pst2.executeQuery();

            if (rs2.next()) {
                total = rs2.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        int absent = total - present;

        // ✅ DATASET
        dataset.setValue("Present", present);
        dataset.setValue("Absent", absent);

        // ✅ CREATE CHART
        JFreeChart chart = ChartFactory.createPieChart(
                "Employee Attendance (Today)",
                dataset,
                true,
                true,
                false
        );

        // 🎨 MODERN DESIGN
        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setSectionPaint("Present", new Color(0, 200, 83)); // Green
        plot.setSectionPaint("Absent", new Color(244, 67, 54)); // Red

        plot.setBackgroundPaint(Color.WHITE);
        chart.setBackgroundPaint(Color.WHITE);

        plot.setOutlineVisible(false);
        plot.setCircular(true);

        // 💡 Percentage Labels
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0} : {1} ({2})"
        ));

        plot.setLabelFont(new Font("Arial", Font.BOLD, 14));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200));

        // ✨ Slight modern effect
        plot.setExplodePercent("Present", 0.05);

        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 20));

        // PANEL
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        setContentPane(panel);
    }
}