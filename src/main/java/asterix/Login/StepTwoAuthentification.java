/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package asterix.Login;

import com.formdev.flatlaf.FlatClientProperties;
import inc.asterix.fim.Dbconnection;
import inc.asterix.fim.FileMonitoring;
import inc.asterix.fim.SessionManager;
import java.text.Normalizer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import asterix.main.Application;
import asterix.manager.FormsManager;

/**
 *
 * @author mehdi
 */
public class StepTwoAuthentification extends JPanel {
    private Dbconnection dbconnection; 
    
    public StepTwoAuthentification(){
        this.dbconnection = new Dbconnection();
        init();
    }
    
    public void init(){
        
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtsecretkey = new JTextField();
        cmdVerify =  new JButton("Verify");
        cmdVerify.addActionListener(e -> {
        // Declaring strings that contain the names of textFields
        String code = txtsecretkey.getText();
        String useremail = SessionManager.getCurrentUserEmail();
        // Check if all the text fields are not empty
        if(code.equals("")) {
            
            JOptionPane.showMessageDialog(null, "All fields are required"); 
        }
        else
        {
        // Establish the connection
        try{
            if(dbconnection.secretkey(code, useremail)){
                String userEmail = useremail; // Assuming emailTextField is your email input field
                SessionManager.loginUser(userEmail);
                FormsManager.getInstance().closeform();
                FileMonitoring mon = new FileMonitoring();
                mon.setVisible(true);
            }else{
                
            }
        }catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error in database operation", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            }
        }
        });
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");
        
        txtsecretkey.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Secret key");
        cmdVerify.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        
        JLabel lbTitle = new JLabel("Step Two Authentification");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +10");
        
        panel.add(lbTitle);
        panel.add(txtsecretkey);
        panel.add(cmdVerify, "gapy 20");
        add(panel);
    }
     private JTextField txtsecretkey;
     private JButton cmdVerify;
}
