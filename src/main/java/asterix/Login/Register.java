/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asterix.Login;

import com.formdev.flatlaf.FlatClientProperties;
import inc.asterix.fim.Dbconnection;
import inc.asterix.fim.SessionManager;
import java.awt.Component;
import java.awt.Cursor;
import asterix.component.PasswordStrengthStatus;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import asterix.manager.FormsManager;

/**
 *
 * @author mehdi
 */
public class Register extends JPanel {
    
    private Dbconnection dbconnection; 
    
     public Register() {
        this.dbconnection = new Dbconnection();
        init();
    }
     
     
    // Methode for checking if the password is strong
    private boolean isPasswordStrong(String password) {
    // Check if the password contains at least one uppercase letter, one digit, and one special character
    return password.matches(".*[A-Z].*") && password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*()-_+=].*");
    }
     
    public void init(){
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtFullname = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        cmdRegister = new JButton("Sign Up");
        
        cmdRegister.addActionListener(e -> {       // Declaring strings that contain the names of textFields
        String fullname = txtFullname.getText();
        String email = txtUsername.getText();
        String plainTextPassword = new String(txtPassword.getPassword());
        String plainTextPassword2 = new String(txtConfirmPassword.getPassword());

        // Check if all the text fields are not empty
        if (txtFullname.equals("") || txtUsername.getText().equals("") || txtPassword.getText().equals("") ||  txtConfirmPassword.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            int minLength = 3;
            int maxLength = 50;
            String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
            // Condition if the Full Name is of the appropriate length
            if (fullname.length() < minLength || fullname.length() > maxLength)
            {
                JOptionPane.showMessageDialog(this, "Full Name should be between " + minLength + " and " + maxLength + " characters.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Condition if the Full Name has no specila characters
            else if (!fullname.matches("^[a-zA-Z\\s]+$")) {
                JOptionPane.showMessageDialog(this, "Full Name can only contain letters and spaces.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Condition if the Full Name is of the appropraite format
            else if (!fullname.matches("^[a-zA-Z]+\\s[a-zA-Z]+$")) {
                JOptionPane.showMessageDialog(this, "Invalid Full Name format. Use First Name and Last Name separated by a space.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Condition if the Email of correct format
            else if (!email.matches(emailPattern)) {
                JOptionPane.showMessageDialog(this, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Condition if the Email has no capital chrarcters
            else if (email.matches(".*[A-Z].*"))
            {

                JOptionPane.showMessageDialog(null, "Emails Email Can't contain upper case letters");

            }
            // Condition if the Email is not of the edu domain
            else if (email.toLowerCase().contains("@edu") || email.toLowerCase().endsWith(".edu"))
            {

                JOptionPane.showMessageDialog(null, "Emails With the edu domain are not valide");
            }
            // Condition if the Password is of the appropru=ite length
            else if (plainTextPassword.length() < 8)
            {
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long!", "Error", JOptionPane.ERROR_MESSAGE);

            }
            // Condition if the Password  has no special invalide characters
            else if (!plainTextPassword.matches("^[a-zA-Z0-9!@#$%^&*()-_+=]+$"))
            {

                JOptionPane.showMessageDialog(null, "Password contains invalid characters!", "Error", JOptionPane.ERROR_MESSAGE);

            }
            // Condition if the Password is not weak
            else if (!isPasswordStrong(plainTextPassword)) {

                JOptionPane.showMessageDialog(null, "Password is too weak!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(!plainTextPassword.matches(plainTextPassword2)) {
                JOptionPane.showMessageDialog(null, "Passwords Don't Match!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                try{
                    if(!dbconnection.signup(fullname, email, plainTextPassword)){
                    }else{
                        String userEmail = email; 
                        SessionManager.loginUser(userEmail);
                        FormsManager.getInstance().showForm(new SecretKey());
                    }
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erreur while connecting to the database : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
            

        });
        
        passwordStrengthStatus = new PasswordStrengthStatus();
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        
        txtFullname.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Last name");
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        
        JLabel lbTitle = new JLabel("Welcome to ATERIX Application");
        JLabel description = new JLabel("Sign up now and start monetoring your files!");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");
        
         
        passwordStrengthStatus.initPasswordField(txtPassword);
        
        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Full Name"), "gapy 10");
        panel.add(txtFullname);
        panel.add(new JLabel("Username or Email"));
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(passwordStrengthStatus, "gapy 0");
        panel.add(new JLabel("Confirm Password"), "gapy 0");
        panel.add(txtConfirmPassword);
        panel.add(cmdRegister, "gapy 20");
        panel.add(createLoginLabel(), "gapy 10");
        add(panel);
    }
    
    private Component createLoginLabel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JButton cmdLogin = new JButton("<html><a href=\"#\">Sign In</a></html>");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,3,3,3");
        cmdLogin.setContentAreaFilled(false);
        cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdLogin.addActionListener(e -> {
        FormsManager.getInstance().showForm(new Login());
        });
        JLabel label = new JLabel("Already have an account ?");
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");
        panel.add(label);
        panel.add(cmdLogin);
        return panel;
    }
    
    public boolean isMatchPassword() {
        String password = String.valueOf(txtPassword.getPassword());
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());
        return password.equals(confirmPassword);
    }
    
    
    private JTextField txtFullname;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton cmdRegister;
    private PasswordStrengthStatus passwordStrengthStatus;
}
