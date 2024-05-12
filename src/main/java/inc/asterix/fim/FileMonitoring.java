package inc.asterix.fim;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */


import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import static inc.asterix.fim.Dbconnection.calculateFileHash;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author mehdi
 */
public class FileMonitoring extends javax.swing.JFrame {
    
    private Dbconnection dbConnection;
    private boolean canClose = false;
    
    
    public FileMonitoring() throws IOException {
        initComponents();
        this.dbConnection = new Dbconnection();
        priorityComboBox.addItem("Low");
        priorityComboBox.addItem("Intermediate");
        priorityComboBox.addItem("High");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        loadData();
        verification();
        init();
    }
    
    // this is a methode that will allow the user to controle all the added files and see wich ones are modifed it will run as soon as the app runs
    public void verification() throws IOException{
        loadData();
        int rowCount = jTable1.getRowCount();
        for (int i = 0; i < rowCount; i++) {
        String filePath = jTable1.getModel().getValueAt(i, 0).toString();
        File file = new File(filePath);
        File old = getPreviousVersionOfFile(file);
        int[] modifiedLineNumbers = dbConnection.compareFiless(old, file);
        dbConnection.verifyAndLogFileModification(file, modifiedLineNumbers);
    }
    }
    
    // here we customized our UI
    public void init(){
        verify.setText("Verify");
        addfiles.setText("Add Files");
        deletefiles.setText("Delete Files");
        comparepr.setText("Compare");
        upprior.setText("Up Priority");
        dashboard.setText("Dashboard");
        dashboardchart.setText("DashboradChart");
        dashboardpie1.setText("DashBoardPie");
        reset.setText("Reset Status");
        logs.setText("Logs");
        refresh.setText("Refresh");
        exit.setText("Logout");
        jTable1.setDefaultRenderer(Object.class, new TableGradientCell());
        jPanel1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:1,1,1,1,$TableHeader.bottomSeparatorColor,,10");
        jTable1.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background");
        jScrollPane1.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:3,0,3,0,$Table.background,10,10");
        jScrollPane1.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "hoverTrackColor:null");
        verify.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        addfiles.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        deletefiles.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        comparepr.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        upprior.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        dashboard.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        dashboardchart.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        dashboardpie1.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        logs.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        exit.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        refresh.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        reset.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        jLabel1.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        jLabel1.setText("Dashboard");
        jTextFieldSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search your files here");
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        addfiles = new javax.swing.JButton();
        deletefiles = new javax.swing.JButton();
        comparepr = new javax.swing.JButton();
        refresh = new javax.swing.JButton();
        dashboard = new javax.swing.JButton();
        upprior = new javax.swing.JButton();
        logs = new javax.swing.JButton();
        verify = new javax.swing.JButton();
        dashboardchart = new javax.swing.JButton();
        dashboardpie1 = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jTextFieldSearch = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        priorityComboBox = new javax.swing.JComboBox<>();
        reset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Path", "Name", "Hash", "User", "Status", "Priority"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        addfiles.setText("jButton1");
        addfiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addfilesActionPerformed(evt);
            }
        });

        deletefiles.setText("jButton2");
        deletefiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletefilesActionPerformed(evt);
            }
        });

        comparepr.setText("jButton3");
        comparepr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compareprActionPerformed(evt);
            }
        });

        refresh.setText("jButton4");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        dashboard.setText("jButton4");
        dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardActionPerformed(evt);
            }
        });

        upprior.setText("jButton4");
        upprior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uppriorActionPerformed(evt);
            }
        });

        logs.setText("jButton4");
        logs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logsActionPerformed(evt);
            }
        });

        verify.setText("jButton4");
        verify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyActionPerformed(evt);
            }
        });

        dashboardchart.setText("jButton4");
        dashboardchart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardchartActionPerformed(evt);
            }
        });

        dashboardpie1.setText("jButton4");
        dashboardpie1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardpie1ActionPerformed(evt);
            }
        });

        exit.setText("jButton4");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        jTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchActionPerformed(evt);
            }
        });
        jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 36)); // NOI18N
        jLabel1.setText("jLabel1");

        reset.setText("jButton1");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(318, 318, 318))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1027, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(226, 226, 226)
                        .addComponent(priorityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(exit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                        .addComponent(dashboardchart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dashboardpie1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dashboard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(upprior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comparepr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addfiles, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(reset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(verify, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(deletefiles, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(priorityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addfiles, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(verify, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(deletefiles, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comparepr, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(upprior, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(logs, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(reset, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dashboardpie1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dashboardchart, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    private void addfilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addfilesActionPerformed
        // TODO add your handling code here:
         String user = SessionManager.getCurrentUserEmail();
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Calcul du hash du fichier
                String fileHash = calculateFileHash(selectedFile.toPath());

                // Copie du fichier dans le dossier spécifié
                Path desktopPath = Paths.get(System.getProperty("user.home"), "MonitoredFiles");
                Files.createDirectories(desktopPath);
                Path targetPath = desktopPath.resolve(selectedFile.getName());
                Files.copy(selectedFile.toPath(), targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Récupération de la priorité sélectionnée dans la JComboBox
                String selectedPriority = (String) priorityComboBox.getSelectedItem();
                int priorityValue = convertPriorityToValue(selectedPriority); // Vous devez implémenter cette méthode

                // Insertion du fichier dans la base de données avec la priorité
                dbConnection.insertFileIntoDatabase(selectedFile, fileHash, user, priorityValue);

                // Rafraîchissement de la table
                dbConnection.refreshTable(jTable1);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors du traitement du fichier : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_addfilesActionPerformed

    private void compareprActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compareprActionPerformed
        // TODO add your handling code here:
        // With code we can compare the changes in the files
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            String filePath = jTable1.getModel().getValueAt(selectedRow, 0).toString(); 
            File currentFile = new File(filePath);
            File previousVersionFile = getPreviousVersionOfFile(currentFile); 

            try {
                String differences = compareFiles(previousVersionFile, currentFile); 
                JOptionPane.showMessageDialog(this, differences, "Différences Fichier", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur comparing files : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected to be compared.", "No selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_compareprActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
// to refresh the table
        loadData();
    }//GEN-LAST:event_refreshActionPerformed

    private void deletefilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletefilesActionPerformed
        // TODO add your handling code here:
        // this code will delete the selected file
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to deleted ths file?", "Warning", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                String filePath = jTable1.getModel().getValueAt(selectedRow, 0).toString(); 
                dbConnection.deleteFileFromDatabase(filePath);
                dbConnection.refreshTable(jTable1); 
            } else {
                // User chose not to proceed, handle it here if needed
            }
        } else {
            JOptionPane.showMessageDialog(this, "Choose a file to delete.", "No selectin", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_deletefilesActionPerformed

    private void uppriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uppriorActionPerformed
        // TODO add your handling code here:
         // This code will update the file priority
        int newPriority = priorityComboBox.getSelectedIndex() + 1; // +1 si vos priorités commencent à 1
        updateFilePriority(newPriority);
    }//GEN-LAST:event_uppriorActionPerformed

    private void dashboardpie1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardpie1ActionPerformed
        // TODO add your handling code here:
        // will show the dashboardpie
        DashboardPie dashboard = new DashboardPie();
        dashboard.setVisible(false);
        dashboard.setVisible(true);
    }//GEN-LAST:event_dashboardpie1ActionPerformed

    private void dashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardActionPerformed
        // TODO add your handling code here:
        // will show the dashboard
        Dashboard dashboard = new Dashboard();
        dashboard.setVisible(false);
        dashboard.setVisible(true);
    }//GEN-LAST:event_dashboardActionPerformed

    private void verifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyActionPerformed
        // TODO add your handling code here:
        // this button will when pressed will run a verification on all th files in database
        loadData();
        int rowCount = jTable1.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String filePath = jTable1.getModel().getValueAt(i, 0).toString();
            File file = new File(filePath);
            File old = getPreviousVersionOfFile(file);
            try {
                int[] modifiedLineNumbers = dbConnection.compareFiless(old, file);
                dbConnection.verifyAndLogFileModification(file, modifiedLineNumbers);
            } catch (IOException ex) {
                Logger.getLogger(FileMonitoring.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_verifyActionPerformed

    private void dashboardchartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardchartActionPerformed
        // TODO add your handling code here:
        // will show the dashboardbarchart
        DashboardBarChart dashboard = new DashboardBarChart();
        dashboard.setVisible(false);
        dashboard.setVisible(true);
    }//GEN-LAST:event_dashboardchartActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        // Button to close the form
        int result = JOptionPane.showConfirmDialog(FileMonitoring.this, "Are you sure you want to close?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            canClose = true;
            dispose(); // Close the frame
        }
    }//GEN-LAST:event_exitActionPerformed

    private void logsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logsActionPerformed
        // TODO add your handling code here:
        // will open the  logs page
        String email = SessionManager.getCurrentUserEmail();
        String userEmail = email;
        SessionManager.loginUser(userEmail);
        Logs log = new Logs();
        log.setVisible(true);
        
    }//GEN-LAST:event_logsActionPerformed

    
    
    private void jTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchActionPerformed
       
    }//GEN-LAST:event_jTextFieldSearchActionPerformed

    private void jTextFieldSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchKeyReleased
        // TODO add your handling code here:
        // will show only the files that match the entered name (search functionality)
        String filter = jTextFieldSearch.getText();
        loadTextData(filter); // Mettre à jour la JTable avec le 
    }//GEN-LAST:event_jTextFieldSearchKeyReleased

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        // TODO add your handling code here:
        // this code will reset the selected file
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            String filePath = jTable1.getModel().getValueAt(selectedRow, 0).toString(); 
            dbConnection.resetstaus(filePath);
            dbConnection.refreshTable(jTable1); 
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fichier à supprimer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_resetActionPerformed
    
    // change priotity to int to be stored in database
    private int convertPriorityToValue(String priority) {
        switch (priority) {
            case "Low":
                return 1;
            case "Intermediate":
                return 2;
            case "High":
                return 3;
            default:
                return 0; 
        }
    }
// change priotity to string to be shown for the user
    public static String convertPriorityToString(int priority) {
        switch (priority) {
            case 1:
                return "Low";
            case 2:
                return "Intermediate";
            case 3:
                return "High";
            default:
                return "Not Defined"; 
        }
    }
    
    
    // this methode will load the data in the table
    private void loadData() {
    String email = SessionManager.getCurrentUserEmail();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); 

    String currentUser = System.getProperty("user.name");
    String sql = "SELECT fileId, filePath, fileName, expectedHash, user, status, priority FROM Files WHERE user = ?";
    try (Connection conn = dbConnection.getConnexion();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, email);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int fileId = rs.getInt("fileId");
                String filePath = rs.getString("filePath");
                String fileName = rs.getString("fileName");
                String expectedHash = rs.getString("expectedHash");
                String user = rs.getString("user");
                String status = rs.getString("status");
                int priority = rs.getInt("priority");

                String priorityString = convertPriorityToString(priority);

                File file = new File(filePath);
                if (!file.exists()) {
                    logFileNotFoundEvent(fileName, email, conn);
                    logFileNotFoundEventToLogs(fileName, email, currentUser, conn);
                    DesktopNotifier notifier = new DesktopNotifier();
                    notifier.displayNotification("File was not found", "The file " + file.getName() + " has been deleted or misplaced.");
                    // Delete the missing file from the database
                    deleteMissingFileFromDatabase(fileId, conn);
                    
                    continue;
                }

                model.addRow(new Object[]{filePath, fileName, expectedHash, user, status, priorityString});
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }
    
    // will delete the file from  the database if is not found in the specified dir
    private void deleteMissingFileFromDatabase(int fileId, Connection conn) throws SQLException {
        String deleteSql = "DELETE FROM Files WHERE fileId = ?";
        try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteSql)) {
            pstmtDelete.setInt(1, fileId);
            pstmtDelete.executeUpdate();
        }
    }
    
    // this methode will isert to the filechanges that the spicifed file is not found in the spicific dir
    private void logFileNotFoundEvent(String fileName, String email, Connection conn) throws SQLException {
    String sqlInsertChange = "INSERT INTO FileChanges (detectedChangeTime, detectedHash, user_email, changeType, chgline) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pstmtChange = conn.prepareStatement(sqlInsertChange)) {
        Timestamp detectedChangeTime = new Timestamp(System.currentTimeMillis());
        pstmtChange.setTimestamp(1, detectedChangeTime);
        pstmtChange.setString(2, "File Not Found");
        pstmtChange.setString(3, email);
        pstmtChange.setString(4, "File Not Found");
        pstmtChange.setString(5, "File Not Found");
        pstmtChange.executeUpdate();
    }
    }
    
     // this methode will isert to the logs that the spicifed file is not found in the spicific dir
    private void logFileNotFoundEventToLogs(String fileName, String email, String currentUser, Connection conn) throws SQLException {
    String sqlInsertLog = "INSERT INTO Logs (timestamp, logLevel, message, log_user, user_email, modline) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmtLog = conn.prepareStatement(sqlInsertLog)) {
        pstmtLog.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        pstmtLog.setString(2, "POSITION"); // Log level for file not found event
        pstmtLog.setString(3, "File not found: " + fileName);
        pstmtLog.setString(4, currentUser); // Log the current user who performed the action
        pstmtLog.setString(5, email);
        pstmtLog.setString(6, "File not found");
        pstmtLog.executeUpdate();
    }
    }
    
    
    
    
    // updates the files priority
    private void updateFilePriority(int newPriority) {
       int selectedRow = jTable1.getSelectedRow();
       if (selectedRow == -1) {
           JOptionPane.showMessageDialog(this, "Choose a file to Update.", "No selectin", JOptionPane.WARNING_MESSAGE);
           return;
       }
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to proceed?", "Warning", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            String filePath = (String) jTable1.getValueAt(selectedRow, 0);
            dbConnection.updateFilePriority(filePath, newPriority);
        } else {
           
        }
   }
    
    // compares the modifed verison with the original version
    private String compareFiles(File oldVersion, File newVersion) throws IOException {
        List<String> oldContent = Files.readAllLines(oldVersion.toPath());
        List<String> newContent = Files.readAllLines(newVersion.toPath());

        StringBuilder differences = new StringBuilder();
        int maxSize = Math.max(oldContent.size(), newContent.size());
        for (int i = 0; i < maxSize; i++) {
            if (i >= oldContent.size() || i >= newContent.size() || !oldContent.get(i).equals(newContent.get(i))) {
                differences.append(String.format("Ligne %d is diffrent\n", i+1));
                if (i < oldContent.size()) differences.append(String.format("Old: %s\n", oldContent.get(i)));
                if (i < newContent.size()) differences.append(String.format("New: %s\n", newContent.get(i)));
            }
        }

        return differences.toString();
    }
    
    // retruns the file previous version wich is in a specific dir
    private File getPreviousVersionOfFile(File currentFile) {
        Path desktopPath = Paths.get(System.getProperty("user.home"), "MonitoredFiles");
        Path previousVersionPath = desktopPath.resolve(currentFile.getName());
        return previousVersionPath.toFile();
    }
     
    
    // will listen to the search bar and shows the files that match the entered test
    public void loadTextData(String filter) {
    String email = SessionManager.getCurrentUserEmail();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // Clear existing rows

        // SQL query with flexible search conditions
    String sql = "SELECT filePath, fileName, expectedHash, user, status, priority FROM Files WHERE (fileName LIKE ? OR expectedHash LIKE ? OR CAST(priority AS CHAR) LIKE ?) AND user = ?";

    try (Connection conn = dbConnection.getConnexion();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        String searchFilter = "%" + filter + "%";
        for (int i = 1; i <= 3; i++) {
            pstmt.setString(i, searchFilter);
        }
        pstmt.setString(4, email); // Set user parameter

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String filePath = rs.getString("filePath");
                String fileName = rs.getString("fileName");
                String expectedHash = rs.getString("expectedHash");
                String user = rs.getString("user");
                String status = rs.getString("status");
                int priority = rs.getInt("priority");

                // Use convertPriorityToString to transform numeric priority to text
                String priorityString = convertPriorityToString(priority);
                model.addRow(new Object[]{filePath, fileName, expectedHash, user, status, priorityString});
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    }
    
    public static void main(String args[]) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("raven.themes");
        UIManager.put("defaultFont",  new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FileMonitoring().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(FileMonitoring.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addfiles;
    private javax.swing.JButton comparepr;
    private javax.swing.JButton dashboard;
    private javax.swing.JButton dashboardchart;
    private javax.swing.JButton dashboardpie1;
    private javax.swing.JButton deletefiles;
    private javax.swing.JButton exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldSearch;
    private javax.swing.JButton logs;
    private javax.swing.JComboBox<String> priorityComboBox;
    private javax.swing.JButton refresh;
    private javax.swing.JButton reset;
    private javax.swing.JButton upprior;
    private javax.swing.JButton verify;
    // End of variables declaration//GEN-END:variables
}
