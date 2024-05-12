/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asterix.Login;

import com.formdev.flatlaf.FlatClientProperties;
import inc.asterix.fim.Dbconnection;
import inc.asterix.fim.SessionManager;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import net.miginfocom.swing.MigLayout;
import asterix.component.PasswordStrengthStatus;
import asterix.manager.FormsManager;

/**
 *
 * @author mehdi
 */
public class ForgotPassword2  extends JPanel{
    
    private Dbconnection dbconnection; // calling of the database class
    
    public ForgotPassword2(){
        
        this.dbconnection = new Dbconnection(); // creating an instance of our database class to avoid declaring everytime
        init(); 
    }
    
    
    // Methode for checking if the password is strong
    private boolean isPasswordStrong(String password) {
    // Check if the password contains at least one uppercase letter, one digit, and one special character
    return password.matches(".*[A-Z].*") && password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*()-_+=].*");
    }
    
    public void init(){
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        cmdChangepass = new JButton("Change Password");
        cmdChangepass.addActionListener(e -> {
        // Declaring strings that contain the names of textFields
        String pass1 = txtPassword.getText();
        String pass2 = txtConfirmPassword.getText();
        String useremail = SessionManager.getCurrentUserEmail();
        
        // Check if all the text fields are not empty
        if(pass1.equals("") || pass2.equals("")) {
            
            JOptionPane.showMessageDialog(null, "All fields are required"); 
        }
        else if (pass1.length() < 8) 
        {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long!", "Error", JOptionPane.ERROR_MESSAGE);
            
        } 
        // Condition if the Password  has no special invalide characters
        else if (!pass1.matches("^[a-zA-Z0-9!@#$%^&*()-_+=]+$")) 
        {
                   
            JOptionPane.showMessageDialog(null, "Password contains invalid characters!", "Error", JOptionPane.ERROR_MESSAGE);
            
        }
        // Condition if the Password is not weak
        else if (!isPasswordStrong(pass1)) {
                    
            JOptionPane.showMessageDialog(null, "Password is too weak!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if (!pass1.matches(pass2))
        {
            JOptionPane.showMessageDialog(null, "Passwords don't match"); 
            txtConfirmPassword.requestFocus();
        }
        else
        {
         try{
            dbconnection.passupdate(useremail, pass2);
            FormsManager.getInstance().showForm(new Login());
         }catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error in database operation", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }   
        }
           
        });
         passwordStrengthStatus = new PasswordStrengthStatus();
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        cmdChangepass.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        
        
        JLabel lbTitle = new JLabel("Change Password!");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        passwordStrengthStatus.initPasswordField(txtPassword);
        
        panel.add(lbTitle);
        panel.add(txtPassword);
        panel.add(passwordStrengthStatus, "gapy 0");
        panel.add(new JLabel("Confirm Password"), "gapy 0");
        panel.add(txtConfirmPassword);
        panel.add(cmdChangepass, "gapy 20");
        add(panel);
        
    }
    
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton cmdChangepass;
    private PasswordStrengthStatus passwordStrengthStatus;
    
}
