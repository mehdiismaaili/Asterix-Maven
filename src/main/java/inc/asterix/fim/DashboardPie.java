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
import asterix.chart.data.pie.DefaultPieDataset;
import asterix.chart.pie.PieChart;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Font;

public class DashboardPie extends JFrame {
  private Dbconnection dbConnection;
    private PieChart pieChart;

    public DashboardPie() {
        initUI();
        dbConnection = new Dbconnection(); // Initialize your DB connection here
        createPieChart();
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

        setTitle("Dashboard File Statistics");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(UIManager.getColor("Panel.background"));

        add(panel);
    }

    private void createPieChart() {
        pieChart = new PieChart();
        JLabel header = new JLabel("File Statistics");
        header.putClientProperty(FlatClientProperties.STYLE, "font:+1");
        pieChart.setHeader(header);

        // Set custom colors for the chart
        pieChart.getChartColor().addColor(Color.decode("#34d399"), Color.decode("#f87171"), Color.decode("#fb923c"));

        // Set border style for the chart
        pieChart.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5;");


        // Set dataset for the chart
        pieChart.setDataset(createPieData());

        // Add the PieChart to the UI
        add(pieChart, BorderLayout.CENTER);
    }

    private DefaultPieDataset createPieData() {
    String user = SessionManager.getCurrentUserEmail();
    DefaultPieDataset dataset = new DefaultPieDataset();

    try (Connection conn = dbConnection.getConnexion()) {
        // Query for integrated files count
        String integresQuery = "SELECT COUNT(*) FROM Files WHERE status = 'Normal' AND user = ?";
        try (PreparedStatement pstmtIntegres = conn.prepareStatement(integresQuery)) {
            pstmtIntegres.setString(1, user);
            ResultSet rsIntegres = pstmtIntegres.executeQuery();
            if (rsIntegres.next()) {
                dataset.setValue("Integrated Files", rsIntegres.getInt(1));
            }
        }

        // Query for modified files count
        String modifiesQuery = "SELECT COUNT(*)  FROM FileChanges WHERE changeType = 'Modification' and user_email = ?";
        try (PreparedStatement pstmtModifies = conn.prepareStatement(modifiesQuery)) {
            pstmtModifies.setString(1, user);
            ResultSet rsModifies = pstmtModifies.executeQuery();
            if (rsModifies.next()) {
                dataset.setValue("Modified Files", rsModifies.getInt(1));
            }
        }

        // Query for files under surveillance count
        String surveillanceQuery = "SELECT COUNT(*) FROM Files WHERE surveillance = 'TRUE' AND user = ?";
        try (PreparedStatement pstmtSurveillance = conn.prepareStatement(surveillanceQuery)) {
            pstmtSurveillance.setString(1, user);
            ResultSet rsSurveillance = pstmtSurveillance.executeQuery();
            if (rsSurveillance.next()) {
                dataset.setValue("Files Under Surveillance", rsSurveillance.getInt(1));
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardPie dashboard = new DashboardPie();
            dashboard.setVisible(true);
        });
    }
}
