/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inc.asterix.fim;

import static inc.asterix.fim.FileMonitoring.convertPriorityToString;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author mehdi
 */
public class Dbconnection {
    
    // cmJ79&49&9Gc50204cc5
    private static final String URL = "jdbc:mysql://51.68.49.205:3306/asterix_fim";
    private static final String UTILISATEUR = "aster";
    private static final String MOT_DE_PASSE = "hightech2024";

    // Methode to get the connection
    public Connection getConnexion() {
        Connection connexion = null;
        try {
          
            Class.forName("com.mysql.cj.jdbc.Driver");

            
            connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la connexion à la base de données : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return connexion;
    }
    
    // signup methode will all the validation checks
    public boolean signup(String fullname, String email, String password){
         boolean authenticated = false; // Initialize the boolean variable
        // Declaring the a string that conatins the hashed password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        // Opening connectio to database
        try (Connection conn = getConnexion()) {
        // Check if the email already exists in the database
        String checkEmailQuery = "SELECT * FROM users WHERE Email = ?";
        try (PreparedStatement checkEmailStatement = conn.prepareStatement(checkEmailQuery)) {
            checkEmailStatement.setString(1, email);
            try (ResultSet emailResultSet = checkEmailStatement.executeQuery()) {
                if (emailResultSet.next()) {
                    JOptionPane.showMessageDialog(null, "Email already exists. Please use a different email.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    // Perform the insertion
                    String insertQuery = "INSERT INTO users (Full_Name, Email, Password) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, fullname);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, BCrypt.hashpw(password, BCrypt.gensalt()));
                        preparedStatement.executeUpdate();
                        authenticated = true;
                    }
                    catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error in database operation", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
            }
            }
            }
        }} catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, "Error in database operation", "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
       }

