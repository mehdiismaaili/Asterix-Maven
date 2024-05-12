/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inc.asterix.fim;

/**
 *
 * @author hp
 */

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import asterix.chart.data.category.DefaultCategoryDataset;
import asterix.chart.line.LineChart;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Font;

public class Dashboard extends JFrame {
      private Dbconnection dbConnection;
    private LineChart lineChart;

    public Dashboard() {
        initUI();
        dbConnection = new Dbconnection(); // Initialize your DB connection here
        createLineChartData();
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

        setTitle("Dashboard Statistiques des Fichiers");
        setSize(800, 600);
        getContentPane().setBackground(UIManager.getColor("Panel.background")); // Use the background color provided by the FlatLaf theme

        // Initialize your LineChart here
        lineChart = new LineChart();
        add(lineChart);
        setLocationRelativeTo(null);
    }

    private void createLineChartData() {
        String user = SessionManager.getCurrentUserEmail();
        DefaultCategoryDataset<String, String> categoryDataset = new DefaultCategoryDataset<>();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");

        try (Connection conn = dbConnection.getConnexion();
         PreparedStatement pstmt = conn.prepareStatement("SELECT DATE(detectedChangeTime) AS modDate, COUNT(*) AS modificationCount FROM FileChanges where user_email = ? GROUP BY modDate")) {
        
        pstmt.setString(1, user);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            java.sql.Date sqlDate = rs.getDate("modDate");
            String date = df.format(sqlDate);
            int modificationCount = rs.getInt("modificationCount");
            categoryDataset.addValue(modificationCount, "Modifications", date);
        }
    } catch (Exception e) {
        e.printStackTrace();
        // Handle the exception here
    }
        
        // Set the format of the values of your LineChart
        // lineChart.getPlotChart().setValuesFormat(new DecimalFormat("#,##0.##"));
        
        lineChart.setCategoryDataset(categoryDataset);
        lineChart.getChartColor().addColor(Color.decode("#38bdf8"), Color.decode("#fb7185"), Color.decode("#34d399"));
        JLabel header = new JLabel("Données des Modifications des Fichiers");
        header.putClientProperty(FlatClientProperties.STYLE, "font:+1; border:0,0,5,0");
        lineChart.setHeader(header);
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
            
        });
    }
}