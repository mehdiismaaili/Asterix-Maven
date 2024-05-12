/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inc.asterix.fim;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
 
import asterix.chart.bar.HorizontalBarChart;
import asterix.chart.data.pie.DefaultPieDataset;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Font;

public class DashboardBarChart extends JFrame {
     private Dbconnection dbConnection;
    private HorizontalBarChart barChart;

    public DashboardBarChart() {
        initUI();
        dbConnection = new Dbconnection(); // Initialize your DB connection here
        createBarChart();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    private void initUI() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Set the FlatLaf dark theme
            FlatRobotoFont.install();
            FlatLaf.registerCustomDefaultsSource("raven.themes");
            UIManager.put("defaultFont",  new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
            FlatMacDarkLaf.setup();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("Logs Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(UIManager.getColor("Panel.background"));
        add(panel);
    }

    private void createBarChart() {
        barChart = new HorizontalBarChart();
        JLabel header = new JLabel("Log Levels");
        header.putClientProperty(FlatClientProperties.STYLE, "font:+1; border:0,0,5,0");
        barChart.setHeader(header);
        barChart.setBarColor(Color.decode("#34d399"));
        barChart.setDataset(createData());

        // Create a panel to hold the chart
        JPanel panel = new JPanel(new BorderLayout());
        panel.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5;");
        panel.add(barChart);

        // Add the panel with the chart to the UI
        add(panel, BorderLayout.CENTER);
    }


    private DefaultPieDataset<String> createData() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        String user = SessionManager.getCurrentUserEmail();
        try (Connection conn = dbConnection.getConnexion();
             PreparedStatement pstmt = conn.prepareStatement("SELECT logLevel, COUNT(*) as count FROM Logs WHERE user_email = ? GROUP BY logLevel")) {

            // Set the user_email parameter
            pstmt.setString(1, user);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String logLevel = rs.getString("logLevel");
                int count = rs.getInt("count");
                dataset.setValue(logLevel, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }

        return dataset;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardBarChart dashboard = new DashboardBarChart();
            dashboard.setVisible(true);
        });
    }
}