        return authenticated;
    }
    
    
    // signin methode will all the validation checks
    public boolean signin(String email, String enteredPassword) {
    boolean authenticated = false; // Initialize the boolean variable
    
    try (Connection conn = getConnexion()) {
        String sql = "SELECT * FROM users WHERE Email = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, email);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) {
                    String storedHashedPassword = resultSet.getString("Password");

                    if (BCrypt.checkpw(enteredPassword, storedHashedPassword)) {
                        // Authentication successful
                        authenticated = true;
                        String userEmail = email;
                        SessionManager.loginUser(userEmail);
                    } else {
                        // Authentication failed
                        JOptionPane.showMessageDialog(null, "Invalid Password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // No matching user found
                    JOptionPane.showMessageDialog(null, "User Not Found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error in database operation", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    
    return authenticated; // Return the boolean value
}

    // methode that will verify the serect key of the user
    public boolean secretkey(String code, String useremail) {
    boolean authenticated = false; // Initialize the boolean variable
    try (Connection conn = getConnexion()) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, useremail);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) {
                    String storedSecretKey = resultSet.getString("Secret_Key");

                    if (code.equals(storedSecretKey)) {
                        // Authentication successful
                        authenticated = true;
                        JOptionPane.showMessageDialog(null, "Sign-in successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Authentication failed
                        JOptionPane.showMessageDialog(null, "Invalid Secret Key", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error in database operation", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    
    return authenticated; // Return the boolean value
}
    
    // methode that will genrate and store the secrret key for the user
    public void getsecretkey(String Code, String useremail){
        try (Connection conn = getConnexion()) {
        String updateQuery = "UPDATE users SET secret_key = ? WHERE Email = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, Code);
            preparedStatement.setString(2, useremail);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sign-up successful", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error storing secret code in the database", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    // methode to check identity to change pass
    public boolean checkkey(String useremail, String code) {
    boolean authenticated = false; // Initialize the boolean variable
    try (Connection conn = getConnexion()) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, useremail);

            try (ResultSet resultSet = pstm.executeQuery()) {
                if (resultSet.next()) {
                    String storedSecretKey = resultSet.getString("Secret_Key");

                    if (code.equals(storedSecretKey)) {
                        // Key found
                        JOptionPane.showMessageDialog(null, "Identity Verified Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        String email = useremail;
                        SessionManager.loginUser(email);
                        authenticated = true; // Set to true since authentication is successful
                    } else {
                        // Key not found or doesn't exist
                        JOptionPane.showMessageDialog(null, "Invalid Secret Key", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "User Not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error in database operation", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    
    return authenticated; // Return the boolean value
}

    // methode that will update the user pass
    public void passupdate(String useremail, String pass2){
         try (Connection conn = getConnexion()) {
        String sql = "Select * from users where email = ?"; 
         try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, useremail);
            try (ResultSet resultSet = pstm.executeQuery()) {
                
                        // Check if there is a matching user
                        if (resultSet.next()) {
                        String updateQuery = "UPDATE users SET Password = ? WHERE Email = ?";
                        try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, BCrypt.hashpw(pass2, BCrypt.gensalt()));
                        preparedStatement.setString(2, useremail);
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Password Updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
        }
                        } else {
                        JOptionPane.showMessageDialog(null, "User Not found", "Error", JOptionPane.ERROR_MESSAGE);

                        }
                }
            } catch (SQLException ex) {

            } 
            
    }catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error in database operation", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            }
    }
    
    
    // methode that will referehs the data
    public void refreshTable(JTable table) {
    String email = SessionManager.getCurrentUserEmail();
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0); // Clear existing rows

    // Make sure the SQL query includes the 'priority' column
    String sql = "SELECT filePath, fileName, expectedHash, user, status, priority FROM Files WHERE user = ?";
    try (Connection conn = getConnexion();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, email); // Set the user parameter in the prepared statement

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String filePath = rs.getString("filePath");
                String fileName = rs.getString("fileName");
                String expectedHash = rs.getString("expectedHash");
                String user = rs.getString("user");
                String status = rs.getString("status");
                int priority = rs.getInt("priority"); // Retrieve the priority value

                // Convert numeric priority to a string representation if necessary
                String priorityString = convertPriorityToString(priority); // Use the convertPriorityToString method defined earlier

                // Add priority as an additional column in the table
                model.addRow(new Object[]{filePath, fileName, expectedHash, user, status, priorityString});
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error refreshing the table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    }

    
    // methode that will update 
    public void updateFilePriority(String filePath, int newPriority) {
    String sql = "UPDATE Files SET priority = ? WHERE filePath = ?";
    try (Connection conn = getConnexion();  
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, newPriority);
        pstmt.setString(2, filePath);
        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            JOptionPane.showMessageDialog(null, "Priorty Updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "File Not Found.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error while trying to update : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    // methode that will insert the selected file into the database
   public void insertFileIntoDatabase(File file, String fileHash, String user, int priority) {
    // Check if the file already exists
    if (fileExistsInDatabase(file.getAbsolutePath())) {
        JOptionPane.showMessageDialog(null, "Le fichier existe déjà dans la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method
    } else if (file.length() == 0) {
        JOptionPane.showMessageDialog(null, "Error: The file " + file.getName() + " is empty and cannot be processed.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Get the user's home directory
    String userHomeDirectory = System.getProperty("user.home");

    // Create the monitored files directory if it doesn't exist
    String monitoredFilesDirectoryPath = userHomeDirectory + File.separator + "MonitoredFiles";
    createMonitoredFilesDirectory(monitoredFilesDirectoryPath);

    // Copy the file to the monitored files directory
    copyFileToMonitoredDirectory(file, monitoredFilesDirectoryPath);
    
    // Prepare SQL statement to insert file into the database
    
    String sql = "INSERT INTO Files (filePath, fileName, expectedHash, user, status, surveillance, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = getConnexion();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Set parameters for the SQL statement
        pstmt.setString(1, file.getAbsolutePath()); // Full file path
        pstmt.setString(2, file.getName()); // File name
        pstmt.setString(3, fileHash); // File hash
        pstmt.setString(4, user);
        pstmt.setString(5, "Normal"); // Status
        pstmt.setString(6, "TRUE"); // Surveillance
        pstmt.setInt(7, priority);

        // Execute the SQL statement
        pstmt.executeUpdate();

        JOptionPane.showMessageDialog(null, "File Added Succssefuly.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erreur adding the file : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
   // methode that will create the specifuc dir
    private void createMonitoredFilesDirectory(String directoryPath) {
        // Create the monitored files directory if it doesn't exist
        File monitoredFilesDirectory = new File(directoryPath);
        if (!monitoredFilesDirectory.exists()) {
            monitoredFilesDirectory.mkdirs();
        }
    }
    
    // methode that will copy the selected file in the scpecifc dir
    private void copyFileToMonitoredDirectory(File file, String monitoredFilesDirectoryPath) {
        try {
            Path sourceFilePath = file.toPath();
            Path destinationFilePath = Paths.get(monitoredFilesDirectoryPath, file.getName());
            Files.copy(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la copie du fichier : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
    // methode that will check of the selected file is alredy added in the database
    private boolean fileExistsInDatabase(String filePath) {
        String sql = "SELECT COUNT(*) FROM Files WHERE filePath = ?";
        try (Connection conn = getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, filePath);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // methode that will delet the selected file
    public void deleteFileFromDatabase(String filePath) {
        String sql = "DELETE FROM Files WHERE filePath = ?";
        try (Connection conn = getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, filePath);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Deleted with success.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "File not found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur deleting the file : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // this methode will reet the selected file status
    public void resetstaus(String filepath){
         String sql = "update Files set status = 'Normal' WHERE filePath = ?";
        try (Connection conn = getConnexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, filepath);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "File Status reseted succecssfully.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "File not found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur deleting the file : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
    // this methode that will verify the files integrity and will report any change
    public void verifyAndLogFileModification(File file, int[] modifiedLineNumbers) {
        String email = SessionManager.getCurrentUserEmail();  
        String currentUser = System.getProperty("user.name");
        File oldversion = getPreviousVersionOfFile(file);
        try {
            // Check if the file is not empty
            
            String currentHash = calculateFileHash(file.toPath());
            String sqlSelect = "SELECT fileId, expectedHash FROM Files WHERE fileName = ?";
            try (Connection conn = getConnexion();
                 PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
                
                pstmtSelect.setString(1, file.getName());
                ResultSet rs = pstmtSelect.executeQuery();
                
                if (rs.next()) {
                    String expectedHash = rs.getString("expectedHash");
                    int fileId = rs.getInt("fileId");
                    
                    if (!expectedHash.equals(currentHash)) {
                        // Update status and hash in Files
                        String sqlUpdate = "UPDATE Files SET status = ?, expectedHash = ? WHERE fileId = ?";
                        try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
                            pstmtUpdate.setString(1, "Modified");
                            pstmtUpdate.setString(2, currentHash);
                            pstmtUpdate.setInt(3, fileId);
                            pstmtUpdate.executeUpdate();
                        }

                         // Determine the change type
                        String changeType;
                        if(!currentHash.equals(expectedHash)){
                            changeType = "Content Changed";
                        }else if (!file.getName().equals(oldversion.getName())) {
                            changeType = "Name Changed";
                        } else if (!file.getParent().equals(oldversion.getParent())) {
                            changeType = "Position Changed";
                        }else{
                            changeType = "UNKNOWN";
                        }

                        // Insert modified lines into FileChanges table
                        String sqlInsertChange = "INSERT INTO FileChanges (fileId, detectedChangeTime, modifcationtime, detectedHash, user_email, changeType, chgline) VALUES (?, NOW(), ?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmtChange = conn.prepareStatement(sqlInsertChange)) {
                            pstmtChange.setInt(1, fileId);
                            pstmtChange.setTimestamp(2, getFileModificationTime(file));
                            pstmtChange.setString(3, currentHash);
                            pstmtChange.setString(4, email);
                            pstmtChange.setString(5, "Modification");
                           if (modifiedLineNumbers.length > 0) {
                                int lastModifiedLineNumber = modifiedLineNumbers[modifiedLineNumbers.length - 1];
                                String line = String.valueOf(lastModifiedLineNumber);
                                pstmtChange.setString(6, line);
                            } else {
                                pstmtChange.setString(6, "Line Added");
                            }
                            // Execute the update
                            pstmtChange.executeUpdate();
                        }
                        // Insert log entry into Logs table
                        String sqlInsertLog = "INSERT INTO Logs (timestamp, modtime, logLevel, message, log_user, user_email, modline) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmtLog = conn.prepareStatement(sqlInsertLog)) {
                            pstmtLog.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                            pstmtLog.setTimestamp(2, getFileModificationTime(file));
                            pstmtLog.setString(3, getLogLevel(changeType));
                            pstmtLog.setString(4, "A modification has been detected for the file: " + file.getName());
                            pstmtLog.setString(5, "The modification was performed by: " + currentUser);
                            pstmtLog.setString(6, email);
                           if (modifiedLineNumbers.length > 0) {
                                int lastModifiedLineNumber = modifiedLineNumbers[modifiedLineNumbers.length - 1];
                                String line = String.valueOf(lastModifiedLineNumber);
                                pstmtLog.setString(7, line);
                            } else {
                                pstmtLog.setString(7, "New Line Added Go Compare");
                            }

                            // Execute the update
                            pstmtLog.executeUpdate();
                        }
                        
                        // Display desktop notification
                        DesktopNotifier notifier = new DesktopNotifier();
                        notifier.displayNotification("Modification detected", "The file " + file.getName() + " has been modified.");
                        
                        JOptionPane.showMessageDialog(null, "Modification detected and recorded for " + file.getName());
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error verifying file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
    // // methode that will retrun the change type
    private String getLogLevel(String changeType) {
        switch (changeType) {
            case "Content Changed":
                return "INFO";
            case "Name Changed":
                return "NAME CHANGED";
            case "Position Changed":
                return "POSITION";
            default:
                return "UNKNOWN";
        }
    }

    
    // methode that will the exact change date for the changed file
    private Timestamp getFileModificationTime(File file) {
    try {
        Path path = Paths.get(file.getAbsolutePath());
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        long modificationTime = attrs.lastModifiedTime().toMillis();
        return new Timestamp(modificationTime);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
    }
    
    
    // methode that will return the line where the survielled files are changed(only works for exiting lines)
    public int[] compareFiless(File oldVersion, File newVersion) throws IOException {
         BufferedReader originalReader = new BufferedReader(new FileReader(oldVersion));
        BufferedReader modifiedReader = new BufferedReader(new FileReader(newVersion));

        String originalLine;
        String modifiedLine;
        int lineNumber = 1;
        int modifiedLinesCount = 0;

        // Count number of modified lines
        while ((originalLine = originalReader.readLine()) != null && (modifiedLine = modifiedReader.readLine()) != null) {
            if (!originalLine.equals(modifiedLine)) {
                modifiedLinesCount++;
            }
            lineNumber++;
        }

        // Close readers
        originalReader.close();
        modifiedReader.close();

        // Create array to store modified line numbers
        int[] modifiedLineNumbers = new int[modifiedLinesCount];
        
        // Reset line number and read files again to store modified line numbers
        originalReader = new BufferedReader(new FileReader(oldVersion));
        modifiedReader = new BufferedReader(new FileReader(newVersion));
        lineNumber = 1;
        int index = 0;

        // Store modified line numbers
        while ((originalLine = originalReader.readLine()) != null && (modifiedLine = modifiedReader.readLine()) != null) {
            if (!originalLine.equals(modifiedLine)) {
                modifiedLineNumbers[index++] = lineNumber;
            }
            lineNumber++;
        }

        // Close readers
        originalReader.close();
        modifiedReader.close();

        return modifiedLineNumbers;
    }
    
    
    // methode that will return the oringinal version of the 
    private File getPreviousVersionOfFile(File currentFile) {
        Path desktopPath = Paths.get(System.getProperty("user.home"), "MonitoredFiles");
        Path previousVersionPath = desktopPath.resolve(currentFile.getName());
        return previousVersionPath.toFile();
    }

    
    // methode that will calculate the file hash 
    public static String calculateFileHash(Path path) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fileContent = Files.readAllBytes(path);
        byte[] hashBytes = digest.digest(fileContent);
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    // methode that will check the file type
    public class FileTypeDetector {
    public static String getFileType(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".txt")) {
            return "text";
        } else if (fileName.endsWith(".docx")) {
            return "word";
        } else if (fileName.endsWith(".xlsx")) {
            return "excel";
        } else {
            return "unsupported";
        }
    }
}
    
}
