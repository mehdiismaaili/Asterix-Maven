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
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
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
public class Login  extends JPanel{
    
    private Dbconnection dbconnection; 
    
    public Login(){
        this.dbconnection = new Dbconnection();
        init();
    }
    
    public void init(){
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cmdLogin = new JButton("Login");
        cmdLogin.addActionListener(e -> {
            
        // Declaring strings that contain the names of textFields
        String email = txtUsername.getText();
        String enteredPassword  = txtPassword.getText();
        
        // Check if all the text fields are not empty
        if(email.equals("") || enteredPassword.equals("")){
            
            JOptionPane.showMessageDialog(null, "All fields are required"); 
        }
        else
        {
        try{
            if(dbconnection.signin(email, enteredPassword)){
                String userEmail = email;
                SessionManager.loginUser(userEmail);
                FormsManager.getInstance().showForm(new StepTwoAuthentification());
            }else {
                
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur while connecting to the database : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        }
            
        });
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "fill,250:280"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        JLabel lbTitle = new JLabel("Welcome back!");
        JLabel description = new JLabel("Please sign in to access your account");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Username or Email"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(cmdLogin, "gapy 10");
        panel.add(createSignupLabel(), "gapy 10");
        panel.add(forgotpassLabel(), "gapy 10");
        add(panel);
    }
    
     private Component createSignupLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JButton cmdRegister = new JButton("<html><a href=\"#\">Sign up</a></html>");
        cmdRegister.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,3,3,3");
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> {
        FormsManager.getInstance().showForm(new Register());
        });
        JLabel label = new JLabel("Don't have an account ?");
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");
        panel.add(label);
        panel.add(cmdRegister);
        return panel;
    }
     
    private Component forgotpassLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JButton passforgot = new JButton("<html><a href=\"#\">Click Here!</a></html>");
        passforgot.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,3,3,3");
        passforgot.setContentAreaFilled(false);
        passforgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        passforgot.addActionListener(e -> {
            // Declaring strings that contain the names of textFields
        String email = txtUsername.getText();
        String userEmail = email;
        
        // Calling the sessionmanger class to get the user email from the current session
        SessionManager.loginUser(userEmail);
        
        // checking if the email field is not empty
        if(email.equals("")){
            JOptionPane.showMessageDialog(null, "PLease Enter your email so we can verify your identity", "Error", JOptionPane.ERROR_MESSAGE);
            txtUsername.requestFocus();
        } 
        else 
        {
        // Rediracting the User to the identity verification page
          FormsManager.getInstance().showForm(new ForgotPasword1());
          SessionManager.loginUser(email);
        }
       
        });
        JLabel label = new JLabel("Forgot Password ?");
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");
        panel.add(label);
        panel.add(passforgot);
        return panel;
    } 
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton cmdLogin;
}
